package com.queuepulse.service;

import com.queuepulse.event.QueueJoinedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "queuepulse.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class QueueEventProducer {

    private final KafkaTemplate<String, QueueJoinedEvent> queueJoinedKafkaTemplate;

    @Value("${queuepulse.kafka.topic.queue-joined}")
    private String queueJoinedTopic;

    public void publishQueueJoined(QueueJoinedEvent event) {
        queueJoinedKafkaTemplate.send(queueJoinedTopic, event.queueId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish QueueJoinedEvent for entry {}", event.entryId(), ex);
                    } else {
                        log.debug(
                                "Published QueueJoinedEvent entryId={} to partition {}",
                                event.entryId(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}
