package com.speant.delivery.Models;

public class TimeoutSuccessPojo {
    private boolean status;
    private String provider_timeout;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProvider_timeout() {
        return provider_timeout;
    }

    public void setProvider_timeout(String provider_timeout) {
        this.provider_timeout = provider_timeout;
    }
}
