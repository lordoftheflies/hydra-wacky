

Web Socket Server Sample - Shows how to use the wsserver service connect to a 
client websocket endpoint. This simple implementation shows how to use 
annotated POJOs to interact with the WebSocket life cycle events.  The example
shows the capture and return transfer of a text string and byte buffer. 

Once the container starts with the sample-configuration bundle, 
	1- Navigate to		http://localhost:8181/system/console/configMgr
	
	2- Select the "Predix Machine Websocket Server".
	
	3- Edit the "WebSocket Server Port" configuration to be "Custom".
	
	4- Edit the "WebSocket Secure Server Port" configuration to be "Custom".
	
	5- Edit the "WS custom Port" configuration to be "5447".
	
Save the configuration to complete the setup.