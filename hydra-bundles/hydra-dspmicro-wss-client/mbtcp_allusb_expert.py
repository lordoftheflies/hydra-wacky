#!/usr/bin/python
#from __future__ import print_function
import socket
import binascii
import struct
import subprocess
import serial
import time
import usb.core
import usb.util
import sys
import linecache
import ConfigParser
import os
import getopt
#import websocket

# lordoftheflies - river ===============================================================================================
import json
import random
import websocket
import thread
import time
import argparse
import ast
import ConfigParser
# lordoftheflies - river ===============================================================================================




# find our devices
dev_avago = usb.core.find(idVendor=0x16c0, idProduct=0x03ee)
dev_sht25 = usb.core.find(idVendor=0x16c0, idProduct=0x03ef)
dev_telaire = usb.core.find(idVendor=0x16c0, idProduct=0x03f0)
dev_adau = usb.core.find(idVendor=0x16c0, idProduct=0x03f1)
dev_default = usb.core.find(idVendor=0x16c0, idProduct=0x05df)


# was it found?
if dev_avago is None:
    print('Light and motion sensor device not connected.')
    avago_en = 0
else:
    print('Found light and motion sensor device.')
    avago_en = 1
if dev_sht25 is None:
    print('Temperature and humidity sensor device not connected.')
    sht25_en = 0
else:
    print('Found temperature and humidity sensor device.')
    sht25_en = 1
if dev_telaire is None:
    print('CO2 gas sensor device not connected.')
    telaire_en = 0
else:
    print('Found CO2 gas sensor device.')
    telaire_en = 1
if dev_adau is None:
    print('Ambient noise sensor device not connected.')
    adau_en = 0
else:
    print('Found ambient noise sensor device.')
    adau_en = 1
if dev_default is None:
    print('Default device not found.')
else:
    print('Found default device.')
print '\n'

# HELP = """Options:
# --help                                        Print this help message.
# --websocket_enable                            Enable websocket data streaming and logging to file. You have to specify MAC and GUID for enabling.
# --MAC 60:53:B2                                Select sensor unit to receive data from by specifying the 3-byte mac address of the SNAP module in it.
# --GUID ff78284c-f954-40f9-acd9-47b306c02603   Set the GUID to use for websocket data streaming.
# --modbus_enable                               Enable modbus-TCP data sending. You have to specify unit order or switch to using their own address.
# --modbus_order 60:53:B2,60:55:A6...           Specify which units you want to reach on modbus unit ID 1, 2, 3...
# --modbus_use_own_addr                         Set the modbus ID of the units to the last byte of their 3-byte MAC address.
# """

def connect():
    own_addr = config_section_map("ModbusTCP")['address'] # Not used if address discovery if enabled
    port = config_section_map("ModbusTCP")['port']

    try:
        dummysock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # Finding out own ip address
        dummysock.connect(('8.8.8.8', 0))
        own_addr = dummysock.getsockname()[0]
        print "Listening on: " + own_addr
        serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        serversocket.bind((own_addr, int(port)))
        serversocket.settimeout(1)
        return serversocket
    except Exception:
        return 0

def read_avago():
    global light, mtn_cnt, mtn_perc, dev_avago
    try:
        ret = dev_avago.ctrl_transfer(0xC0, 3, 0, 0, 6)
        light = (256*ret[0])+ret[1]
        mtn_cnt = (256*ret[2])+ret[3]
        mtn_perc = ret[5]
        #print "Light level   : " + str(light) + "lux"
        #print "Motion count  : " + str(mtn_cnt) + "pcs"
        #print "Motion percent: " + str(mtn_perc) + "%"
        return 1
    except Exception:
        time.sleep(0.1)
        return 0

def read_sht25():
    global temp, hum, dev_sht25
    try:
        ret = dev_sht25.ctrl_transfer(0xC0, 3, 0, 0, 4)
        hum = (256*ret[0])+ret[1]
        temp = (256*ret[2])+ret[3]
        #print "Temperature   : " + str(temp) + "C"
        #print "Rel. humidity : " + str(hum) + "%"
        return 1
    except Exception:
        time.sleep(0.1)
        return 0

def read_telaire():
    global co2, co2_prev, co2_prev2, dev_telaire
    try:
        ret = dev_telaire.ctrl_transfer(0xC0, 3, 0, 0, 2)
        tmp = (256*ret[0])+ret[1]
        co2_prev2 = co2_prev
        co2_prev = tmp
        if tmp == 0:
            if co2_prev == 0 and co2_prev2 == 0:
                co2 = 0
        else:
            co2 = tmp
        #print "CO2 gas level : " + str(co2) + "ppm"
        return 1
    except Exception:
        time.sleep(0.1)
        return 0

def read_adau():
    global noise, dev_adau
    try:
        debug_file.write("Function: try start\r\n")
        ret = dev_adau.ctrl_transfer(0xC0, 3, 0, 0, 2, 100)
        debug_file.write("Function: usb transmission done\r\n")
        noise = (256*ret[0])+ret[1]
        debug_file.write("Function: Calculation done\r\n")
        #print "Noise level   : " + str(noise) + "dB"
        return 1
    except Exception, e:
        debug_file.write("Function: Exception\r\n")
        #print e
        time.sleep(0.1)
        return 0

def log_data():
    global sensordata, sensor_logfile, MAC
    if MAC in sensordata:
        file = open(sensor_logfile + str(MAC) + ".csv", "a+")
        if os.stat(sensor_logfile + str(MAC) + ".csv").st_size == 0:
            file.write(header+"\r\n")
        file.write(time.strftime("%Y-%m-%d,%H:%M:%S,", time.localtime()))
        file.write(sensordata[MAC]+"\r\n")
        file.close
        print ("File write sensor_logfile_" + str(MAC) + ".csv OK")

def restart():
    print ("Network connection broken, restarting...")
    errorlog(3)
    command = "/usr/bin/sudo /sbin/reboot"
    process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
    output = process.communicate()[0]
    print (output)

def errorlog(num):
    global broken_pings, sensordata, error_description, query_bk, response_bk, data_bk, error_logfile
    if num != 7:
        print ("Runtime error occurred, error logfile updated.")
    file = open(error_logfile, "a+")
    file.write(time.strftime("%Y-%m-%d %H:%M:%S: ", time.localtime()))
    if num == 1:
        file.write("Incoming value error - ")
        for key in sensordata:
            file.write(sensordata[key]+',')
        file.seek(-1, os.SEEK_CURR)
        file.write('\r\n')
    if num == 2:
        file.write("Socket error\r\n")
    if num == 3:
        file.write("Network connection broken, rebooting...\r\n")
    if num == 4:
        file.write("Broken ping count: ")
        file.write(str(broken_pings))
        file.write("\r\n")
    if num == 5:
        file.write("No route to host (network lost).\r\n")
    if num == 6:
        file.write("Other unhandled exception occured: ")
        file.write(error_description)
        exc_type, exc_obj, tb = sys.exc_info()
        f = tb.tb_frame
        lineno = tb.tb_lineno
        filename = f.f_code.co_filename
        linecache.checkcache(filename)
        line = linecache.getline(filename, lineno, f.f_globals)
        file.write('EXCEPTION IN ({}, LINE {} "{}"): {}'.format(filename, lineno, line.strip(), exc_obj))
        file.write("\r\n")
    if num == 7:
        file.write("REBOOTED.\r\n")
    if num == 8:
        file.write("Index error at calculating query from response. ")
        file.write('Error in line {} '.format(sys.exc_info()[-1].tb_lineno))
        file.write("Query: ")
        file.write(binascii.hexlify(query_bk))
        file.write(" Data: ")
        file.write(binascii.hexlify(data_bk))
        file.write(" Response: ")
        file.write(binascii.hexlify(response_bk))
        file.write("\r\n")
    if num == 9:
        file.write("Serial port error. Trying to reset port without reboot...\r\n")
    if num == 10:
        file.write("Error parsing the config file!\r\n")
    file.close

def ping_test():
    global broken_pings

    ip_addr = config_section_map("PingRestart")['address']
    max_broken = config_section_map("PingRestart")['restart_after']

    process = subprocess.Popen("ping " + ip_addr + " -c 1 | grep received", stdout=subprocess.PIPE, shell=True)
    ping_resp = process.stdout.read()

    try:
        ping_resp = int(ping_resp.split(' ')[3])
        if not ping_resp:
            broken_pings += 1
            errorlog(4)
        else:
            broken_pings = 0

    except IndexError:
        errorlog(5)
        time.sleep(1)
        broken_pings += 1

    if broken_pings >= int(max_broken):
        restart()

def config_section_map(section):
    global config
    dict1 = {}
    options = config.options(section)
    for option in options:
        try:
            dict1[option] = config.get(section, option)
            if dict1[option] == -1:
                errorlog(10)
        except Exception:
            errorlog(10)
            dict1[option] = None
    return dict1

# lordoftheflies - river ===============================================================================================

def create_datapoint(code, val):
    data_point = {}
    data_point['code'] = code
    data_point['value'] = val
    return json.dumps(data_point)

def on_message(ws, message):
    print ("Acknowledgement << WS-SERVER[name=data-ingestion]: " + message)


def on_error(ws, error):
    print ("Error << WS-SERVER[name=data-ingestion]: " + error)


def on_close(ws):
    print ("Data-ingestion stream closed.")


def on_open(ws):
    def run(*args):
        global adau_en, avago_en, telaire_en, sht25_en, mtn_perc, mtn_cnt, co2, light, temp, hum, noise, ws


        print ("Open data-ingestion stream ...")

        # both - main ==========================================================================================================

        while 1:
            try:
                debug_file.write(time.strftime("\r\n%Y-%m-%d %H:%M:%S\r\n", time.localtime()))
                if avago_en:
                    usb_success = 0
                    try_ctr = 0
                    while not usb_success:
                        try_ctr += 1
                        if try_ctr == 4:
                            dev_avago = usb.core.find(idVendor=0x16c0, idProduct=0x03ee)
                        usb_success = read_avago()
                        if try_ctr == 7:
                            avago_en = 0
                            usb_success = 1
                            light = 0
                            mtn_cnt = 0
                            mtn_perc =0

                if sht25_en:
                    usb_success = 0
                    try_ctr = 0
                    while not usb_success:
                        try_ctr += 1
                        if try_ctr == 4:
                            dev_sht25 = usb.core.find(idVendor=0x16c0, idProduct=0x03ef)
                        usb_success = read_sht25()
                        if try_ctr == 7:
                            sht25_en = 0
                            usb_success = 1
                            hum = 0
                            temp = 0

                if telaire_en:
                    usb_success = 0
                    try_ctr = 0
                    while not usb_success:
                        try_ctr += 1
                        if try_ctr == 4:
                            dev_telaire = usb.core.find(idVendor=0x16c0, idProduct=0x03f0)
                        usb_success = read_telaire()
                        if try_ctr == 7:
                            telaire_en = 0
                            usb_success = 1
                            co2 = 0
                debug_file.write("Reading Adau sensor\r\n")
                if adau_en:
                    debug_file.write("Adau_en\r\n")
                    usb_success = 0
                    try_ctr = 0
                    debug_file.write("Zeroed variables\r\n")
                    while not usb_success:
                        debug_file.write("Not yet usb success\r\n")
                        try_ctr += 1
                        if try_ctr == 4:
                            debug_file.write("Reconnecting dev_adau\r\n")
                            dev_adau = usb.core.find(idVendor=0x16c0, idProduct=0x03f1)
                        debug_file.write("Before read function\r\n")
                        usb_success = read_adau()
                        debug_file.write("After read function\r\n")
                        if try_ctr == 7:
                            debug_file.write("Try ctr = 7\r\n")
                            adau_en = 0
                            usb_success = 1
                            noise = 0
                        debug_file.write("Done not_usb_success cycle\r\n")
                    debug_file.write("USB success\r\n")

                debug_file.write("After adau read\r\n")
                sensordata['1'] = str(mtn_cnt) + ',' + str(mtn_perc) + ',' + str(co2) + ',' + str(light) + ',' + str(temp) + ',' + str(hum) + ',' + str(noise) + ',0,\r\n'

                i = 0
                # Generated datapoints
                json_message = create_datapoint("motion-count", mtn_cnt)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("motion-avg", mtn_perc)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("co2-concentration", co2)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("light-level", light)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("temperature", temp)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("humidity", hum)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1
                json_message = create_datapoint("noise-level", noise)
                ws.send(json_message)
                print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)
                i += 1

                try:
                    if config.getboolean("PingRestart", "enabled"):
                        ping_test()
                except Exception:
                    errorlog(10)

                if websocket_enable:
                    print("Sending data " + MAC + " ...")
                    url = "ws://" + ws_address + ":" + ws_port + "/data/" + GUID + "/realtime"
                    print("Url: " + url)
                    ws = websocket.create_connection(url)
                    values = sensordata[MAC].split(',')
                    for pos, value in enumerate(values):
                        print("Sending streamed data from ID: " + str(pos) + " key: " + str(ws_keys[pos]) +  "...")
                        ws.send(str(ws_keys[pos]) + "=" + str(value))
                        print("Sent streamed data: OK.")
                    ws.close()
                    print("Data sent.\r\n")
                    log_data()

                if modbus_enable:
                    if serversocket == 0:
                        serversocket = connect()
                        if serversocket == 0:
                            print ("Error opening socket for Modbus-TCP, is the network connectiok OK? Retrying in 1 sec.")
                            time.sleep(1)
                        else:
                            print (serversocket)
                            serversocket.settimeout(1)
                    else:
                        try:
                            serversocket.listen(5)
                            (clientsocket, address) = serversocket.accept()
                            clientsocket.settimeout(1)
                            print (address)
                            query = clientsocket.recv(1024)
                            wordcnt = ord(query[11])
                            data_offset = ord(query[9])
                            if wordcnt > 126:
                                wordcnt = 126
                            response[0] = query[0]
                            response[1] = query[1]
                            response[6] = query[6]
                            response[7] = query[7]
                            response[5] = wordcnt*2+3
                            response[8] = wordcnt*2
                            # responds to requests made to any modbus device address using the same address in response
                            print("Modbus data requested from unit: " + str.upper(binascii.hexlify(query[6])))
                            print "Motion count  : " + str(mtn_cnt) + "pcs"
                            print "Motion percent: " + str(mtn_perc) + "%"
                            print "CO2 gas level : " + str(co2) + "ppm"
                            print "Light level   : " + str(light/10) + "lux"
                            print "Temperature   : " + str(temp/100) + "C"
                            print "Rel. humidity : " + str(hum/100) + "%"
                            print "Noise level   : " + str(noise/100) + "dB"
                            response_key = '1'
                            if response_key in sensordata:
                                for x in range(0, wordcnt):
                                    try:
                                        data[2*x:2*x+2] = struct.pack('>H', int(sensordata[response_key].split(',')[x]))
                                    except ValueError:
                                        continue
                                    except IndexError:
                                        continue
                                    except struct.error:
                                        errorlog(1)
                                        continue
                                for x in range(0, wordcnt*2):
                                    response[x+9] = data[x+data_offset*2]
                                print (binascii.hexlify(response[0:wordcnt*2+9]))
                                print (time.strftime("%H:%M:%S ", time.localtime()))
                                print ("Offset:" + str(data_offset))
                                print ("Wordcnt:" + str(wordcnt) + "\r\n")
                                clientsocket.send(response[0:wordcnt*2+9])
                            clientsocket.close()
                        except socket.timeout:
                            continue
                        except socket.error:
                            errorlog(2)
                            serversocket = 0
                        except IndexError:
                            query_bk = query
                            data_bk = data
                            response_bk = response
                            errorlog(8)
                            continue
            except Exception, e:
               print e.message
               # error_description = e.message
               # errorlog(6)
               # continue


        # both - main ==========================================================================================================

        print ("Close data-ingestion stream ...")
        time.sleep(1)
        ws.close()

    thread.start_new_thread(run, ())


def loadConfiguration(fileName, space):
    config = ConfigParser.SafeConfigParser({
        'node': 'unknown-node',
        'sensors': [],
        'period': 1,

        'protocol': 'ws',
        'host': 'localhost',
        'port': 8183,
    })
    config.read(fileName)

    try:
        space['node'] = config.get("machine-controller", "node")
        space['sensors'] = ast.literal_eval(config.get("machine-controller", "sensors"))
    except ConfigParser.NoSectionError:
        config.add_section("machine-controller")
        config.set("machine-controller", "node", space['node'])
        config.set("machine-controller", "sensors", space['sensors'])

    try:
        space['protocol'] = config.get("ingestion-gateway", "protocol")
        space['host'] = config.get("ingestion-gateway", "host")
        space['port'] = config.getint("ingestion-gateway", "port")
    except ConfigParser.NoSectionError:
        config.add_section("ingestion-gateway")
        config.set("ingestion-gateway", "protocol", space['protocol'])
        config.set("ingestion-gateway", "host", space['host'])
        config.set("ingestion-gateway", "port", str(space['port']))

    try:
        space['period'] = config.getfloat("sensor-sampling", "period")
    except ConfigParser.NoSectionError:
        config.add_section("sensor-sampling")
        config.set("sensor-sampling", "period", str(space['period']))


def saveConfiguration(fileName, space):
    config = ConfigParser.SafeConfigParser()

    if (not config.has_section("machine-controller")):
        config.add_section("machine-controller")
    config.set("machine-controller", "node", space['node'])
    config.set("machine-controller", "sensors", str(space['sensors']))

    if (not config.has_section("ingestion-gateway")):
        config.add_section("ingestion-gateway")
    config.set("ingestion-gateway", "protocol", space['protocol'])
    config.set("ingestion-gateway", "host", space['host'])
    config.set("ingestion-gateway", "port", str(space['port']))

    if (not config.has_section("sensor-sampling")):
        config.add_section("sensor-sampling")
    config.set("sensor-sampling", "period", str(space['period']))

    # Writing our configuration file.
    with open(fileName, 'wb') as configfile:
        config.write(configfile)
# lordoftheflies - river ===============================================================================================

modbus_enable=0
websocket_enable=0
modbus_use_own_addr=0
MAC = ''
GUID = ''
modbus_order= ''
response = bytearray(b'\x00' * 261)
data = bytearray(b'\x00' * 252)
broken_pings = 0
serversocket = 0
error_description = 0
ping_test_enabled = 0
response_key=''
sensordata = {}
modbus_devices = {}
ws_keys = [0,0,0,0,0,0,0,0]
config = ConfigParser.ConfigParser()
config.read("./config.ini")
sensor_logfile = config_section_map("LogFiles")['sensor_log']
error_logfile = config_section_map("LogFiles")['error_log']
header = config_section_map("Header")['header']
ws_address = config_section_map("Websocket")['address']
ws_port = config_section_map("Websocket")['port']
# port = serial.Serial("/dev/ttyACM0", baudrate=115200)
light = 0
mtn_cnt = 0
mtn_perc =0
hum = 0
temp = 0
co2 = 0
co2_prev = 0
co2_prev2 = 0
noise = 0
usb_success = 0
try_ctr = 0

for i in range(8):
    ws_keys[i] = config_section_map("Keys")[str(i)]

# try:
#     (optlist, args) = getopt.getopt(sys.argv[1:], "", ["help", "websocket_enable", "MAC=", "GUID=", "modbus_enable", "modbus_order=", "modbus_use_own_addr" ])
# except Exception, e:
#     print ("\r\nCannot parse command line: " + str(e) + "\r\n")
#     sys.stderr.write(HELP + "\n" )
#     sys.exit( 1 )
#
# if args:
#     print ("\r\nNo arguments expected.\r\n")
#     sys.stderr.write(HELP + "\n" )
#     sys.exit( 1 )
#
# if len(sys.argv) == 1: # if only 1 argument, it's the script name
#     sys.stderr.write(HELP + "\n" )
#     sys.exit( 1 )

# for (opt, arg) in optlist:
#     if opt == "--help":
#         sys.stderr.write(HELP + "\n" )
#         sys.exit( 1 )
#     if opt == "--MAC":
#         MAC = arg
#     elif opt == "--GUID":
#         GUID = arg
#     elif opt == "--modbus_order":
#         modbus_order = arg
#     elif opt == "--modbus_use_own_addr":
#         modbus_use_own_addr=1
#     elif opt == "--websocket_enable":
#         websocket_enable=1
#     elif opt == "--modbus_enable":
#         modbus_enable=1

modbus_use_own_addr=1
modbus_enable=1

# lrodfotheflies - river ===============================================================================================
configFile = "data-ingestion.cfg"
initialSpace = {}
initialSpace['host'] = "localhost"
initialSpace['port'] = 8183
initialSpace['period'] = 1
initialSpace['node'] = "node-id"
initialSpace['protocol'] = "ws"
initialSpace['sensors'] = ["temperature", "humidity", "noise-level", "motion-count"]

# lrodfotheflies - river ===============================================================================================

# if websocket_enable:
#     if not MAC or not GUID:
#         print ("\r\nMAC address or GUID was not given.\r\n")
#         sys.stderr.write(HELP + "\n" )
#         sys.exit( 1 )
#
# if modbus_enable:
#     if not modbus_order and not modbus_use_own_addr:
#         print ("\r\nModbus usage type was not defined.\r\n")
#         sys.stderr.write(HELP + "\n" )
#         sys.exit( 1 )
#
# if not websocket_enable and not modbus_enable:
#     print ("\r\nNo function is enabled, exiting.\r\n")
#     sys.stderr.write(HELP + "\n" )
#     sys.exit( 1 )

MAC=str.upper(MAC)
GUID=str.upper(GUID)
modbus_order=str.upper(modbus_order)	

print ("Start")
if modbus_order:
    modbus_devices=modbus_order.split(',')



errorlog(7)
debug_file = open("debug.txt", "a+", 1)

# lordoftheflies - river ===============================================================================================
parser = argparse.ArgumentParser(description='Process some integers.')

# Parsing argument from command line (these options prioritized)
parser.add_argument("-c", "--config", help="Path of the configuration file.", type=str)
parser.add_argument("-u", "--update", help="Persist runtime configuration.", action="store_true")
parser.add_argument("-n", "--node", help="Identifier of the Asset.", type=str)
parser.add_argument("-p", "--period", help="Sampling period (in ms).", type=int)
parser.add_argument("-v", "--verbose", help="Increase output verbosity", action="store_true")

args = parser.parse_args()

if (args.verbose):
    print ("Verbosing system output.")
    websocket.enableTrace(True)
else:
    websocket.enableTrace(False)

if (args.config):
    print ("Use configuration file " + args.config)
    configFile = args.config
loadConfiguration(configFile, initialSpace)

if (args.update):
    print ("Persist configuration to " + configFile)
    saveConfiguration(configFile, initialSpace)

if (args.period):
    period = args.period
    print ("Use sampling period: " + str(args.period))
else:
    print ("Use default sampling period: " + str(initialSpace['period']))

if (args.node):
    period = args.node
    print ("Use asset node id: " + args.node)
else:
    print ("Use default asset node id: " + initialSpace['node'])


print ("Start driver for the asset[" + initialSpace['node'] + "] ...")

dataStreamUrl = initialSpace['protocol'] + "://" + initialSpace['host'] + ":" + str(
    initialSpace['port']) + "/data/" + initialSpace['node'] + "/ingest"
# dataStreamUrl = initialSpace['protocol']+"://" + initialSpace['host'] + ":" + str(initialSpace['port']) + "/SaveTimeSeriesData"
print ("Connecting to field agent (" + dataStreamUrl + ")")
ws = websocket.WebSocketApp(dataStreamUrl,
                            on_message=on_message,
                            on_error=on_error,
                            on_close=on_close)
ws.on_open = on_open
ws.run_forever()
# lordoftheflies - river ===============================================================================================