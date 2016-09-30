package com.circuitwall.ml.platform.flink.evolution.exception;

/**
 * Project: evolution
 * Created by andrew on 30/08/16.
 */
public class ConfigurationException extends RuntimeException {
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
