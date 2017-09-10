package com.genenakagaki.splitstep.exercise.ui.model;

/**
 * Created by gene on 9/9/17.
 */

public class ErrorMessage {

    private boolean isValid;
    private String errorMessage;

    public ErrorMessage() {}

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
