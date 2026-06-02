package com.queuepulse.consumer;

import com.queuepulse.event.QueueJoinedEvent;
import com.queuepulse.service.JoinAnalyticsStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "queuepulse.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class QueueJoinedEventConsumer {

    private final JoinAnalyticsStorageService joinAnalyticsStorageService;

    @KafkaListener(
            topics = "${queuepulse.kafka.topic.queue-joined}",
            groupId = "${queuepulse.kafka.consumer.group-id}",
            containerFactory = "queueJoinedKafkaListenerContainerFactory"
    )
    public void consume(QueueJoinedEvent event) {
        log.debug("Received QueueJoinedEvent entryId={} token={}", event.entryId(), event.token());
        joinAnalyticsStorageService.store(event);
    }
}
