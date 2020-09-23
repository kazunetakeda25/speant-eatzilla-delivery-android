package com.speant.delivery.Models;

public class DailyEarningsResponse {

    private boolean status;
    private String today_earnings;
    private String today_incentives;
    private String today_orders;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToday_earnings() {
        return today_earnings;
    }

    public void setToday_earnings(String today_earnings) {
        this.today_earnings = today_earnings;
    }

    public String getToday_incentives() {
        return today_incentives;
    }

    public void setToday_incentives(String today_incentives) {
        this.today_incentives = today_incentives;
    }

    public String getToday_orders() {
        return today_orders;
    }

    public void setToday_orders(String today_orders) {
        this.today_orders = today_orders;
    }
}
