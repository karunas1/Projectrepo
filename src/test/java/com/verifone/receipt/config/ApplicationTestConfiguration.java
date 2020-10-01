package com.verifone.receipt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties
public class ApplicationTestConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "receipt")
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }

}
