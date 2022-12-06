package com.gigabait.config.squishyyaml;


public class YamlConfigurationException extends RuntimeException {


    public YamlConfigurationException(YamlConfigurationExceptionType type) {
        super(type.getMessage());
    }

    public YamlConfigurationException(YamlConfigurationExceptionType type, String variable) {
        super(type.getMessage(variable));
    }
}