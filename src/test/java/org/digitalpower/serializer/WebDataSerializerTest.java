package org.digitalpower.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.digitalpower.common.WebData;
import org.digitalpower.common.WebData.CartActivity;
import org.digitalpower.common.WebData.ItemAdded;
import org.digitalpower.common.WebData.PageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WebDataSerializerTest {

    private Serializer<WebData> serializer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        serializer = new WebDataSerializer();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialize() throws Exception {
        ArrayList<PageView> pageViews = new ArrayList<>();
        pageViews.add(new PageView("http://example.com", 1627849923000L));

        ArrayList<ItemAdded> itemsAdded = new ArrayList<>();
        itemsAdded.add(new ItemAdded("product123", 2, 1627849923000L));

        CartActivity cartActivity = new CartActivity(itemsAdded);

        WebData webData = new WebData("user123", "session456", 1627849923000L, 3600, pageViews, cartActivity);

        byte[] serializedData = serializer.serialize("test-topic", webData);
        byte[] expectedData = objectMapper.writeValueAsBytes(webData);

        assertNotNull(serializedData);
        assertArrayEquals(expectedData, serializedData);
    }
}