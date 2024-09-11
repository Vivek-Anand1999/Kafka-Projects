package com.example.kafka;

/**
 * ! when we share data from kafka producer to the kafka server we need to send using key value pair
 * ! we can not share object directly into the kafka we need(SERIAlIZER (POJO -> JSON) & DESERIAlIZER (JSON -> POJO))
 * ! Producer to kafka -> SERIAlIZER
 * ! kafka to consumer -> DESERIAlIZER
 * ! when we are sending data to the kafka server we need to send as producer record object, In order to send the data to Kafka, the user need to create a ProducerRecord.
 * #  private static final String bootStrapServer = "localhost:9092"; // Kafka broker address This is the address of kafka server
 */

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class ProducerData {

    private static final Logger logger = LoggerFactory.getLogger(ProducerData.class);
    private static final String bootStrapServer = "localhost:9092";

    Properties properties;

    public ProducerData() {
        // Set up the producer properties
        log.info("Setting properties file of Kafka...");
        properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    public void sendMessage(String topic, String key, String value) {
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);
        kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    log.info("\u001B[1;32m" + "===== RecordMetaData =====" + "\u001B[0m\n" +
                            "\u001B[1;34m" + "Topics:      " + "\u001B[0m" + metadata.topic() + "\n" +
                            "\u001B[1;35m" + "Offset:      " + "\u001B[0m" + metadata.offset() + "\n" +
                            "\u001B[1;36m" + "Partitions:  " + "\u001B[0m" + metadata.partition() + "\n" +
                            "\u001B[1;33m" + "Timestamp:   " + "\u001B[0m" + metadata.timestamp() + "\n" +
                            "\u001B[1;31m" + "Class:       " + "\u001B[0m" + metadata.getClass() + "\n" +
                            "\u001B[1;32m" + "==========================" + "\u001B[0m");
                } else {
                    log.error("Error occurred during sending data...");
                }
            }
        });
        kafkaProducer.flush(); // it ensure everything must deliver before closing.
        kafkaProducer.close(); // release resources.
    }

    public static void main(String[] args) {
        ProducerData producerData = new ProducerData();
        log.info("Sending data to Kafka ....");
        List<String> list = Arrays.asList("Apple", "Banana", "Orange", "Mango", "Grapes", "Pineapple", "Strawberry", "Blueberry", "Watermelon", "Peach", "Pear", "Cherry", "Kiwi", "Papaya", "Lemon", "Lime", "Pomegranate", "Avocado", "Coconut", "Plum");
        for (int i = 0; i < list.size(); i++) {
            producerData.sendMessage("fruits", "key1", list.get(i));
        }
    }

}