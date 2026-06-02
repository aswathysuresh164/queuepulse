package com.queuepulse.repository;

import com.queuepulse.entity.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface AnalyticsRepository extends JpaRepository<QueueEntry, Long> {

    @Query("""
            SELECT AVG(
                EXTRACT(EPOCH FROM e.servedAt) - EXTRACT(EPOCH FROM e.joinedAt)
            )
            FROM QueueEntry e
            WHERE e.servedAt IS NOT NULL
            AND (:queueId IS NULL OR e.queue.id = :queueId)
            AND (:organizationId IS NULL OR e.queue.organization.id = :organizationId)
            """)
    Double findAverageWaitingTimeSeconds(
            @Param("queueId") Long queueId,
            @Param("organizationId") Long organizationId);

    @Query("""
            SELECT COUNT(e)
            FROM QueueEntry e
            WHERE e.servedAt IS NOT NULL
            AND e.servedAt >= :startOfDay
            AND e.servedAt < :endOfDay
            AND (:queueId IS NULL OR e.queue.id = :queueId)
            AND (:organizationId IS NULL OR e.queue.organization.id = :organizationId)
            """)
    long countCustomersServedToday(
            @Param("startOfDay") Instant startOfDay,
            @Param("endOfDay") Instant endOfDay,
            @Param("queueId") Long queueId,
            @Param("organizationId") Long organizationId);
}
