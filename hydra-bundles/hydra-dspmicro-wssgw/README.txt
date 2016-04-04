

Basic Machine Adapter Sample - Implements IMachineAdapter interface and shows how to develop a
	basic machine adapter

At start up the adapter creates data nodes that can be used to read/write data. The data for each
data node is provided from a random data generator, which runs on a separate thread. Data frequency
can be controlled using the update interval property. The latest data values are cached in memory
so that multiple clients can read same values.

To change the machine adapter settings/properties, such as adapter info, number of nodes, and update interval:
	1) Open the com.ge.dspmicro.sample.basicmachineadapter.cfg configuration file located in 
		the <Predix-Machine>/configuration/machine/ folder
		
		There are 4 fields to modify: 
			# Data Update Interval in Milliseconds
			com.ge.dspmicro.sample.basicmachineadapter.UpdateInterval=1000

			# Number of Nodes
			com.ge.dspmicro.sample.basicmachineadapter.NumberOfNodes=3

			# Human Readable Adapter Name and Description Information
			com.ge.dspmicro.sample.basicmachineadapter.Name=Basic Adapter Sample
			com.ge.dspmicro.sample.basicmachineadapter.Description=This adapter generates random data
