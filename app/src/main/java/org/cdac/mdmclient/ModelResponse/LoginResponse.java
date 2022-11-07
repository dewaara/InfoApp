package org.cdac.mdmclient.ModelResponse;

public class LoginResponse {
    private boolean error;
    private String status;
    private String message;
    private String role;
    private String token;
    private User user;

    public LoginResponse() {
    }

    public LoginResponse(boolean error, String status, String message, String role, String token, User user) {
        this.error = error;
        this.status = status;
        this.message = message;
        this.role = role;
        this.token = token;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
