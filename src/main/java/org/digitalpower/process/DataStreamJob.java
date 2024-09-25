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
        // Sets up the execution environment, which is the main entry point
        // to building Flink applications.
        // JobManager host and port
        String jobManagerHost = "localhost";
        int jobManagerPort = 8081;

        // Create a configuration object
        Configuration config = new Configuration();

        try (StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment(jobManagerHost, jobManagerPort, config)) {

            // Create a Kafka source
            KafkaSource<WebData> source = KafkaSource.<WebData>builder()
					.setBootstrapServers("localhost:29092")
                    .setTopics("users")
					.setGroupId("test-group")
					.setStartingOffsets(OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(new WebDataDeserializerSchema())
                    .build();

			// Create a data stream from the source
			DataStream<WebData> stream = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

            // Print the stream to the console
            stream.print();

            // Execute the job
            env.execute("Flink Java API Skeleton");
        }

    }
}
