package com.queuepulse.service;

import com.queuepulse.entity.QueueEntry;
import com.queuepulse.repository.QueueEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class QueueEntryService {

    private final QueueEntryRepository queueEntryRepository;

    @Transactional
    public void markServed(Long entryId) {
        QueueEntry entry = queueEntryRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Queue entry not found"));

        if (entry.getServedAt() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer already served");
        }

        entry.setServedAt(Instant.now());
        queueEntryRepository.save(entry);
    }
}
