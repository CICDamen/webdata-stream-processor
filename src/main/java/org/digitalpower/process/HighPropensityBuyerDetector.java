package org.digitalpower.process;

import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.digitalpower.model.WebData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class HighPropensityBuyerDetector extends KeyedProcessFunction<String, WebData, String> {

    private static final long serialVersionUID = 1L;

    private static final long THIRTY_DAYS = 30L * 24 * 60 * 60 * 1000;
    private static final long SEVEN_DAYS = 7L * 24 * 60 * 60 * 1000;
    private static final int MAX_RECENT_WEBDATA_EVENTS = 5;

    private transient AggregatingState<Long, Double> avgSessionDurationState;
    private transient ListState<WebData> recentWebDataState;

    private static final Logger LOG = LoggerFactory.getLogger(HighPropensityBuyerDetector.class);

    @Override
    public void open(Configuration parameters) throws Exception {

        // State for storing recent web data
        ListStateDescriptor<WebData> recentWebDataDescriptor = new ListStateDescriptor<>(
                "recentWebData",
                WebData.class
        );
        recentWebDataState = getRuntimeContext().getListState(recentWebDataDescriptor);

        // State for storing the average session duration
        AggregatingStateDescriptor<Long, Tuple2<Long, Long>, Double> avgSessionDurationDescriptor =
                new AggregatingStateDescriptor<>(
                        "avgSessionDuration",
                        new AggregateFunction<Long, Tuple2<Long, Long>, Double>() {
                            @Override
                            public Tuple2<Long, Long> createAccumulator() {
                                return Tuple2.of(0L, 0L);
                            }

                            @Override
                            public Tuple2<Long, Long> add(Long value, Tuple2<Long, Long> accumulator) {
                                return Tuple2.of(accumulator.f0 + value, accumulator.f1 + 1);
                            }

                            @Override
                            public Double getResult(Tuple2<Long, Long> accumulator) {
                                return accumulator.f1 == 0 ? 0.0 : (double) accumulator.f0 / accumulator.f1;
                            }

                            @Override
                            public Tuple2<Long, Long> merge(Tuple2<Long, Long> a, Tuple2<Long, Long> b) {
                                return Tuple2.of(a.f0 + b.f0, a.f1 + b.f1);
                            }
                        },
                        Types.TUPLE(Types.LONG, Types.LONG)
                );
        avgSessionDurationState = getRuntimeContext().getAggregatingState(avgSessionDurationDescriptor);
    }

    @Override
    public void processElement(WebData webData, Context context, Collector<String> collector) throws Exception {
        long thirtyDaysAgo = System.currentTimeMillis() - THIRTY_DAYS;
        long sevenDaysAgo = System.currentTimeMillis() - SEVEN_DAYS;

        // Count the number of page views in the last 30 days
        long visitCount = webData.pageViews.stream()
                .filter(pageView -> pageView.timestamp > thirtyDaysAgo)
                .count();
        System.out.println("Visit count: " + visitCount);

        // Add session duration and calculate the average session duration
        avgSessionDurationState.add((long) webData.sessionDurationSeconds);
        double avgSessionDuration = avgSessionDurationState.get();


        // Count the number of items added to the cart in the last 7 days
        long cartCount = webData.cartActivity.itemsAdded.stream()
                .filter(itemAdded -> itemAdded.timestamp > sevenDaysAgo)
                .count();

        // Add the current webdata event
        recentWebDataState.add(webData);

        // Check if the user visited the /checkout page in the last X events
        boolean visitedCheckout = false;
        List<WebData> recentWebDataEvents = new ArrayList<>();
        for (WebData recentWebData : recentWebDataState.get()) {
            recentWebDataEvents.add(recentWebData);
            if (recentWebData.pageViews.stream().anyMatch(pageView -> pageView.pageUrl.equals("/checkout"))) {
                visitedCheckout = true;
                break;
            }
        }

        // Remove the oldest event if the list is too long
        if (recentWebDataEvents.size() > MAX_RECENT_WEBDATA_EVENTS) {
            recentWebDataEvents.remove(0);
        }
        recentWebDataState.update(recentWebDataEvents);

        boolean isHighPropensity = visitCount >= 5 && avgSessionDuration > 5 * 60 && cartCount >= 1 && visitedCheckout;

        String logMessage = "Not a high propensity buyer";

        // Log the user details if high propensity
        if (isHighPropensity) {
            logMessage = "High propensity buyer detected!\n" +
                    "User ID: " + webData.userId + "\n" +
                    "Session ID: " + webData.sessionId + "\n" +
                    "Visit Count: " + visitCount + "\n" +
                    "Average Session Duration: " + avgSessionDuration + "\n" +
                    "Cart Count: " + cartCount;
        }

        // Collect the log message
        collector.collect("Processed session: " + webData.sessionId + " for user: " + webData.userId + " - " + logMessage);

    }
}