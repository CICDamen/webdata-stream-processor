package org.digitalpower.producer;

import com.github.javafaker.Faker;
import org.digitalpower.models.WebData;

import java.util.*;

public class WebDataProducer {

    private final Faker faker;
    private final List<String> pagePaths;
    private final Random random;
    private final Set<String> userIds;

    // Constructor
    public WebDataProducer(long seed, int numberOfUsers) {
        this.faker = new Faker(new Random(seed));
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

    private Set<String> generateUserIds(int numberOfUsers) {
        Set<String> userIds = new HashSet<>();
        while (userIds.size() < numberOfUsers) {
            userIds.add(faker.idNumber().valid());
        }
        return userIds;
    }

    public WebData generateWebData() {
        WebData webData = new WebData();
        webData.setUserId(getRandomUserId());
        webData.setSessionId(faker.idNumber().valid());
        webData.setTimestamp(System.currentTimeMillis());
        webData.setSessionDurationSeconds(faker.number().numberBetween(1, 1000));
        webData.setPageViews(generatePageViews(faker.number().numberBetween(1, 10)));
        webData.setCartActivity(new WebData.CartActivity());
        webData.getCartActivity().setItemsAdded(generateItemsAdded(faker.number().numberBetween(1, 10)));
        return webData;
    }

    private String getRandomUserId() {
        int index = random.nextInt(userIds.size());
        return userIds.toArray(new String[0])[index];
    }

    private List<WebData.PageView> generatePageViews(int numPageViews) {
        List<WebData.PageView> pageViews = new ArrayList<>();
        for (int i = 0; i < numPageViews; i++) {
            WebData.PageView pageView = new WebData.PageView();
            pageView.setPageUrl(pagePaths.get(random.nextInt(pagePaths.size())));
            pageView.setTimestamp(System.currentTimeMillis());
            pageViews.add(pageView);
        }
        return pageViews;
    }

    private List<WebData.ItemAdded> generateItemsAdded(int numItemsAdded) {
        List<WebData.ItemAdded> itemsAdded = new ArrayList<>();

        for (int i = 0; i < numItemsAdded; i++) {
            WebData.ItemAdded itemAdded = new WebData.ItemAdded();
            itemAdded.setTimestamp(System.currentTimeMillis());
            itemAdded.setProductId(faker.idNumber().valid());
            itemAdded.setQuantity(faker.number().numberBetween(1, 10));
            itemsAdded.add(itemAdded);
        }
        return itemsAdded;
    }
}