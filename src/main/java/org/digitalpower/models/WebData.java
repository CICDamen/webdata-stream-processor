package org.digitalpower.models;

import java.util.List;

public class WebData {

    private String userId;
    private String sessionId;
    private long timestamp;
    private int sessionDurationSeconds;
    private List<PageView> pageViews;
    private CartActivity cartActivity;

    // Constructors
    public WebData() {
    }

    public WebData(String userId, String sessionId, long timestamp, int sessionDurationSeconds, List<PageView> pageViews, CartActivity cartActivity) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.sessionDurationSeconds = sessionDurationSeconds;
        this.pageViews = pageViews;
        this.cartActivity = cartActivity;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getSessionDurationSeconds() {
        return sessionDurationSeconds;
    }

    public void setSessionDurationSeconds(int sessionDurationSeconds) {
        this.sessionDurationSeconds = sessionDurationSeconds;
    }

    public List<PageView> getPageViews() {
        return pageViews;
    }

    public void setPageViews(List<PageView> pageViews) {
        this.pageViews = pageViews;
    }

    public CartActivity getCartActivity() {
        return cartActivity;
    }

    public void setCartActivity(CartActivity cartActivity) {
        this.cartActivity = cartActivity;
    }

    @Override
    public String toString() {
        return "WebData{" +
                "userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", timestamp=" + timestamp +
                ", sessionDurationSeconds=" + sessionDurationSeconds +
                ", pageViews=" + pageViews +
                ", cartActivity=" + cartActivity +
                '}';
    }

    public static class PageView {
        private String pageUrl;
        private long timestamp;

        public PageView() {
        }

        public PageView(String pageUrl, long timestamp) {
            this.pageUrl = pageUrl;
            this.timestamp = timestamp;
        }

        // Getters and Setters
        public String getPageUrl() {
            return pageUrl;
        }

        public void setPageUrl(String pageUrl) {
            this.pageUrl = pageUrl;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "PageView{" +
                    "pageUrl='" + pageUrl + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }

    public static class CartActivity {
        private List<ItemAdded> itemsAdded;

        public CartActivity() {
        }

        public CartActivity(List<ItemAdded> itemsAdded) {
            this.itemsAdded = itemsAdded;
        }

        // Getters and Setters
        public List<ItemAdded> getItemsAdded() {
            return itemsAdded;
        }

        public void setItemsAdded(List<ItemAdded> itemsAdded) {
            this.itemsAdded = itemsAdded;
        }

        @Override
        public String toString() {
            return "CartActivity{" +
                    "itemsAdded=" + itemsAdded +
                    '}';
        }
    }

    public static class ItemAdded {
        private String productId;
        private int quantity;
        private long timestamp;

        public ItemAdded() {
        }

        public ItemAdded(String productId, int quantity, long timestamp) {
            this.productId = productId;
            this.quantity = quantity;
            this.timestamp = timestamp;
        }

        // Getters and Setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "ItemAdded{" +
                    "productId='" + productId + '\'' +
                    ", quantity=" + quantity +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
}
