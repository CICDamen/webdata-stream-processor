package org.digitalpower.process;

public class HighPropensityBuyer {

    private String userId;
    private Long visitCount;
    private Long cartCount;
    private Double avgSessionDuration;

    // Constructors
    public HighPropensityBuyer() {
    }

    public HighPropensityBuyer(String userId, Long visitCount, Long cartCount, Double avgSessionDuration) {
        this.userId = userId;
        this.visitCount = visitCount;
        this.cartCount = cartCount;
        this.avgSessionDuration = avgSessionDuration;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getCartCount() {
        return cartCount;
    }

    public void setCartCount(Long cartCount) {
        this.cartCount = cartCount;
    }

    public Double getAvgSessionDuration() {
        return avgSessionDuration;
    }

    public void setAvgSessionDuration(Double avgSessionDuration) {
        this.avgSessionDuration = avgSessionDuration;
    }

    @Override
    public String toString() {
        return "HighPropensityBuyer{" +
                "userId='" + userId + '\'' +
                ", visitCount=" + visitCount +
                ", cartCount=" + cartCount +
                ", avgSessionDuration=" + avgSessionDuration +
                '}';
    }
}