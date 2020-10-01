package com.verifone.receipt.config;

//import static org.junit.jupiter.api.Assertions.*;

import com.verifone.receipt.consumer.RebalanceListener;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.verifone.receipt.config.TestUtils.prepareKafkaProperties;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class KafkaConfigTest extends AbstractTestNGSpringContextTests {

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Autowired
    private KafkaConfig.ConsumerConfig consumerConfig;

    @TestConfiguration
    static class KafkaConsumerConfigTestConfiguration {
        @Bean
        public KafkaConfig.ConsumerConfig kafkaConsumerConfig() {
            return new KafkaConfig.ConsumerConfig();
        }
    }

    @Mock
    private ApplicationProperties applicationProperties;

    @Mock
    private RebalanceListener rebalanceListener;

    @Autowired
    private KafkaConfig.ProducerConfig producerConfig;

    @TestConfiguration
    static class ProducerConfigTestConfiguration {
        @Bean
        public KafkaConfig.ProducerConfig kafkaProducerConfig() {
            return new KafkaConfig.ProducerConfig();
        }
    }

    @Test
    public void kafkaConsumerConfigTest_success() {
        when(applicationProperties.getKafka()).thenReturn(prepareKafkaProperties());
        ConsumerFactory<String, String> factory = consumerConfig.consumerFactory(applicationProperties);
        assertNotNull(factory);
        verify(applicationProperties).getKafka();
    }

    @Test
    public void kafkaConsumerConfigTest_success01() {
        when(applicationProperties.getKafka()).thenReturn(prepareKafkaProperties());
        ConcurrentKafkaListenerContainerFactory<String, String> factory = consumerConfig.kafkaListenerContainerFactory(applicationProperties, rebalanceListener);
        assertNotNull(factory);
        verify(applicationProperties, times(2)).getKafka();
    }

    @Test
    public void kafkaProducerConfigTest_success() {
        when(applicationProperties.getKafka()).thenReturn(prepareKafkaProperties());
        ProducerFactory<String, String> factory = producerConfig.producerFactory(applicationProperties);
        assertNotNull(factory);
        KafkaTemplate kafkaTemplate = new KafkaTemplate(factory);
        assertNotNull(kafkaTemplate);
        verify(applicationProperties).getKafka();
    }

    @Test
    public void kafkaProducerConfigTest_success01() {
        when(applicationProperties.getKafka()).thenReturn(prepareKafkaProperties());
        KafkaTemplate kafkaTemplate = producerConfig.kafkaTemplate(applicationProperties);
        assertNotNull(kafkaTemplate);
        verify(applicationProperties).getKafka();
    }


}