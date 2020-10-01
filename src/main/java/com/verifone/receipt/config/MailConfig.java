package com.verifone.receipt.config;

import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.verifone.receipt.dao.SMTPConfigDAO;
import com.verifone.receipt.exception.DaoException;
import javax.mail.Session;

@Configuration
public class MailConfig {

    @Autowired
    private SMTPConfigDAO smtpConfigDAO;

    @Autowired
    private ApplicationContext context;

    @Value("${receipt.mail.host}")
    private String host;

    @Bean
    public Session getSession() throws DaoException {
        Properties properties = MailConfig.getMailProperties();
        properties.setProperty("mail.smtp.host", smtpConfigDAO.getSMTPConfDetails().getHost());
        properties.setProperty("mail.smtp.port", smtpConfigDAO.getSMTPConfDetails().getPort());
        Session session = Session.getInstance(properties);
        session.getProperties().put("mail.smtp.ssl.trust", host);
        // Over ride the Object Configuration not to allow additional field in request
        // specific requirement to make strict request JSON Schema
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        return session;
    }

    private static Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        return properties;
    }
}
