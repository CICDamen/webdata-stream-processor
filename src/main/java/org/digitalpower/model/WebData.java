package org.digitalpower.model;

public class WebData {

    private String userId;
    private int clicks;
    private int impressions;
    private int conversions;
    private int addToCarts;
    private int revenue;

    // Constructors
    public WebData() {
    }

    public WebData(String userId, int clicks, int impressions, int conversions, int addToCarts, int revenue) {
        this.userId = userId;
        this.clicks = clicks;
        this.impressions = impressions;
        this.conversions = conversions;
        this.addToCarts = addToCarts;
        this.revenue = revenue;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getImpressions() {
        return impressions;
    }

    public void setImpressions(int impressions) {
        this.impressions = impressions;
    }

    public int getConversions() {
        return conversions;
    }

    public void setConversions(int conversions) {
        this.conversions = conversions;
    }

    public int getAddToCarts() {
        return addToCarts;
    }

    public void setAddToCarts(int addToCarts) {
        this.addToCarts = addToCarts;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "WebData{" + "userId='" + userId + '\'' + ", clicks=" + clicks + ", impressions=" + impressions + ", conversions=" + conversions + ", addToCarts=" + addToCarts + ", revenue=" + revenue + '}';
    }

}
