package org.cdac.mdmclient.ModelResponse;

public class RegisterResponse {
    String error;
    String message;
    private User user;

    public RegisterResponse() {
    }

    public RegisterResponse(String error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
