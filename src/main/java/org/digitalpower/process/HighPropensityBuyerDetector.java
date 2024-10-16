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
import org.digitalpower.models.HighPropensityBuyer;
import org.digitalpower.models.WebData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class HighPropensityBuyerDetector extends KeyedProcessFunction<String, WebData, HighPropensityBuyer> {

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
    public void processElement(WebData webData, Context context, Collector<HighPropensityBuyer> collector) throws Exception {
        long thirtyDaysAgo = System.currentTimeMillis() - THIRTY_DAYS;
        long sevenDaysAgo = System.currentTimeMillis() - SEVEN_DAYS;

        // Count the number of page views in the last 30 days
        long visitCount = webData.getPageViews().stream()
                .filter(pageView -> pageView.getTimestamp() > thirtyDaysAgo)
                .count();

        // Add session duration and calculate the average session duration
        avgSessionDurationState.add((long) webData.getSessionDurationSeconds());
        double avgSessionDuration = avgSessionDurationState.get();


        // Count the number of items added to the cart in the last 7 days
        long cartCount = webData.getCartActivity().getItemsAdded().stream()
                .filter(itemAdded -> itemAdded.getTimestamp()> sevenDaysAgo)
                .count();

        // Add the current webdata event
        recentWebDataState.add(webData);

        // Check if the user visited the /checkout page in the last 5 events
        boolean visitedCheckout = false;
        List<WebData> recentWebDataEvents = new ArrayList<>();
        for (WebData recentWebData : recentWebDataState.get()) {
            recentWebDataEvents.add(recentWebData);
            if (recentWebData.getPageViews().stream().anyMatch(pageView -> pageView.getPageUrl().equals("/checkout"))) {
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

        // Return the user ID and the calculated metrics
        if (isHighPropensity) {
            collector.collect(new HighPropensityBuyer(webData.getUserId(), visitCount, cartCount, avgSessionDuration));
        }

    }

}