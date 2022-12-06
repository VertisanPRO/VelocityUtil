package com.gigabait.config.squishyyaml;


public enum YamlConfigurationExceptionType {
    WRONG_TYPE("This path contains the wrong type: <var>");

    private final String message;


    YamlConfigurationExceptionType(String message) {
        this.message = message;
    }


    public String getMessage() {
        return this.message;
    }


    public String getMessage(String variable) {
        return this.message.replace("<var>", variable);
    }
}