package com.ge.lighting.hydra.m2c.data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataPoint implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String key;
	private Date ts;
	private Double value;
	private UUID box;
	
	public DataPoint() {
		super();
	}
	
	public DataPoint(String key, Date ts, Double value, UUID box) {
		super();
		this.key = key;
		this.ts = ts;
		this.value = value;
		this.box = box;
	}



	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Date getTs() {
		return ts;
	}
	
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}

	public UUID getBox() {
		return box;
	}

	public void setBox(UUID box) {
		this.box = box;
	}

	@Override
	public String toString() {
		return "[" + key + "|" + ts + "|" + box + "]"
				+ " = " + value;
	}
	
	
}
