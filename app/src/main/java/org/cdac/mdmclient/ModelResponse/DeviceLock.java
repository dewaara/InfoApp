package org.cdac.mdmclient.ModelResponse;

public class DeviceLock {
  //  private String id;
   // private String deviceId;
 //   private String deviceStatus;
      boolean isDeviceLock;
 //   private int timestamp;
  //  private String createdDate;
  //  private int v;
    String error;
    String message;


    public DeviceLock() {
    }

    public DeviceLock(boolean isDeviceLock, String error, String message) {
        this.isDeviceLock = isDeviceLock;
        this.error = error;
        this.message = message;
    }

    public boolean isDeviceLock() {
        return isDeviceLock;
    }

    public void setDeviceLock(boolean deviceLock) {
        isDeviceLock = deviceLock;
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
}
