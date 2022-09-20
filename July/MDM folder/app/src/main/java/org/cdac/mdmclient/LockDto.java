package com.example.cdac.expsocketapp.dtos;

public class LockDto {

    private String name;
    private String status;

    public LockDto() {
    }

    public LockDto(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}