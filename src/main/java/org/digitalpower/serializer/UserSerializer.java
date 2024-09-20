package org.digitalpower.serializer;

import org.apache.kafka.common.serialization.Serializer;
import org.digitalpower.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;


public class UserSerializer implements Serializer<User> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No configuration needed
    }

    @Override
    public byte[] serialize(String topic, User data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing User", e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}
