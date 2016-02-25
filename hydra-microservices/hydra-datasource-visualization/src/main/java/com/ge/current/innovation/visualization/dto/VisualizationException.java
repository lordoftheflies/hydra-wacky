/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.current.innovation.visualization.dto;

/**
 *
 * @author predix
 */
public class VisualizationException extends Exception {

    public VisualizationException() {
    }

    public VisualizationException(String message) {
        super(message);
    }

    public VisualizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisualizationException(Throwable cause) {
        super(cause);
    }

    public VisualizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
