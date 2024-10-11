package org.digitalpower.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.digitalpower.model.WebData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaWebDataSender {

    private static final String WEBDATA_TOPIC = "webdata";
    private static final Logger logger = LoggerFactory.getLogger(KafkaWebDataSender.class);
    private final KafkaProducer<String, WebData> producer;

    // Constructor
    public KafkaWebDataSender() {
        this.producer = KafkaProducerConfig.createWebDataProducer();
    }

    // Main method
    public static void main(String[] args) {

        long seed = 12345L;
        int numberOfUsers = 5;
        int numberOfEvents = 50;

        // Create a new instance of the KafkaWebDataSender class
        KafkaWebDataSender kafkaWebDataSender = new KafkaWebDataSender();

        // Create a new instance of the WebDataProducer class
        WebDataProducer webDataProducer = new WebDataProducer(seed, numberOfUsers);

        // Generate multiple WebData events
        for (int i = 0; i < numberOfEvents; i++) {
            WebData webData = webDataProducer.generateWebData();

            // Send the generated webdata to Kafka
            kafkaWebDataSender.sendToKafka(webData);
        }
    }

    // Method to send WebData to Kafka
    public void sendToKafka(WebData webData) {
        logger.info("Sending WebData event to Kafka: {}", webData);
        producer.send(new ProducerRecord<>(WEBDATA_TOPIC, webData.userId, webData), (metadata, exception) -> {
            if (exception != null) {
                logger.error("Error sending WebData to Kafka", exception);
            } else {
                logger.info("Successfully sent WebData to Kafka: {}", metadata);
            }
        });
        producer.flush();
    }
}