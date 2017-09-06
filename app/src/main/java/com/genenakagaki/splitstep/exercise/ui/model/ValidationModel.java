package com.genenakagaki.splitstep.exercise.ui.model;

/**
 * Created by Gene on 9/6/2017.
 */

public class ValidationModel {

    private boolean isValid;
    private String errorMessage;

    public ValidationModel() {}

    public ValidationModel(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }

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
