package com.verifone.receipt.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class RebalanceListenerTest extends AbstractTestNGSpringContextTests {

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Autowired
    private RebalanceListener rebalanceListener;

    @TestConfiguration
    static class RebalanceListenerTestConfiguration {
        @Bean
        public RebalanceListener rebalanceListener() {
            return new RebalanceListener();
        }
    }

    @Mock
    Consumer<?, ?> consumer;

    @Test
    public void rebalanceListenerTest_success() {
        Map<TopicPartition, Long> partitionToUncommittedOffsetMap = new HashMap<>();
        TopicPartition tp = new TopicPartition("topic", 1);
        partitionToUncommittedOffsetMap.put(tp, System.currentTimeMillis());
        rebalanceListener.setPartitionToUncommittedOffsetMap(partitionToUncommittedOffsetMap);
        rebalanceListener.onPartitionsRevokedBeforeCommit(consumer, null);
        assertEquals(0, partitionToUncommittedOffsetMap.size());
    }

    @Test
    public void rebalanceListenerTest_failure() {
        Map<TopicPartition, Long> partitionToUncommittedOffsetMap = new HashMap<>();
        rebalanceListener.onPartitionsRevokedBeforeCommit(consumer, null);
        assertEquals(0, partitionToUncommittedOffsetMap.size());
    }

    @Test
    public void rebalanceListenerTest_failure01() {
        Map<TopicPartition, Long> partitionToUncommittedOffsetMap = new HashMap<>();
        rebalanceListener.setPartitionToUncommittedOffsetMap(partitionToUncommittedOffsetMap);
        rebalanceListener.onPartitionsRevokedBeforeCommit(consumer, null);
        assertEquals(0, partitionToUncommittedOffsetMap.size());
    }

}