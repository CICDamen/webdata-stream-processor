package org.digitalpower.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.digitalpower.models.WebData;
import org.digitalpower.serialize.WebDataSerializer;

import java.util.Properties;

public class KafkaProducerConfig {


    public static KafkaProducer<String, WebData> createWebDataProducer(String host) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, WebDataSerializer.class.getName());
        return new KafkaProducer<>(props);
    }


}
