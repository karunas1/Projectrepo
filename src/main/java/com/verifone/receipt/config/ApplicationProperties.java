package com.verifone.receipt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component("property")
@ConfigurationProperties(prefix = "receipt")
public class ApplicationProperties {

    private KafkaProperties kafka;

    public KafkaProperties getKafka() {
        return kafka;
    }
}
