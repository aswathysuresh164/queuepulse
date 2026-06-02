package com.queuepulse.service;

import com.queuepulse.event.QueueJoinedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "queuepulse.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class QueueJoinedEventListener {

    private final QueueEventProducer queueEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onQueueJoined(QueueJoinedEvent event) {
        queueEventProducer.publishQueueJoined(event);
    }
}
