package org.digitalpower.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.digitalpower.model.User;
import org.digitalpower.model.WebData;
import org.digitalpower.serializer.UserSerializer;
import org.digitalpower.serializer.WebDataSerializer;

import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer<String, User> createUserProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    public static KafkaProducer<String, WebData> createWebDataProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, WebDataSerializer.class.getName());
        return new KafkaProducer<>(props);
    }


}
