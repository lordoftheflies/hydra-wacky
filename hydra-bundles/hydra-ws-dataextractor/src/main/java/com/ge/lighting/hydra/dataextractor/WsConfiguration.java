/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.dataextractor;

import aQute.bnd.annotation.metatype.Meta;
import aQute.bnd.annotation.metatype.Meta.Type;

/**
 *
 * @author Hegedűs László (212429780)
 */
@Meta.OCD(
    name = "%ws.name", 
    factory = true, 
    localization = "bundle", 
    description = "%ws.description")
public interface WsConfiguration {

	static final String DEFAULT_IP = "3.117.211.148";
	static final String DEFAULT_PORT = "5672";
	static final String DEFAULT_USER = "hydra";
	static final String DEFAULT_PASSWD = "ombre2383";
    static final String SERVICE_PID = "com.ge.lighting.hydra.machine.m2c";

    @Meta.AD(name = "%rabbitmqhost.name",
            description = "%rabbitmqhost.description",
            id = SERVICE_PID + ".rabbitmq.host",
            required = false,
            deflt = DEFAULT_IP)
    String rabbitMqHost();

    @Meta.AD(name = "%rabbitmqport.name",
            description = "%rabbitmqport.description",
            id = SERVICE_PID + ".rabbitmq.port",
            required = false,
            type = Type.Integer,
            deflt = DEFAULT_PORT)
    int rabbitMqPort();

    @Meta.AD(name = "%rabbitmqusername.name",
            description = "%rabbitmqusername.description",
            id = SERVICE_PID + ".rabbitmq.username",
            required = false,
            deflt = DEFAULT_USER)
    String username();

    @Meta.AD(name = "%rabbitmqpassword.name",
            description = "%rabbitmqpassword.description",
            id = SERVICE_PID + ".rabbitmq.password",
            required = false,
            deflt = DEFAULT_PASSWD)
    String password();

    @Meta.AD(name = "%dataextractionround.name", description = "%dataextractionround.description", id = SERVICE_PID
            + ".dataextraction.round", required = false, type = Type.Long, deflt = "1000")
    long round();

    @Meta.AD(name = "%dataextractionid.name", description = "%dataextractionid.description", id = SERVICE_PID
            + ".dataextraction.id", required = false, deflt = "fd2ee75e-9fd5-4509-85fe-8b717b96189e")
    String boxId();

    @Meta.AD(name = "%dataextractionsource.name", description = "%dataextractionsource.description", id = SERVICE_PID
            + ".dataextraction.source", required = false, deflt = "/opt/hydra/var/sensor_log.csv")
    String source();

    @Meta.AD(name = "%dataextractiondatepattern.name", description = "%dataextractiondatepattern.description", id = SERVICE_PID
            + ".dataextraction.datepattern", required = false, deflt = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    String datePattern();
}
