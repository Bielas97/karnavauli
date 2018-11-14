package com.karnavauli.app.exceptions;

public enum ExceptionCode {
    REPOSITORY ("EXCEPTION IN REPOSITORY"),
    SERVICE ("EXCEPTION IN SERVICE"),
    DATABASE ("EXCEPTION IN DATABASE"),
    VALIDATION ("VALIDATION EXCEPTION"),
    MAX_PLACES("THERE IS NO MORE FREE PLACES FOR THAT TABLE");

    private String description;

    ExceptionCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
