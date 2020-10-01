package com.verifone.receipt.config;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
@TestPropertySource(locations = "/templates/application-test.properties")
public class ApplicationPropertiesTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ApplicationProperties properties;

    @Test
    public void testProperties() {
        String ipAddress = "10.99.9.142:54543";
        KafkaProperties kafkaProperties = properties.getKafka();
        assertEquals("testTopic", kafkaProperties.getTopicName());
        assertEquals(ipAddress, kafkaProperties.getBootstrapServers());
        assertEquals("test-consumer-group", kafkaProperties.getConsumerGroupId());
        assertEquals(new Integer(300), kafkaProperties.getMaxPollRecord());
        assertEquals(new Integer(600000), kafkaProperties.getMaxPollInterval());
        assertEquals(new Integer(2000), kafkaProperties.getHeartbeatInterval());
        assertEquals(new Integer(20000), kafkaProperties.getSessionTimeout());
        assertEquals("earliest", kafkaProperties.getAutoOffsetReset());
        assertEquals(new Integer(1), kafkaProperties.getConcurrency());
        assertEquals(true, kafkaProperties.isSyncCommit());
        assertEquals(new Integer(0), kafkaProperties.getPollTimeout());
        assertEquals(false, kafkaProperties.isAckOnError());
        assertEquals("insert-failure-topic",kafkaProperties.getInsertFailureTopic());
        assertEquals(new Integer(100), kafkaProperties.getRequestTimeOutMS());
        assertEquals("12.01.0.1:90967", kafkaProperties.getBootstrapServerForProducer());
        assertEquals(new Integer(0), kafkaProperties.getBatchSize());
        assertEquals(new Integer(5), kafkaProperties.getRequestRetries());
        assertEquals(new Integer(1), kafkaProperties.getMaxInFlightRequestsPerConnection());
    }

}