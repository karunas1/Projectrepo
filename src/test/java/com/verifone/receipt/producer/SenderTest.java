package com.verifone.receipt.producer;

import com.verifone.receipt.exception.KafkaImporterException;
import com.verifone.receipt.model.KafkaServiceResponse;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.annotations.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
public class SenderTest extends AbstractTestNGSpringContextTests {

    private static final String SUCCESS_TOPIC_NAME = "vfi-failure-topic";

    private static final Logger LOGGER = LoggerFactory.getLogger(SenderTest.class);

    @Autowired
    private Sender sender;

    @TestConfiguration
    static class SenderTestConfiguration {
        @Bean
        public Sender sender() {
            return new Sender();
        }
    }

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private ListenableFuture<SendResult<String, String>> buildKafkaMockResponse() {
        return mock(ListenableFuture.class);
    }

    private String buildRequestMessageFailure() {
        String jsonMessage = "{ \"security\":{ \"keySerialNumber\":\"xyz\" }, \"header\":{ \"state\":\"AUTHORISED\",\"source\":{ \"sourceId\":\"OCS\" } }, \"transaction\":{\"gatewayTraceId\":\"UK5MIDB025V3A110357604\",\"transactionType\":\"SALE\",\"transactionId\":\"12345\",\"transactionDescription\":\"test\",\"merchant\":{ \"id\":\"140017402\" }, \"amount\":{ \"value\":\"7890\" }, \"createdDateTime\":\"2018-12-28T00:00:00Z\", \"outcome\":[ { \"responseCode\":\"test_import_dimeboxblr\" } ], \"context\":{ \"paymentContext\":{ \"channel\":\"ECOM\" } }, \"instrument\":[ { \"cardholderToken\":\"47611457009631750010\", \"tokenExpiryDate\":\"2019-02-23T00:00:00.000Z\" } ], \"instalment\":{ \"recurringType\":\"INSTALMENT\" }, \"customer\":{ \"billingAddress\":{ \"city\":\"Amsterdam\", \"postCode\":\"1111 XX\", \"countrySubdivision\":\"North Holland\", \"addressLine1\":\"Singel 250\", \"addressLine2\":\"string\", \"addressLine3\":\"string\" }, \"country\":\"NL\", \"identification\":{ \"dateOfBirth\":\"1970-01-01\", \"socialSecurityNumber\":\"111213\" }, \"email\":\"spam@email.com\", \"firstName\":\"John\", \"lastName\":\"Doe\", \"phoneNumbers\":[ { \"value\":\"0998888978\" } ], \"prefix\":\"mr\" } } }\n";
        return jsonMessage;
    }

    @Test
    public void testPublishMessage() throws URISyntaxException {
        KafkaServiceResponse kafkaResponse;
        try {
            sender.publishMessage(null, null);
        } catch (Exception e) {
            LOGGER.error("Invalid message. There is an exception", e);
        }
        try {
            kafkaResponse = sender.publishMessage(buildRequestMessageFailure(), SUCCESS_TOPIC_NAME);
        } catch (Exception e) {
            LOGGER.error("Invalid message. There is an I/O exception", e);
        }
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(buildKafkaMockResponse());
        kafkaResponse = sender.publishMessage(buildRequestMessageFailure(), SUCCESS_TOPIC_NAME);
        assertNotNull(kafkaResponse);
        assertEquals(HttpStatus.ACCEPTED.name(), kafkaResponse.getStatus());
        assertEquals(HttpStatus.ACCEPTED.value(), kafkaResponse.getStatusCode().intValue());
        sender.publishMessage(null, SUCCESS_TOPIC_NAME);
    }

    @Test
    public void failureTest() {
        sender.publishMessage(buildRequestMessageFailure(), SUCCESS_TOPIC_NAME);
    }

    @Test
    public void failureTest01() {
        when(kafkaTemplate.send(Mockito.anyString(), Mockito.anyString(), any())).thenThrow(new KafkaImporterException(""));
        KafkaServiceResponse response = sender.publishMessage(buildRequestMessageFailure(), SUCCESS_TOPIC_NAME);
        assertNull(response);
    }

}