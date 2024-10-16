package org.digitalpower.producer;

import org.apache.commons.cli.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.digitalpower.models.WebData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaWebDataSender {

    private static String KAFKA_BOOTSTRAP_SERVERS;
    private static String KAFKA_TOPIC;
    private static final Logger logger = LoggerFactory.getLogger(KafkaWebDataSender.class);
    private final KafkaProducer<String, WebData> producer;

    // Main entry point to generate and send events to Kafka
    public static void main(String[] args) {

        // Parameters for event generation
        int numberOfUsers;
        int numberOfEvents;

        // Parse command line arguments
        Options options = new Options();
        options.addOption("b", "kafka.bootstrap.servers", true, "Kafka bootstrap servers");
        options.addOption("t", "kafka.topic", true, "Kafka topic");
        options.addOption("u", "number-of-users", true, "Number of users");
        options.addOption("e", "number-of-events", true, "Number of events");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            KAFKA_BOOTSTRAP_SERVERS = cmd.getOptionValue("b", "localhost:29092");
            KAFKA_TOPIC = cmd.getOptionValue("t", "webdata");
            numberOfUsers = Integer.parseInt(cmd.getOptionValue("u", "5"));
            numberOfEvents = Integer.parseInt(cmd.getOptionValue("e", "50"));
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments", e);
            throw new RuntimeException("Error parsing command line arguments", e);
        }

        // Create a new instance of the KafkaWebDataSender class
        KafkaWebDataSender kafkaWebDataSender = new KafkaWebDataSender();

        // Push events to Kafka
        kafkaWebDataSender.pushEventsToKafka(12345L, numberOfUsers, numberOfEvents);
    }

    // Constructor to initialize KafkaProducer
    public KafkaWebDataSender() {
        this.producer = KafkaProducerConfig.createWebDataProducer(KAFKA_BOOTSTRAP_SERVERS);
    }


    // Method to push multiple events to Kafka
    public void pushEventsToKafka(long seed, int numberOfUsers, int numberOfEvents) {
        WebDataProducer webDataProducer = new WebDataProducer(seed, numberOfUsers);
        for (int i = 0; i < numberOfEvents; i++) {
            WebData webData = webDataProducer.generateWebData();
            send(webData);
        }
    }

    // Method to send WebData to Kafka
    public void send(WebData webData) {
        logger.info("Sending WebData event to Kafka: {}", webData);
        producer.send(new ProducerRecord<>(KAFKA_TOPIC, webData.getUserId(), webData), (metadata, exception) -> {
            if (exception != null) {
                logger.error("Error sending WebData to Kafka", exception);
                throw new RuntimeException("Error sending WebData to Kafka", exception);
            } else {
                logger.info("Successfully sent WebData to Kafka: {}", metadata);
            }
        });
        producer.flush();
    }

}