package com.ge.lighting.hydra.m2c.asset;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jean Lau
 * 
 */

@XmlRootElement
public class EventDto implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EventDto()
    {
    }

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}