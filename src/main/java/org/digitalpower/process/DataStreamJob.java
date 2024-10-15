package org.digitalpower.process;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.digitalpower.deserializer.WebDataDeserializerSchema;
import org.digitalpower.common.WebData;

public class DataStreamJob {

    public static void main(String[] args) throws Exception {

        // Create a configuration object
        Configuration config = new Configuration();
        String BOOTSTRAP_SERVER = "127.0.0.1:29092";

        // Setup local execution environment with Web UI
        try (StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(config)) {

            // Create the Kafka source
            KafkaSource<WebData> webDataSource = KafkaSource.<WebData>builder()
                    .setBootstrapServers(BOOTSTRAP_SERVER)
                    .setTopics("webdata")
                    .setGroupId("test-group")
                    .setStartingOffsets(OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(new WebDataDeserializerSchema())
                    .build();

            // Create the data stream from the Kafka sources
            DataStream<WebData> webDataStream = env.fromSource(webDataSource, WatermarkStrategy.noWatermarks(), "webdata");

            // Apply the HighPropensityDetector process function
            DataStream<HighPropensityBuyer> highPropensityBuyers = webDataStream
                    .keyBy(WebData::getUserId)
                    .process(new HighPropensityBuyerDetector())
                    .name("high-propensity-detector");

            highPropensityBuyers.print();

            // TO DO:
            // - Kafka sink
            // - Combine with other data streams
            // - Use mapping from database


            // Execute the job
            env.execute("Webdata - High propensity buyers");

        }
    }
}
