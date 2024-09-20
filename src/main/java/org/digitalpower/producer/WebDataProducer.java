package org.digitalpower.producer;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.digitalpower.model.WebData;

public class WebDataProducer {

    public static void main(String[] args) {
        // Create a new instance of the WebDataProducer class
        WebDataProducer webDataProducer = new WebDataProducer();

        // Generate a new WebData object
        WebData webData = webDataProducer.generateWebData();
        System.out.println(webData.toString());

        // Send the WebData object to Kafka
        webDataProducer.sendToKafka(webData);
    }

    private final Faker faker;
    private final KafkaProducer<String, WebData> producer;
    private static final String TOPIC = "web-data";

    public WebDataProducer() {
        this.faker = new Faker();
        this.producer = KafkaProducerConfig.createWebDataProducer();
    }

    public WebData generateWebData() {
        WebData webData = new WebData();
        webData.setUserId(faker.idNumber().valid());
        webData.setClicks(faker.number().numberBetween(1, 10));
        webData.setImpressions(faker.number().numberBetween(1, 10));
        webData.setConversions(faker.number().numberBetween(1, 10));
        webData.setAddToCarts(faker.number().numberBetween(1, 10));
        webData.setRevenue(faker.number().numberBetween(1, 100));
        return webData;
    }

    public void sendToKafka(WebData webData) {
        producer.send(new ProducerRecord<>(TOPIC, webData.getUserId(), webData));
        producer.flush();
    }

}


