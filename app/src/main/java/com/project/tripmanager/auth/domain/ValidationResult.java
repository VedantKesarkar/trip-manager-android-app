package com.project.tripmanager.auth.domain;

public class ValidationResult {
    private boolean success = false;
    private String emailError = "";
    private String passwordError = "";
    private String confirmPassError = "";

    public ValidationResult() {}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getConfirmPassError() {
        return confirmPassError;
    }

    public void setConfirmPassError(String confirmPassError) {
        this.confirmPassError = confirmPassError;
    }
}
