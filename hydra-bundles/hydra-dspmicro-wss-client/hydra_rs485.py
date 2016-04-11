#!/usr/bin/python
from __future__ import print_function
import socket
import struct
import subprocess
import time
import sys
import linecache
import ConfigParser
import getopt
import serial


def connect():
    own_addr = config_section_map("ModbusTCP")['address'] # Not used because address discovery is enabled (3 rows after try)
    port = config_section_map("ModbusTCP")['port']

    try:
        dummysock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM) # Finding out own ip address
        dummysock.connect(('8.8.8.8', 0))
        own_addr = dummysock.getsockname()[0]
        print ("Listening on: " + own_addr)
        serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        serversocket.bind((own_addr, int(port)))
        serversocket.settimeout(1)
        return serversocket
    except Exception:
        return 0

def restart():
    errorlog("Network connection broken, rebooting...\r\n")
    command = "/usr/bin/sudo /sbin/reboot"
    process = subprocess.Popen(command.split(), stdout=subprocess.PIPE)
    output = process.communicate()[0]
    print (output)


def errorlog(error_description):
    global error_logfile

    file = open(error_logfile, "a")
    file.write(time.strftime("%Y-%m-%d %H:%M:%S: ", time.localtime()))
    file.write(error_description)
    exc_type, exc_obj, tb = sys.exc_info()
    if tb:
        f = tb.tb_frame
        lineno = tb.tb_lineno
        filename = f.f_code.co_filename
        linecache.checkcache(filename)
        line = linecache.getline(filename, lineno, f.f_globals)
        file.write(', EXCEPTION IN ({}, LINE {} "{}"): {}'.format(filename, lineno, line.strip(), exc_obj))
    file.write("\r\n")
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
            errorlog("Broken ping!")
        else:
            broken_pings = 0

    except IndexError, e:
        errorlog(e.message) #No route to host (network lost)
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
                errorlog("Error parsing the config file!\r\n")
        except Exception, e:
            errorlog(e.message)
            dict1[option] = None
    return dict1

def calculateLRC(input):
    lrc = 0
    cnt = 1
    length = len(input) - 5
    try:
        while cnt < length:
            lrc = lrc - int((input[cnt:cnt+2]), 16)
            cnt = cnt + 2
        if (lrc & 0xff) == (int((input[cnt:cnt+2]), 16)):
            return True
        else:
            return False
    except Exception:
        return False

def read_serial():
    global port, light, light_prev, light_prev2, mtn_cnt, mtn_perc, hum, hum_prev, hum_prev2, temp, temp_prev, temp_prev2, co2, co2_prev, co2_prev2, noise, noise_prev, noise_prev2
    read_avago =   "\x3A\x45\x45\x30\x33\x30\x30\x30\x30\x30\x30\x30\x33\x30\x43\x0D\x0A"
    read_sht25 =   "\x3A\x45\x46\x30\x33\x30\x30\x30\x30\x30\x30\x30\x32\x30\x43\x0D\x0A"
    read_telaire = "\x3A\x46\x30\x30\x33\x30\x30\x30\x30\x30\x30\x30\x31\x30\x43\x0D\x0A"
    read_adau =    "\x3A\x46\x31\x30\x33\x30\x30\x30\x30\x30\x30\x30\x31\x30\x42\x0D\x0A"

    while True:
        try:
            rcv = ''
            port.write(read_avago)
            rcv = port.readline()
            if len(rcv) >= 19:
                if not (calculateLRC(rcv)):
                    continue
                tmp = int((rcv[7:11]), 16)
                light_prev2 = light_prev
                light_prev = tmp
                if tmp == 0:
                    if light_prev == 0 and light_prev2 == 0:
                        light = 0
                else:
                    light = tmp
                mtn_cnt = int((rcv[11:15]), 16)
                mtn_perc = int((rcv[15:19]), 16)
        except Exception, e:
            print (e)
            continue
        break
		
    while True:
        try:
            rcv = ''
            port.write(read_sht25)
            rcv = port.readline()
            if len(rcv) >= 15:
                if not (calculateLRC(rcv)):
                    continue
                tmp = int((rcv[7:11]), 16)
                hum_prev2 = hum_prev
                hum_prev = tmp
                if tmp == 0:
                    if hum_prev == 0 and hum_prev2 == 0:
                        hum = 0
                else:
                    hum = tmp
                tmp = int((rcv[11:15]), 16)
                temp_prev2 = temp_prev
                temp_prev = tmp
                if tmp == 0:
                    if temp_prev == 0 and temp_prev2 == 0:
                        temp = 0
                else:
                    temp = tmp
        except Exception, e:
            print (e)
        break
		
    while True:
        try:
            rcv = ''
            port.write(read_telaire)
            rcv = port.readline()
            if len(rcv) >= 11:
                if not (calculateLRC(rcv)):
                    continue
                tmp = int((rcv[7:11]), 16)
                co2_prev2 = co2_prev
                co2_prev = tmp
                if tmp == 0:
                    if co2_prev == 0 and co2_prev2 == 0:
                        co2 = 0
                else:
                    co2 = tmp
        except Exception, e:
            print (e)
        break
		
    while True:
        try:
            rcv = ''
            port.write(read_adau)
            rcv = port.readline()
            if len(rcv) >= 11:
                if not (calculateLRC(rcv)):
                    continue
                tmp = int((rcv[7:11]), 16)
                noise_prev2 = noise_prev
                noise_prev = tmp
                if tmp == 0:
                    if noise_prev == 0 and noise_prev2 == 0:
                        noise = 0
                else:
                    noise = tmp
        except Exception, e:
            print (e)
        break

    port.flushInput()

response = bytearray(b'\x00' * 261)
data = bytearray(b'\x00' * 252)
broken_pings = 0
serversocket = 0
sensordata = ''
config = ConfigParser.ConfigParser()
config.read("config.ini")
error_logfile = config_section_map("LogFiles")['error_log']
light = 0
light_prev = 0
light_prev2 = 0
mtn_cnt = 0
mtn_perc =0
hum = 0
hum_prev = 0
hum_prev2 = 0
temp = 0
temp_prev = 0
temp_prev2 = 0
co2 = 0
co2_prev = 0
co2_prev2 = 0
noise = 0
noise_prev = 0
noise_prev2 = 0

print ("Start...")
file = open(error_logfile, "a")
file.write(time.strftime("Program started on %Y-%m-%d %H:%M:%S.\r\n", time.localtime()))
file.close()
port = serial.Serial("/dev/ttyAMA0", baudrate=19200, timeout=0.1)

while 1:
    read_serial()
    sensordata = str(mtn_cnt) + ',' + str(mtn_perc) + ',' + str(co2) + ',' + str(light) + ',' + str(temp) + ',' + str(hum) + ',' + str(noise) + ',0,\r\n'

    #print("Motion count  : " + str(mtn_cnt) + " pcs")
    #print("Motion percent: " + str(mtn_perc) + " %")
    #print("CO2 gas level : " + str(co2) + " ppm")
    #print("Light level   : " + str(light/10) + " lux")
    #print("Temperature   : " + str(temp/100) + " C")
    #print("Rel. humidity : " + str(hum/100) + " %")
    #print("Noise level   : " + str(noise/100) + " dB")
    #print("\r\n")

    try:
        if config.getboolean("PingRestart", "enabled"):
            ping_test()
    except Exception, e:
        errorlog(e.message)

    if serversocket == 0:
        serversocket = connect()
        if serversocket == 0:
            print ("Error opening socket for Modbus-TCP, is the network connection OK? Retrying in 1 sec.")
            errorlog("Error opening socket for Modbus-TCP, is the network connection OK? Retrying in 1 sec.")
            time.sleep(1)
        else:
            print (serversocket)
            print ('')
            serversocket.settimeout(1)
    else:
        try:
            serversocket.listen(5)
            (clientsocket, address) = serversocket.accept()
            clientsocket.settimeout(1)
            query = clientsocket.recv(1024)
            if len(query) >= 11:
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
                print(time.strftime("%Y-%m-%d %H:%M:%S:", time.localtime()))
                print("Modbus data requested by " + address[0])
                print("MC: " + str(mtn_cnt) + " pcs, MP: " + str(mtn_perc) + " %, CO2: " + str(co2) + " ppm, AL: " + str(light/10) + " lux, T: " + str(temp/100) + " C, RH: " + str(hum/100) + " %, NL: " + str(noise/100) + " dB\r\n")

                for x in range(0, wordcnt):
                    try:
                        data[2*x:2*x+2] = struct.pack('>H', int(sensordata.split(',')[x]))
                    except ValueError:
                        continue
                    except IndexError:
                        continue
                    except struct.error, e:
                        errorlog(e.message)
                        continue
                for x in range(0, wordcnt*2):
                    response[x+9] = data[x+data_offset*2]
            else:
                errorlog("Query length problem, sending old data...")

            #print (time.strftime("%H:%M:%S ", time.localtime()))
            #print ("Offset:" + str(data_offset))
            #print ("Wordcnt:" + str(wordcnt) + "\r\n")
            clientsocket.send(response[0:wordcnt*2+9])
            clientsocket.close()
        except socket.timeout:
            continue
        except socket.error, e:
            errorlog(e.message)
            serversocket = 0
        except IndexError, e:
            clientsocket.close()
            errorlog(e.message)
            continue
        except Exception, e:
            clientsocket.close()
            errorlog(e.message)
            continue
