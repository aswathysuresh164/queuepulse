package com.queuepulse.service;

import com.queuepulse.dto.JoinQueueResponse;
import com.queuepulse.entity.Queue;
import com.queuepulse.entity.QueueEntry;
import com.queuepulse.entity.QueueStatus;
import com.queuepulse.event.QueueJoinedEvent;
import com.queuepulse.repository.QueueEntryRepository;
import com.queuepulse.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JoinQueueService {

    private final QueueRepository queueRepository;
    private final QueueEntryRepository queueEntryRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public JoinQueueResponse join(Long queueId) {
        Queue queue = queueRepository.findByIdForUpdate(queueId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue not found"));

        if (queue.getStatus() != QueueStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Queue is not accepting new entries");
        }

        String token = formatToken(queue.getTokenPrefix(), queue.getNextTokenNumber());
        queue.setNextTokenNumber(queue.getNextTokenNumber() + 1);
        queueRepository.save(queue);

        Instant joinedAt = Instant.now();
        QueueEntry entry = QueueEntry.builder()
                .queue(queue)
                .token(token)
                .joinedAt(joinedAt)
                .build();
        QueueEntry saved = queueEntryRepository.save(entry);

        long position = queueEntryRepository.countByQueueId(queueId);

        eventPublisher.publishEvent(new QueueJoinedEvent(
                saved.getId(),
                queue.getId(),
                queue.getOrganization().getId(),
                saved.getToken(),
                saved.getJoinedAt(),
                position));

        return JoinQueueResponse.builder()
                .id(saved.getId())
                .queueId(queue.getId())
                .organizationId(queue.getOrganization().getId())
                .token(saved.getToken())
                .joinedAt(saved.getJoinedAt())
                .position(position)
                .build();
    }

    private String formatToken(String prefix, int number) {
        return prefix + number;
    }
}
