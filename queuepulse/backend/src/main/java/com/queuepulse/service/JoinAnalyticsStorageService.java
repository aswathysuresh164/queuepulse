package com.queuepulse.service;

import com.queuepulse.entity.QueueJoinAnalytics;
import com.queuepulse.event.QueueJoinedEvent;
import com.queuepulse.repository.QueueJoinAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinAnalyticsStorageService {

    private final QueueJoinAnalyticsRepository queueJoinAnalyticsRepository;

    @Transactional
    public void store(QueueJoinedEvent event) {
        if (queueJoinAnalyticsRepository.existsByEntryId(event.entryId())) {
            log.debug("Analytics already stored for entryId={}", event.entryId());
            return;
        }

        int joinHour = event.joinedAt().atZone(ZoneOffset.UTC).getHour();

        QueueJoinAnalytics analytics = QueueJoinAnalytics.builder()
                .entryId(event.entryId())
                .queueId(event.queueId())
                .organizationId(event.organizationId())
                .token(event.token())
                .joinedAt(event.joinedAt())
                .joinHour(joinHour)
                .position(event.position())
                .build();

        queueJoinAnalyticsRepository.save(analytics);
        log.info("Stored join analytics for entryId={} token={}", event.entryId(), event.token());
    }
}
