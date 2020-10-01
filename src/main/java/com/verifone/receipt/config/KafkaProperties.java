package com.verifone.receipt.config;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class KafkaProperties {

    private String topicName;

    private String bootstrapServers;

    private String consumerGroupId;

    private Boolean enableAutoCommit = Boolean.FALSE;

    private Integer maxPollRecord;

    private Integer maxPollInterval;

    private Integer heartbeatInterval;

    private Integer sessionTimeout;

    private String autoOffsetReset;

    private Integer concurrency;

    private boolean syncCommit;

    private Integer pollTimeout;

    private boolean ackOnError;

    private String insertFailureTopic;

    private Integer requestTimeOutMS;

    private String bootstrapServerForProducer;

    private Integer batchSize;

    private Integer requestRetries;

    private Integer maxInFlightRequestsPerConnection;

}
