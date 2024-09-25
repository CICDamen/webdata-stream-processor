package org.digitalpower.producer;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.digitalpower.model.User;

public class UserDataProducer {

    public static void main(String[] args) {
        // Create a new instance of the UserDataProducer class
        UserDataProducer userDataProducer = new UserDataProducer();

        // Generate a new User object
        User user = userDataProducer.generateUser();
        System.out.println(user.toString());

        // Send the User object to Kafka
        userDataProducer.sendToKafka(user);
    }

    private final Faker faker;
    private final KafkaProducer<String, User> producer;
    private static final String TOPIC = "users";

    public UserDataProducer() {
        this.faker = new Faker();
        this.producer = KafkaProducerConfig.createUserProducer();
    }

    public User generateUser() {
        User user = new User();
        user.userId = faker.idNumber().valid();
        user.userName = faker.name().username();
        user.email = faker.internet().emailAddress();
        user.city = faker.address().city();
        user.state = faker.address().state();
        user.country = faker.address().country();
        user.age = faker.number().numberBetween(18, 65);
        return user;
    }

    public void sendToKafka(User user) {
        producer.send(new ProducerRecord<>(TOPIC, user.userId, user));
        producer.flush();
    }
}