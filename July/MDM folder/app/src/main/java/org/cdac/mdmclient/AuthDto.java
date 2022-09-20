package com.example.cdac.expsocketapp.dtos;

public class AuthDto {

    private String tname,timei;

    public AuthDto() {
    }

    public AuthDto(String tname) {
        this.tname = tname;
    }

    public AuthDto(String tname,String timei) {
        this.tname = tname;
        this.timei=timei;
    }


    public String getTname() {
        return tname;
    }

    public String getImei() {
        return timei;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void setTname(String tname, String timei) {
        this.tname = tname;
        this.timei=timei;


    }

}