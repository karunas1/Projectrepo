package com.verifone.receipt.consumer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.receipt.config.ApplicationProperties;
import com.verifone.receipt.config.KafkaProperties;
import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.exception.InvalidTemplateDataException;
import com.verifone.receipt.exception.InvalidTemplateException;
import com.verifone.receipt.producer.Sender;
import com.verifone.receipt.service.IReceiptService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.lang.reflect.Type;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static com.verifone.receipt.constant.ReceiptConstants.COMMA_DELIM;

public class ReceiptConsumer {

    /**
     * Logger Info.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ReceiptConsumer.class);

    /**
     * RebalanceListener is used to commit offset of processed transaction in kafka when rebalancing is triggerred.
     */
    @Autowired
    private RebalanceListener rebalanceListener;

    /**
     * ApplicationProperties to keep application specific value.
     */
    @Autowired
    @Qualifier("property")
    private ApplicationProperties applicationProperties;

    /**
     * Sender is used to send the transaction to failure topic in case of getting any exception
     */
    @Autowired
    private Sender sender;

    private IReceiptService receiptService;

    public ReceiptConsumer(IReceiptService rcptService) {
        receiptService = rcptService;
    }

    private CountDownLatch latch = new CountDownLatch(1);

    public CountDownLatch getLatch() {
        return latch;
    }

    /**
     * This method is used for reading message from kafka topic.
     * @param topic topic is a category or feed name to which records are published.
     * @param partition is an ordered, immutable record sequence.
     * @param offset identifies each record location within the partition.
     * @param ack is the acknowledgement object which is used for sending acknowledgement to kafka.
     * @param receiptTransactionString is the message which we are reading from kafka.
     */
    @KafkaListener(topics = "#{'${receipt.kafka.topicName}'.split('" + COMMA_DELIM + "')}", groupId = "${receipt.kafka.consumerGroupId}")
    public void receive(@Header(KafkaHeaders.RECEIVED_TOPIC) String topic, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.OFFSET) long offset, Acknowledgment ack, final String receiptTransactionString)  {

        KafkaProperties kafkaProps = applicationProperties.getKafka();
        latch.countDown();
        putUnCommittedOffset(topic, partition, offset);
        LOG.info("Insert Message Received from offset : {} ", offset);
            if (StringUtils.isNotBlank(receiptTransactionString)) {
                boolean value = false;
                ReceiptTransaction receiptTransaction = convertToReceiptTransaction(receiptTransactionString);
                if (receiptTransaction != null) {
                    LOG.info("sending receipt process started.");
                    try {
                        final Long startTime = System.currentTimeMillis();
                        receiptService.processReceipt(receiptTransaction);
                        LOG.info("Total time taken Receipt Services in {} ms ", (System.currentTimeMillis() - startTime));
                        value=true;
                    } catch (InvalidTemplateDataException | InvalidTemplateException | EmailServiceException e) {
                        LOG.error(e.getMessage(), e);
                    } catch (Exception e) {
                        LOG.error("Error while Receiving Message from offset", e);
                    }
                    if (!value) {
                        insertTransactionFailure(receiptTransactionString, kafkaProps.getInsertFailureTopic());
                    }
                }
            }
            ack.acknowledge();
    }

    /**
     * This method is used for listing topic and partition for rebalance.
     * @param topic is a category or feed name to which records are published.
     * @param partition is an ordered, immutable record sequence.
     * @param offset identifies each record location within the partition.
     */

    protected void putUnCommittedOffset(String topic, int partition, long offset) {
        TopicPartition tp = new TopicPartition(topic, partition);
        Map<TopicPartition, Long> partitionToUncommittedOffsetMap = new ConcurrentHashMap<>();
        partitionToUncommittedOffsetMap.put(tp, offset);
        rebalanceListener.setPartitionToUncommittedOffsetMap(partitionToUncommittedOffsetMap);
    }

    /**
     * This method is used for converting string to object.
     * @param payload data which we consumed from kafka.
     * @return the ReceiptTransaction object after conversion from json payload.
     */
    private ReceiptTransaction convertToReceiptTransaction(final String payload) {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
                @Override
                public OffsetDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    return OffsetDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString());
                }
            }).create();
            return gson.fromJson(payload, new TypeToken<ReceiptTransaction>() {}.getType());
        } catch (JsonSyntaxException e) {
            LOG.error(e.getMessage());
            return null;
        }
    }

    /**
     * This method is used to send a failure transaction to failure topic.
     * @param failureTopic is the failure topic.
     * @param payload is the message which we are reading from kafka.
     */
    private void insertTransactionFailure(String payload, String failureTopic) {
        LOG.info("Receipt sending operation failed.");
        sender.publishMessage(payload, failureTopic);
    }

}
