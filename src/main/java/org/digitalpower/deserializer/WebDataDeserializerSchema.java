package org.digitalpower.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;
import org.digitalpower.model.WebData;

import java.io.IOException;

public class WebDataDeserializerSchema extends AbstractDeserializationSchema<WebData> {

    private static final long serialVersionUID = 1L;
    private transient ObjectMapper objectMapper;

    @Override
    public void open(InitializationContext context) {
        objectMapper = JsonMapper.builder().build();
    }

    @Override
    public WebData deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, WebData.class);
    }
}