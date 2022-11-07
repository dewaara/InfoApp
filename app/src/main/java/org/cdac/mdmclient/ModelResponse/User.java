package org.cdac.mdmclient.ModelResponse;

public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String repeatPassword;
    private String deviceId;
    private boolean isActive;
    private String roles;
    private String createdDate;
    private int v;

    public User() {
    }

    public User(String id, String username, String email, String password, String repeatPassword, String deviceId, boolean isActive, String roles, String createdDate, int v) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.deviceId = deviceId;
        this.isActive = isActive;
        this.roles = roles;
        this.createdDate = createdDate;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }
}
