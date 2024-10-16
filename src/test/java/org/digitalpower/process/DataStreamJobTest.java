package org.digitalpower.process;

import org.digitalpower.producer.KafkaWebDataSender;

import org.digitalpower.producer.WebDataProducer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Properties;

public class DataStreamJobTest {

    private static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.4"));

    @BeforeAll
    public static void setup() {
        kafka.start();
    }

    @AfterAll
    public static void teardown() {
        kafka.stop();
    }

    @Test
    public void testDataStreamJob() {
        // Generate stream of webdata
        KafkaWebDataSender sender = new KafkaWebDataSender();
        sender.pushEventsToKafka(12345L, 5, 50);

        // Process stream of webdata
//        DataStreamJob.main(new String[] {kafka.getBootstrapServers()});

    }

}
