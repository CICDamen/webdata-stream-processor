package org.digitalpower.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Properties;

public class KafkaWebDataSenderTest {

    private static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));

    @BeforeAll
    public static void startKafka() {
        kafka.start();
        createTopic();
    }

    @AfterAll
    public static void stopKafka() {
        kafka.stop();
    }

    private static void createTopic() {
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());

        try (AdminClient adminClient = AdminClient.create(config)) {
            NewTopic newTopic = new NewTopic("webdata", 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(newTopic)).all().get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create topic", e);
        }
    }

    @Test
    public void testSendToKafka() {
        String bootstrapServers = kafka.getBootstrapServers();
        String[] args = {
                "--kafka.bootstrap.servers", bootstrapServers,
                "--kafka.topic", "webdata",
                "--number-of-users", "1",
                "--number-of-events", "5"
        };
        KafkaWebDataSender.main(args);
    }

}
