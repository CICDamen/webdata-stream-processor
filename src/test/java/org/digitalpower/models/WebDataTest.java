package org.digitalpower.models;

import org.digitalpower.models.WebData.CartActivity;
import org.digitalpower.models.WebData.ItemAdded;
import org.digitalpower.models.WebData.PageView;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebDataTest {

    @Test
    public void testWebDataCreation() {
        ArrayList<PageView> pageViews = new ArrayList<>();
        pageViews.add(new PageView("http://example.com", 1627849923000L));

        ArrayList<ItemAdded> itemsAdded = new ArrayList<>();
        itemsAdded.add(new ItemAdded("product123", 2, 1627849923000L));

        CartActivity cartActivity = new CartActivity(itemsAdded);

        WebData webData = new WebData("user123", "session456", 1627849923000L, 3600, pageViews, cartActivity);

        assertNotNull(webData);
        assertEquals("user123", webData.getUserId());
        assertEquals("session456", webData.getSessionId());
        assertEquals(1627849923000L, webData.getTimestamp());
        assertEquals(3600, webData.getSessionDurationSeconds());
        assertEquals(pageViews, webData.getPageViews());
        assertEquals(cartActivity, webData.getCartActivity());
    }
}