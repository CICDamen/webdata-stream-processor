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
        user.setUserId(faker.idNumber().valid());
        user.setUserName(faker.name().username());
        user.setEmail(faker.internet().emailAddress());
        user.setCity(faker.address().city());
        user.setState(faker.address().state());
        user.setCountry(faker.address().country());
        user.setAge(faker.number().numberBetween(18, 65));
        return user;
    }

    public void sendToKafka(User user) {
        producer.send(new ProducerRecord<>(TOPIC, user.getUserId(), user));
        producer.flush();
    }


}