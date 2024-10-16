package org.digitalpower.process;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.digitalpower.deserialize.WebDataDeserializer;
import org.digitalpower.models.HighPropensityBuyer;
import org.digitalpower.models.WebData;
import org.digitalpower.serialize.HighPropensityBuyerSerializationSchema;

public class DataStreamJob {

    private static String KAFKA_BOOTSTRAP_SERVERS;
    private static String KAFKA_INPUT_TOPIC = "webdata";
    private static String KAFKA_OUTPUT_TOPIC = "high-propensity-buyers";


    public static void main(String[] args) throws Exception {

        // Parse the command line arguments
        ParameterTool params = ParameterTool.fromArgs(args);
        KAFKA_BOOTSTRAP_SERVERS = params.getRequired("kafka.bootstrap.servers");
        KAFKA_INPUT_TOPIC = params.getRequired("kafka.topic.input");
        KAFKA_OUTPUT_TOPIC = params.getRequired("kafka.topic.output");


        // Get Flink execution environment
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // Create the Kafka source and sink
        KafkaSource<WebData> webDataSource = createKafkaSource();
        KafkaSink<HighPropensityBuyer> highPropensityBuyerSink = createKafkaSink();

        // Create the data stream from the source
        DataStream<WebData> webDataStream = env.fromSource(webDataSource, WatermarkStrategy.noWatermarks(), KAFKA_INPUT_TOPIC);

        // Apply the HighPropensityDetector process function
        DataStream<HighPropensityBuyer> highPropensityBuyers = webDataStream
                .keyBy(WebData::getUserId)
                .process(new HighPropensityBuyerDetector())
                .name("high-propensity-detector");


        // Write the results to the Kafka sink
        highPropensityBuyers.sinkTo(highPropensityBuyerSink);

        // Execute the job
        env.execute("Webdata - High propensity buyers");

    }

    private static KafkaSource<WebData> createKafkaSource() {
        return KafkaSource.<WebData>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                .setTopics(KAFKA_INPUT_TOPIC)
                .setGroupId("test-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new WebDataDeserializer())
                .build();
    }

    private static KafkaSink<HighPropensityBuyer> createKafkaSink() {
        return KafkaSink.<HighPropensityBuyer>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic(KAFKA_OUTPUT_TOPIC)
                        .setValueSerializationSchema(new HighPropensityBuyerSerializationSchema())
                        .build()
                )
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

}
