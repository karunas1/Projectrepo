package com.verifone.receipt.config;

import com.verifone.receipt.annotation.impl.ReceiptConditionServiceImpl;
import com.verifone.receipt.consumer.RebalanceListener;
import com.verifone.receipt.consumer.ReceiptConsumer;
import com.verifone.receipt.service.IReceiptService;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.LogIfLevelEnabled;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableKafka
public class KafkaConfig {

    @ReceiptConditionServiceImpl(name = "kafka")
    public static class ConsumerConfig {

        @Bean
        public ConsumerFactory<String, String> consumerFactory(ApplicationProperties applicationProperties) {
            Map<String, Object> props = new HashMap<>();
            KafkaProperties kafka = applicationProperties.getKafka();
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, kafka.getConsumerGroupId());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafka.getEnableAutoCommit());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafka.getMaxPollRecord());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafka.getMaxPollInterval());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafka.getHeartbeatInterval());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafka.getSessionTimeout());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafka.getAutoOffsetReset());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            return new DefaultKafkaConsumerFactory<>(props);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ApplicationProperties applicationProperties, RebalanceListener rebalanceListener) {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            ConsumerFactory<String, String> config = consumerFactory(applicationProperties);
            factory.setConsumerFactory(config);
            KafkaProperties kafka = applicationProperties.getKafka();
            factory.getContainerProperties().setCommitLogLevel(LogIfLevelEnabled.Level.INFO);
            factory.setConcurrency(kafka.getConcurrency());
            factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
            factory.getContainerProperties().setSyncCommits(kafka.isSyncCommit());
            factory.getContainerProperties().setPollTimeout(kafka.getPollTimeout());
            factory.getContainerProperties().setAckOnError(kafka.isAckOnError());
            factory.getContainerProperties().setConsumerRebalanceListener(rebalanceListener);
            return factory;
        }

        @Bean
        public ReceiptConsumer receiptConsumer(IReceiptService receiptService) {
            return new ReceiptConsumer(receiptService);
        }

    }

    @ReceiptConditionServiceImpl(name = "kafka")
    public static class ProducerConfig {

        @Bean
        public ProducerFactory<String, String> producerFactory(ApplicationProperties applicationProperties) {
            Map<String, Object> configProps = new HashMap<>();
            KafkaProperties kafka = applicationProperties.getKafka();
            configProps.put(
                    org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    kafka.getBootstrapServerForProducer());
            Integer retries = kafka.getRequestRetries();
            configProps.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, retries);
            if (Objects.nonNull(retries) && retries > 0) {
                configProps.put(org.apache.kafka.clients.producer.ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, kafka.getMaxInFlightRequestsPerConnection());
            }
            configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG, kafka.getBatchSize());
            configProps.put(CommonClientConfigs.REQUEST_TIMEOUT_MS_CONFIG, kafka.getRequestTimeOutMS());
            configProps.put(
                    org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class);
            configProps.put(
                    org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                    StringSerializer.class);
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate(ApplicationProperties applicationProperties) {
            return new KafkaTemplate<>(producerFactory(applicationProperties));
        }
    }

}
