package com.ge.current.innovation.infrastructure.service;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * An example of creating a Rest api using Spring Annotations @RestController.  
 * 
 *   
 * 
 * @author predix
 */
@RestController
public class HelloController {

	
    /**
     * 
     */
    public HelloController() {
		super();
	}

	/**
	 * Sample Endpoint which returns a Welcome Message
	 * @param echo - the string to echo back
	 * @return -
	 */
	@SuppressWarnings("nls")
    @RequestMapping("/")
    public String index(@RequestParam(value="echo",defaultValue="echo") String echo) {
        return "Greetings from Predix Spring Boot! " + (new Date());
    }

}