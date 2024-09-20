package org.digitalpower.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.digitalpower.model.WebData;

import java.util.Map;

public class WebDataSerializer implements Serializer<WebData> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public byte[] serialize(String topic, WebData data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing WebData", e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}
