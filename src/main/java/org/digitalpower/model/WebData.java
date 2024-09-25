package org.digitalpower.model;

public class WebData {

    public String userId;
    public long timestamp;
    public int clicks;
    public int impressions;
    public int conversions;
    public int addToCarts;
    public int revenue;

    // Constructors
    public WebData() {}

    public WebData(String userId, long timestamp, int clicks, int impressions, int conversions, int addToCarts, int revenue) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.clicks = clicks;
        this.impressions = impressions;
        this.conversions = conversions;
        this.addToCarts = addToCarts;
        this.revenue = revenue;
    }

    @Override
    public String toString() {
        return "WebData{" +
                "userId='" + userId + '\'' +
                ", timestamp=" + timestamp +
                ", clicks=" + clicks +
                ", impressions=" + impressions +
                ", conversions=" + conversions +
                ", addToCarts=" + addToCarts +
                ", revenue=" + revenue +
                '}';
    }
}