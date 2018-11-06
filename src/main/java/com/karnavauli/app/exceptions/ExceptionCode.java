package com.karnavauli.app.exceptions;

public enum ExceptionCode {
    REPOSITORY ("EXCEPTION IN REPOSITORY"),
    SERVICE ("EXCEPTION IN SERVICE"),
    DATABASE ("EXCEPTION IN DATABASE"),
    VALIDATION ("VALIDATION EXCEPTION");

    private String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
