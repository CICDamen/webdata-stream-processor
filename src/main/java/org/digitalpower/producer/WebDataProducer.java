package org.digitalpower.producer;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.digitalpower.model.WebData;

import java.util.*;

public class WebDataProducer {

    private static final String WEBDATA_TOPIC = "webdata";
    private final Faker faker;
    private final KafkaProducer<String, WebData> producer;
    private final List<String> pagePaths;
    private final Random random;
    private final Set<String> userIds;

    // Constructor
    public WebDataProducer(long seed, int numberOfUsers) {
        this.faker = new Faker(new Random(seed));
        this.producer = KafkaProducerConfig.createWebDataProducer();
        this.pagePaths = Arrays.asList(
                "/cart",
                "/home",
                "/category",
                "/checkout",
                "/wishlist",
                "/product_listing",
                "/product_detail"
        );
        this.random = new Random();
        this.userIds = generateUserIds(numberOfUsers);
    }

    // Main method
    public static void main(String[] args) {

        long seed = 12345L;
        int numberOfUsers = 5;
        int numberOfEvents = 50;

        // Create a new instance of the WebDataProducer class
        WebDataProducer webDataProducer = new WebDataProducer(seed, numberOfUsers);

        // Generate a new WebData object
        for (int i = 0; i < numberOfEvents; i++) {
            WebData webData = webDataProducer.generateWebData();
            System.out.println(webData.toString());

            // Send the generated webdata to Kafka
            webDataProducer.sendToKafka(webData);
        }

    }

    private Set<String> generateUserIds(int numberOfUsers) {
        Set<String> userIds = new HashSet<>();
        while (userIds.size() < numberOfUsers) {
            userIds.add(faker.idNumber().valid());
        }
        return userIds;
    }

    public WebData generateWebData() {
        WebData webData = new WebData();
        webData.userId = getRandomUserId();
        webData.sessionId = faker.idNumber().valid();
        webData.timestamp = System.currentTimeMillis();
        webData.sessionDurationSeconds = faker.number().numberBetween(1, 1000);
        webData.pageViews = generatePageViews(faker.number().numberBetween(1, 10));
        webData.cartActivity = new WebData.CartActivity();
        webData.cartActivity.itemsAdded = generateItemsAdded(faker.number().numberBetween(1, 10));
        return webData;
    }

    private String getRandomUserId() {
        int index = random.nextInt(userIds.size());
        return userIds.toArray(new String[0])[index];
    }

    private ArrayList<WebData.PageView> generatePageViews(int numPageViews) {
        ArrayList<WebData.PageView> pageViews = new ArrayList<>();
        for (int i = 0; i < numPageViews; i++) {
            WebData.PageView pageView = new WebData.PageView();
            pageView.pageUrl = pagePaths.get(random.nextInt(pagePaths.size()));
            pageView.timestamp = System.currentTimeMillis();
            pageViews.add(pageView);
        }
        return pageViews;
    }

    private ArrayList<WebData.ItemAdded> generateItemsAdded(int numItemsAdded) {
        ArrayList<WebData.ItemAdded> itemsAdded = new ArrayList<>();

        for (int i = 0; i < numItemsAdded; i++) {
            WebData.ItemAdded itemAdded = new WebData.ItemAdded();
            itemAdded.timestamp = System.currentTimeMillis();
            itemAdded.productId = faker.idNumber().valid();
            itemAdded.quantity = faker.number().numberBetween(1, 10);
            itemsAdded.add(itemAdded);
        }
        return itemsAdded;
    }

    public void sendToKafka(WebData webData) {
        producer.send(new ProducerRecord<>(WEBDATA_TOPIC, webData.userId, webData));
        producer.flush();
    }
}