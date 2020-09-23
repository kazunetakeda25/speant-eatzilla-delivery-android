package com.speant.delivery.Models;

import java.util.List;

public class WeeklyEarningsResponse {
    private boolean status;
    private String weekly_earnings;
    private String weekly_incentives;
    private String monthly_earnings;
    private String monthly_incentives;
    private List<GraphData> graph_data;

    public String getMonthly_earnings() {
        return monthly_earnings;
    }

    public void setMonthly_earnings(String monthly_earnings) {
        this.monthly_earnings = monthly_earnings;
    }

    public String getMonthly_incentives() {
        return monthly_incentives;
    }

    public void setMonthly_incentives(String monthly_incentives) {
        this.monthly_incentives = monthly_incentives;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getWeekly_earnings() {
        return weekly_earnings;
    }

    public void setWeekly_earnings(String weekly_earnings) {
        this.weekly_earnings = weekly_earnings;
    }

    public String getWeekly_incentives() {
        return weekly_incentives;
    }

    public void setWeekly_incentives(String weekly_incentives) {
        this.weekly_incentives = weekly_incentives;
    }

    public List<GraphData> getGraph_data() {
        return graph_data;
    }

    public void setGraph_data(List<GraphData> graph_data) {
        this.graph_data = graph_data;
    }
}
