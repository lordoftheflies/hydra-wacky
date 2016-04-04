#!/usr/bin/python

from __future__ import print_function
import socket
import binascii
import struct
import subprocess
import serial
import time
import sys
import linecache
import ConfigParser
import os
import getopt
# lordoftheflies - hydra ===============================================================================================
import json
import random
import websocket
import thread
import time
import argparse
import ast
import ConfigParser

configFile = "data-ingestion.cfg"
initialSpace = {}
initialSpace['host'] = "localhost"
initialSpace['port'] = 8183
initialSpace['period'] = 1
initialSpace['node'] = "node-id"
initialSpace['protocol'] = "ws"
initialSpace['sensors'] = ["temperature", "humidity", "noise-level", "motion-count"]


def on_message(ws, message):
    print ("Acknowledgement << WS-SERVER[name=data-ingestion]: " + message)


def on_error(ws, error):
    print ("Error << WS-SERVER[name=data-ingestion]: " + error)


def on_close(ws):
    print ("Data-ingestion stream closed.")


def create_datapoint(code, val):
    data_point = {}
    data_point['code'] = code
    data_point['value'] = val
    return json.dumps(data_point)

def on_open(ws):
    def run(*args):
        print ("Open data-ingestion stream ...")
        # both - old ===================================================================================================

        i = 0
        while 1:
            try:
                read_serial()
                sensordata['1'] = str(mtn_cnt) + ',' + str(mtn_perc) + ',' + str(co2) + ',' + str(light) + ',' + str(temp) + ',' + str(hum) + ',' + str(noise) + ',0,\r\n'

                print("Motion count  : " + str(mtn_cnt) + " pcs")
                print("Motion percent: " + str(mtn_perc) + " %")
                print("CO2 gas level : " + str(co2) + " ppm")
                print("Light level   : " + str(light/10) + " lux")
                print("Temperature   : " + str(temp/100) + " C")
                print("Rel. humidity : " + str(hum/100) + " %")
                print("Noise level   : " + str(noise/100) + " dB")
                print("\n")

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

                time.sleep(initialSpace['period'])


                # if config.getboolean("PingRestart", "enabled"):
                #     ping_test()
                #
                # if serversocket == 0:
                #     serversocket = connect()
                #     if serversocket == 0:
                #         print("Error opening socket for Modbus-TCP, is the network connection OK? Retrying in 1 sec.")
                #         time.sleep(1)
                #     else:
                #         print(serversocket)
                #         serversocket.settimeout(1)
                # else:
                #     try:
                #         serversocket.listen(5)
                #         (clientsocket, address) = serversocket.accept()
                #         clientsocket.settimeout(1)
                #         print(address)
                #         query = clientsocket.recv(1024)
                #         wordcnt = ord(query[11])
                #         data_offset = ord(query[9])
                #         if wordcnt > 126:
                #             wordcnt = 126
                #         response[0] = query[0]
                #         response[1] = query[1]
                #         response[6] = query[6]
                #         response[7] = query[7]
                #         response[5] = wordcnt * 2 + 3
                #         response[8] = wordcnt * 2
                #
                #         # responds to requests made to any modbus device address using the same address in response
                #         print("Modbus data requested from unit: " + str.upper(binascii.hexlify(query[6])))
                #         print("Motion count  : " + str(mtn_cnt) + " pcs")
                #         print("Motion percent: " + str(mtn_perc) + " %")
                #         print("CO2 gas level : " + str(co2) + " ppm")
                #         print("Light level   : " + str(light / 10) + " lux")
                #         print("Temperature   : " + str(temp / 100) + " C")
                #         print("Rel. humidity : " + str(hum / 100) + " %")
                #         print("Noise level   : " + str(noise / 100) + " dB")
                #         response_key = '1'
                #
                #         if response_key in sensordata:
                #             for x in range(0, wordcnt):
                #                 try:
                #                     data[2 * x:2 * x + 2] = struct.pack('>H', int(sensordata[response_key].split(',')[x]))
                #                 except ValueError:
                #                     continue
                #                 except IndexError:
                #                     continue
                #                 except struct.error:
                #                     errorlog(1)
                #                     continue
                #
                #             for x in range(0, wordcnt * 2):
                #                 response[x + 9] = data[x + data_offset * 2]
                #
                #             print(binascii.hexlify(response[0:wordcnt * 2 + 9]))
                #             print(time.strftime("%H:%M:%S ", time.localtime()))
                #             print("Offset:" + str(data_offset))
                #             print("Wordcnt:" + str(wordcnt) + "\r\n")
                #             clientsocket.send(response[0:wordcnt * 2 + 9])
                #         clientsocket.close()
                #     except socket.timeout:
                #         continue
                #     except socket.error:
                #         errorlog(2)
                #         serversocket = 0
                #     except IndexError:
                #         query_bk = query
                #         data_bk = data
                #         response_bk = response
                #         errorlog(8)
                #         continue
                #     except Exception, e:
                #         print(e.message)
                #         error_description = e.message
                #         errorlog(6)
                #         continue

            except Exception, e:
                print(e.message)
                error_description = e.message
                errorlog(6)


                # ==============================================================================================================


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
# ======================================================================================================================


def connect():
    own_addr = config_section_map("ModbusTCP")['address']
    port = config_section_map("ModbusTCP")['port']

    try:
        dummysock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        dummysock.connect(('8.8.8.8', 0))  # connecting to a UDP address doesn't send packets
        own_addr = dummysock.getsockname()[0]
        print("Listening on: " + own_addr)
        serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        serversocket.bind((own_addr, int(port)))  # socket.gethostname()
        serversocket.settimeout(1)
        return serversocket
    except Exception:
        return 0


def read_serial():
    global port, light, mtn_cnt, mtn_perc, hum, temp, co2, noise
    read_avago = "\x3A\x45\x45\x30\x33\x30\x30\x30\x30\x30\x30\x30\x33\x30\x43\x0D\x0A"
    read_sht25 = "\x3A\x45\x46\x30\x33\x30\x30\x30\x30\x30\x30\x30\x32\x30\x43\x0D\x0A"
    read_telaire = "\x3A\x46\x30\x30\x33\x30\x30\x30\x30\x30\x30\x30\x31\x30\x43\x0D\x0A"
    read_adau = "\x3A\x46\x31\x30\x33\x30\x30\x30\x30\x30\x30\x30\x31\x30\x42\x0D\x0A"

    while True:
        try:
            rcv = ''
            while rcv == '':
                port.write(read_avago)
                rcv = port.readline()
                if len(rcv) >= 19:
                    # print (rcv)
                    light = int((rcv[7:11]), 16)
                    mtn_cnt = int((rcv[11:15]), 16)
                    mtn_perc = int((rcv[15:19]), 16)
        except Exception:
            continue
        break

    while True:
        try:
            rcv = ''
            while rcv == '':
                port.write(read_sht25)
                rcv = port.readline()
                if len(rcv) >= 15:
                    # print (rcv)
                    hum = int((rcv[7:11]), 16)
                    temp = int((rcv[11:15]), 16)
        except Exception:
            continue
        break

    while True:
        try:
            rcv = ''
            while rcv == '':
                port.write(read_telaire)
                rcv = port.readline()
                if len(rcv) >= 11:
                    # print (rcv)
                    co2 = int((rcv[7:11]), 16)
        except Exception:
            continue
        break

    while True:
        try:
            rcv = ''
            while rcv == '':
                port.write(read_adau)
                rcv = port.readline()
                if len(rcv) >= 11:
                    # print (rcv)
                    noise = int((rcv[7:11]), 16)
        except Exception:
            continue
        break

    port.flushInput()


def restart():
    print("Network connection broken, restarting...")
    errorlog(3)
    command = "/usr/bin/sudo /sbin/reboot"
    process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
    output = process.communicate()[0]
    print(output)


def errorlog(num):
    global broken_pings, sensordata, error_description, query_bk, response_bk, data_bk, error_logfile
    if num != 7:
        print("Runtime error occurred, error logfile updated.")
    file = open(error_logfile, "a")
    file.write(time.strftime("%Y-%m-%d %H:%M:%S: ", time.localtime()))
    if num == 1:
        file.write("Incoming value error - ")
        for key in sensordata:
            file.write(sensordata[key] + ',')
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

modbus_use_own_addr = 1
modbus_order = ''
serversocket = 0
sensordata = {}
modbus_devices = {}
config = ConfigParser.ConfigParser()
config.read("config.ini")
error_logfile = config_section_map("LogFiles")['error_log']
light = 0
mtn_cnt = 0
mtn_perc = 0
hum = 0
temp = 0
co2 = 0
noise = 0
response = bytearray(b'\x00' * 261)
data = bytearray(b'\x00' * 252)
broken_pings = 0
error_description = 0
response_key = ''

HELP="kaka"

    try:
    (optlist, args) = getopt.getopt(sys.argv[1:], "", ["help", "modbus_order="])
except Exception, e:
    print("\r\nCannot parse command line: " + str(e) + "\r\n")
    sys.stderr.write(HELP + "\n")
    sys.exit(1)

if args:
    print("\r\nNo arguments expected.\r\n")
    sys.stderr.write(HELP + "\n")
    sys.exit(1)

for (opt, arg) in optlist:
    if opt == "--help":
        sys.stderr.write(HELP + "\n")
        sys.exit(1)
    elif opt == "--modbus_order":
        modbus_use_own_addr = 0
        modbus_order = arg
        modbus_devices = modbus_order.split(',')

modbus_order = str.upper(modbus_order)

print("Start...")

errorlog(7)

port = serial.Serial("/dev/ttyAMA0", baudrate=19200, timeout=0.5)

# ======================================================================================================================
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
# ======================================================================================================================

# while 1:
#     try:
#         read_serial()
#         sensordata['1'] = str(mtn_cnt) + ',' + str(mtn_perc) + ',' + str(co2) + ',' + str(light) + ',' + str(
#             temp) + ',' + str(hum) + ',' + str(noise) + ',0,\r\n'
#
#         # print("Motion count  : " + str(mtn_cnt) + " pcs")
#         # print("Motion percent: " + str(mtn_perc) + " %")
#         # print("CO2 gas level : " + str(co2) + " ppm")
#         # print("Light level   : " + str(light/10) + " lux")
#         # print("Temperature   : " + str(temp/100) + " C")
#         # print("Rel. humidity : " + str(hum/100) + " %")
#         # print("Noise level   : " + str(noise/100) + " dB")
#         # print("\n")
#
#         if config.getboolean("PingRestart", "enabled"):
#             ping_test()
#
#         if serversocket == 0:
#             serversocket = connect()
#             if serversocket == 0:
#                 print("Error opening socket for Modbus-TCP, is the network connection OK? Retrying in 1 sec.")
#                 time.sleep(1)
#             else:
#                 print(serversocket)
#                 serversocket.settimeout(1)
#         else:
#             try:
#                 serversocket.listen(5)
#                 (clientsocket, address) = serversocket.accept()
#                 clientsocket.settimeout(1)
#                 print(address)
#                 query = clientsocket.recv(1024)
#                 wordcnt = ord(query[11])
#                 data_offset = ord(query[9])
#                 if wordcnt > 126:
#                     wordcnt = 126
#                 response[0] = query[0]
#                 response[1] = query[1]
#                 response[6] = query[6]
#                 response[7] = query[7]
#                 response[5] = wordcnt * 2 + 3
#                 response[8] = wordcnt * 2
#
#                 # responds to requests made to any modbus device address using the same address in response
#                 print("Modbus data requested from unit: " + str.upper(binascii.hexlify(query[6])))
#                 print("Motion count  : " + str(mtn_cnt) + " pcs")
#                 print("Motion percent: " + str(mtn_perc) + " %")
#                 print("CO2 gas level : " + str(co2) + " ppm")
#                 print("Light level   : " + str(light / 10) + " lux")
#                 print("Temperature   : " + str(temp / 100) + " C")
#                 print("Rel. humidity : " + str(hum / 100) + " %")
#                 print("Noise level   : " + str(noise / 100) + " dB")
#                 response_key = '1'
#
#                 if response_key in sensordata:
#                     for x in range(0, wordcnt):
#                         try:
#                             data[2 * x:2 * x + 2] = struct.pack('>H', int(sensordata[response_key].split(',')[x]))
#                         except ValueError:
#                             continue
#                         except IndexError:
#                             continue
#                         except struct.error:
#                             errorlog(1)
#                             continue
#
#                     for x in range(0, wordcnt * 2):
#                         response[x + 9] = data[x + data_offset * 2]
#
#                     print(binascii.hexlify(response[0:wordcnt * 2 + 9]))
#                     print(time.strftime("%H:%M:%S ", time.localtime()))
#                     print("Offset:" + str(data_offset))
#                     print("Wordcnt:" + str(wordcnt) + "\r\n")
#                     clientsocket.send(response[0:wordcnt * 2 + 9])
#                 clientsocket.close()
#             except socket.timeout:
#                 continue
#             except socket.error:
#                 errorlog(2)
#                 serversocket = 0
#             except IndexError:
#                 query_bk = query
#                 data_bk = data
#                 response_bk = response
#                 errorlog(8)
#                 continue
#             except Exception, e:
#                 print(e.message)
#                 error_description = e.message
#                 errorlog(6)
#                 continue
#
#     except Exception:
#         errorlog(10)
