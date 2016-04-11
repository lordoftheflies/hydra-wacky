#!/usr/bin/python
import time
import usb.core
import usb.util
import serial

def read_serial():
    global port
    rcv = "00000"
    try:
        rcv = port.readline()
    except Exception:
        port.close()
        time.sleep(0.1)
        port = serial.Serial("/dev/ttyACM0", baudrate=115200)
        port.flushInput()
    return rcv

def read_avago():
    global light, light_prev, light_prev2, mtn_cnt, mtn_perc, dev_avago
    try:
        ret = dev_avago.ctrl_transfer(0xC0, 3, 0, 0, 6)
        tmp = (256*ret[0])+ret[1]
        light_prev2 = light_prev
        light_prev = tmp
        if tmp == 0:
            if light_prev == 0 and light_prev2 == 0:
                light = 0
        else:
            light = tmp
        mtn_cnt = (256*ret[2])+ret[3]
        mtn_perc = ret[5]
        return 1
    except Exception:
        time.sleep(0.1)
        return 0

def read_sht25():
    global temp, temp_prev, temp_prev2, hum, hum_prev, hum_prev2, dev_sht25
    try:
        ret = dev_sht25.ctrl_transfer(0xC0, 3, 0, 0, 4)
        tmp = (256*ret[0])+ret[1]
        hum_prev2 = hum_prev
        hum_prev = tmp
        if tmp == 0:
            if hum_prev == 0 and hum_prev2 == 0:
                hum = 0
        else:
            hum = tmp
        tmp = (256*ret[2])+ret[3]
        temp_prev2 = temp_prev
        temp_prev = tmp
        if tmp == 0:
            if temp_prev == 0 and temp_prev2 == 0:
                temp = 0
        else:
            temp = tmp
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
        return 1
    except Exception:
        time.sleep(0.1)
        return 0

def read_adau():
    global noise, noise_prev, noise_prev2, dev_adau
    try:
        ret = dev_adau.ctrl_transfer(0xC0, 3, 0, 0, 2, 100)
        tmp = (256*ret[0])+ret[1]
        noise_prev2 = noise_prev
        noise_prev = tmp
        if tmp == 0:
            if noise_prev == 0 and noise_prev2 == 0:
                noise = 0
        else:
            noise = tmp
        return 1
    except Exception, e:
        time.sleep(0.1)
        return 0

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
usb_success = 0
try_ctr = 0
port = serial.Serial("/dev/ttyACM0", baudrate=115200)

print ("Start...")

dev_avago = usb.core.find(idVendor=0x16c0, idProduct=0x03ee)
dev_sht25 = usb.core.find(idVendor=0x16c0, idProduct=0x03ef)
dev_telaire = usb.core.find(idVendor=0x16c0, idProduct=0x03f0)
dev_adau = usb.core.find(idVendor=0x16c0, idProduct=0x03f1)

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
print ''

while 1:
    rcv = read_serial()
    if rcv[:5] == '&data':

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

        port.write(str(mtn_cnt) + ',' + str(mtn_perc) + ',' + str(co2) + ',' + str(light) + ',' + str(temp) + ',' + str(hum) + ',' + str(noise) + ',0,\r\n') #port.write

        print(time.strftime("%Y-%m-%d %H:%M:%S:", time.localtime()))
        print("Data request.")
        print("MC: " + str(mtn_cnt) + " pcs, MP: " + str(mtn_perc) + " %, CO2: " + str(co2) + " ppm, AL: " + str(light/10) + " lux, T: " + str(temp/100) + " C, RH: " + str(hum/100) + " %, NL: " + str(noise/100) + " dB\r\n")
		
        #print("Motion count  : " + str(mtn_cnt) + " pcs")
        #print("Motion percent: " + str(mtn_perc) + " %")
        #print("CO2 gas level : " + str(co2) + " ppm")
        #print("Light level   : " + str(light/10) + " lux")
        #print("Temperature   : " + str(temp/100) + " C")
        #print("Rel. humidity : " + str(hum/100) + " %")
        #print("Noise level   : " + str(noise/100) + " dB")
        #print("\r\n")
