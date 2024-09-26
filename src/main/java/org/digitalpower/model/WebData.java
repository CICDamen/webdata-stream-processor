package org.digitalpower.model;

import java.util.ArrayList;

public class WebData {

    public String userId;
    public String sessionId;
    public long timestamp;
    public int sessionDuration;
    public ArrayList<PageView> pageViews;
    public CartActivity cartActivity;

    // Constructors
    public WebData() {
    }

    public WebData(String userId, String sessionId, long timestamp, int sessionDuration, ArrayList<PageView> pageViews, CartActivity cartActivity) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.timestamp = timestamp;
        this.sessionDuration = sessionDuration;
        this.pageViews = pageViews;
        this.cartActivity = cartActivity;
    }

    @Override
    public String toString() {
        return "WebData{" +
                ", userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", timestamp=" + timestamp +
                ", sessionDuration=" + sessionDuration +
                ", pageViews=" + pageViews +
                ", cartActivity=" + cartActivity +
                '}';
    }

    public static class PageView {
        public String pageUrl;
        public long timestamp;

        public PageView() {
        }

        public PageView(String pageUrl, long timestamp) {
            this.pageUrl = pageUrl;
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
        public ArrayList<ItemAdded> itemsAdded;

        public CartActivity() {
        }

        public CartActivity(ArrayList<ItemAdded> itemsAdded) {
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
        public String productId;
        public int quantity;
        public long timestamp;

        public ItemAdded() {
        }

        public ItemAdded(String productId, int quantity, long timestamp) {
            this.productId = productId;
            this.quantity = quantity;
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