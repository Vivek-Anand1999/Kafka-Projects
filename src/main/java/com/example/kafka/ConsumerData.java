package com.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class ConsumerData {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerData.class);
    private static final String bootStrapServer = "localhost:9092";
    private static final String groupId = "java-group-consumer";
    Properties properties;

    public ConsumerData() {
        properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public void consumeMessage() {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList("fruits"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                log.info("\u001B[1;32m" + "Received Record: " + "\u001B[0m" +
                        "\u001B[1;34m" + "key: " + "\u001B[0m" + record.key() + " " +
                        "\u001B[1;35m" + "value: " + "\u001B[0m" + record.value() + " " +
                        "\u001B[1;36m" + "Topic: " + "\u001B[0m" + record.topic() + " " +
                        "\u001B[1;33m" + "offset: " + "\u001B[0m" + record.offset() + " " +
                        "\u001B[1;31m" + "partition: " + "\u001B[0m" + record.partition());
            }
        }
    }
    public static void main(String[] args){
        ConsumerData consumerData = new ConsumerData();
        consumerData.consumeMessage();
    }
}
