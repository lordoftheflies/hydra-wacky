package com.ge.lighting.hydra.asset.model.entity;

import com.ge.lighting.hydra.asset.EventDto;
import com.ge.lighting.innovation.persistence.ext.DataTransferrableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.ge.lighting.innovation.persistence.ext.AbstractEntity;

@Entity
public class EventEntity extends AbstractEntity<Long> implements DataTransferrableEntity<EventDto> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public EventEntity() {
    }

    public EventEntity(long id, String message, String source) {
        this.id = id;
        this.message = message;
        this.source = source;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "source")
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public EventDto getDto() {
        EventDto alarmEvent = new EventDto();
        alarmEvent.setId(getId());
        alarmEvent.setMessage(getMessage());
        alarmEvent.setSource(getSource());

        return alarmEvent;
    }

}
