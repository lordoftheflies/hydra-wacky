/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.data.visualization;

/**
 *
 * @author predix
 */
public class VisualizationInputException extends VisualizationException {

    public VisualizationInputException() {
    }

    public VisualizationInputException(String message) {
        super(message);
    }

    public VisualizationInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisualizationInputException(Throwable cause) {
        super(cause);
    }

    public VisualizationInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
