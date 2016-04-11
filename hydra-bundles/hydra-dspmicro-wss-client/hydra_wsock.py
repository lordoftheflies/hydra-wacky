#!/usr/bin/python
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
        ret = dev_adau.ctrl_transfer(0xC0, 3, 0, 0, 2, 100)
        noise = (256*ret[0])+ret[1]
        #print "Noise level   : " + str(noise) + "dB"
        return 1
    except Exception, e:
        #print e
        time.sleep(0.1)
        return 0

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

                if adau_en:
                    usb_success = 0
                    try_ctr = 0
                    while not usb_success:
                        try_ctr += 1
                        if try_ctr == 4:
                            dev_adau = usb.core.find(idVendor=0x16c0, idProduct=0x03f1)
                        usb_success = read_adau()
                        if try_ctr == 7:
                            adau_en = 0
                            usb_success = 1
                            noise = 0

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
            except Exception, e:
               print e.message
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

print ("Start")

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
