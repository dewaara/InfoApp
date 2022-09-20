package org.cdac.mdmclient;

public class AppDataDto {
    private String name;
    private String pkg;
    private String version;

    public AppDataDto() {
    }

    public AppDataDto(String name, String pkg, String version) {
        this.name = name;
        this.pkg = pkg;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}