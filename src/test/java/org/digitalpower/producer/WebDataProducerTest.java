package org.digitalpower.producer;

import org.digitalpower.models.WebData;
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
        assertNotNull(webData.getSessionId());
        assertTrue(webData.getTimestamp() > 0);
        assertTrue(webData.getSessionDurationSeconds() > 0);
        assertNotNull(webData.getPageViews());
        assertNotNull(webData.getCartActivity());
        assertNotNull(webData.getCartActivity().getItemsAdded());
    }

}