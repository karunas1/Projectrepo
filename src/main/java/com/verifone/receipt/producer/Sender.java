package com.verifone.receipt.producer;

import com.verifone.receipt.exception.KafkaImporterException;
import com.verifone.receipt.model.KafkaServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.verifone.receipt.constant.ReceiptConstants.KAFKA_ERR_MSG;

/**
 * Created by GayatriB1 on 11/21/2019.
 */
@Component
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaServiceResponse publishMessage(String request, String topicName) {
        LOGGER.info("Received request to publish in kafka topic : Sender:publishEventToKafka() {}", topicName);
        try {
            if (!StringUtils.isBlank(request)) {
                return sendRequestToKafka(request, topicName);
            }
        } catch (KafkaImporterException e) {
            LOGGER.error("Error in Sender.publishMessage() : Error while publishing message to kafka : {}", e.getMessage());
        }
        return null;
    }

    private KafkaServiceResponse sendRequestToKafka(String request, String topicName) {
        KafkaServiceResponse response = new KafkaServiceResponse();
        try {
            kafkaTemplate.send(topicName, createKeyJsonString(MDC.get("correlation_id")), request);
        } catch (Exception e) {
            throw new KafkaImporterException(KAFKA_ERR_MSG + e.getMessage());
        }
        response.setStatus(HttpStatus.ACCEPTED.name());
        response.setStatusCode(HttpStatus.ACCEPTED.value());
        LOGGER.info("End of publish in kafka topic : sender:publishMessage() {}", topicName);
        return response;
    }

    public String createKeyJsonString(String uniqueId) throws JSONException {
        JSONObject keyJson = new JSONObject();
        keyJson.put("uniqueId", uniqueId);
        return keyJson.toString();
    }

}
