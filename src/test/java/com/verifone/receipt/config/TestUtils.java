package com.verifone.receipt.config;

public class TestUtils {

    public static KafkaProperties prepareKafkaProperties() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setInsertFailureTopic("test");
        kafkaProperties.setPollTimeout(12);
        return kafkaProperties;
    }

}
