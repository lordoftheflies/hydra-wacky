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


def on_open(ws):
    def run(*args):
        print ("Open data-ingestion stream ...")
        for i in range(1000):
            data_point = {}
            data_point['code'] = initialSpace['sensors'][random.randint(0, len(initialSpace['sensors']) - 1)]
            data_point['value'] = random.random()
            json_message = json.dumps(data_point)

            ws.send(json_message)
            print ("Message (" + str(i) + ") >> WS-SERVER[name=data-ingestion]: " + json_message)

            time.sleep(initialSpace['period'])

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


# if __name__ == "__main__":
def run():
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
