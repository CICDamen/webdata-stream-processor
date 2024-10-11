package org.digitalpower.process;

import org.digitalpower.model.WebData;
import org.digitalpower.producer.WebDataProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebDataProducerTest {

    private WebDataProducer webDataProducer;

    @BeforeEach
    public void setUp() {
        long seed = 12345L;
        int numberOfUsers = 5;
        webDataProducer = new WebDataProducer(seed, numberOfUsers);
    }

    @Test
    public void testGenerateWebData() {
        WebData webData = webDataProducer.generateWebData();
        assertNotNull(webData);
        assertNotNull(webData.getUserId());
        assertNotNull(webData.sessionId);
        assertTrue(webData.timestamp > 0);
        assertTrue(webData.sessionDurationSeconds > 0);
        assertNotNull(webData.pageViews);
        assertNotNull(webData.cartActivity);
        assertNotNull(webData.cartActivity.itemsAdded);
    }

}