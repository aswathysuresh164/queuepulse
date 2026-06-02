package com.queuepulse.repository;

import com.queuepulse.entity.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    long countByQueueId(Long queueId);
}
