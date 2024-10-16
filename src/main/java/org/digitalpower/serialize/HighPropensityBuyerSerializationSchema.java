package org.digitalpower.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.digitalpower.models.HighPropensityBuyer;

public class HighPropensityBuyerSerializationSchema implements SerializationSchema<HighPropensityBuyer> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void open(InitializationContext context) {
        // Initialization if needed
    }

    @Override
    public byte[] serialize(HighPropensityBuyer element) {
        try {
            return objectMapper.writeValueAsBytes(element);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing HighPropensityBuyer", e);
        }
    }
}