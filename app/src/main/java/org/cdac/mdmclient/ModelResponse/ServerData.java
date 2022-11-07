package org.cdac.mdmclient.ModelResponse;

public class ServerData {
    String deviceId;
    String message;
    String id;

    ServerData(String deviceId, String msg, String id){
        this.deviceId = deviceId;
        this.message = msg;
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
