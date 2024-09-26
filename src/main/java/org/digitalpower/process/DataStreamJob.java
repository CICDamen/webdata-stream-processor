package org.digitalpower.process;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.digitalpower.deserializer.WebDataDeserializerSchema;
import org.digitalpower.model.WebData;

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
                    .setTopics("web-data")
                    .setGroupId("test-group")
                    .setStartingOffsets(OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(new WebDataDeserializerSchema())
                    .build();


            // Create the data streams from the Kafka sources
            DataStream<WebData> webDataStream = env.fromSource(webDataSource, WatermarkStrategy.noWatermarks(), "WebData source");

            // Print the stream to the console
            webDataStream.print();

            // Execute the job
            env.execute("Process user and web data");

        }
    }
}
