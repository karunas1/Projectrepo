package com.verifone.receipt.consumer;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class RebalanceListener implements ConsumerAwareRebalanceListener {

    private Map<TopicPartition, Long> partitionToUncommittedOffsetMap;

    public void setPartitionToUncommittedOffsetMap(Map<TopicPartition, Long> partitionToUncommittedOffsetMap) {
        this.partitionToUncommittedOffsetMap = partitionToUncommittedOffsetMap;
    }

    private void commitOffsets(Map<TopicPartition, Long> partitionToOffsetMap, Consumer consumer) {
        if(partitionToOffsetMap!=null && !partitionToOffsetMap.isEmpty()) {
            Map<TopicPartition, OffsetAndMetadata> partitionToMetadataMap = new HashMap<>();
            for(Map.Entry<TopicPartition, Long> e : partitionToOffsetMap.entrySet()) {
                log.info("Adding partition & offset for topic{}", e.getKey());
                partitionToMetadataMap.put(e.getKey(), new OffsetAndMetadata(e.getValue() + 1));
            }
            log.info("Consumer : {}, committing the offsets : {}", consumer, partitionToMetadataMap);
            consumer.commitSync(partitionToMetadataMap);
            partitionToOffsetMap.clear();
        }
    }

    @Override
    public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.info("Consumer is going to commit the offsets {}",consumer);
        commitOffsets(partitionToUncommittedOffsetMap, consumer);
        log.info("Committed offsets {}",consumer);
    }

}