package com.WIFI.keyshare;

public class RedWiFi {
    private String ssid;
    private String psk;

    public RedWiFi(String ssid, String psk) {
        this.ssid = ssid;
        this.psk = psk;
    }

    public String getSsid() {
        return ssid;
    }

    public String getPsk() {
        return psk;
    }
}
