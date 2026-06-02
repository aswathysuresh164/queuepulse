package com.queuepulse.repository;

import com.queuepulse.dto.HourlyTrafficDto;
import com.queuepulse.entity.QueueJoinAnalytics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface QueueJoinAnalyticsRepository extends JpaRepository<QueueJoinAnalytics, Long> {

    boolean existsByEntryId(Long entryId);

    @Query("""
            SELECT COUNT(a)
            FROM QueueJoinAnalytics a
            WHERE a.joinedAt >= :startOfDay
            AND a.joinedAt < :endOfDay
            AND (:queueId IS NULL OR a.queueId = :queueId)
            AND (:organizationId IS NULL OR a.organizationId = :organizationId)
            """)
    long countJoinsToday(
            @Param("startOfDay") Instant startOfDay,
            @Param("endOfDay") Instant endOfDay,
            @Param("queueId") Long queueId,
            @Param("organizationId") Long organizationId);

    @Query("""
            SELECT new com.queuepulse.dto.HourlyTrafficDto(a.joinHour, COUNT(a))
            FROM QueueJoinAnalytics a
            WHERE a.joinedAt >= :startOfDay
            AND a.joinedAt < :endOfDay
            AND (:queueId IS NULL OR a.queueId = :queueId)
            AND (:organizationId IS NULL OR a.organizationId = :organizationId)
            GROUP BY a.joinHour
            ORDER BY COUNT(a) DESC
            """)
    List<HourlyTrafficDto> findPeakHourTraffic(
            @Param("startOfDay") Instant startOfDay,
            @Param("endOfDay") Instant endOfDay,
            @Param("queueId") Long queueId,
            @Param("organizationId") Long organizationId,
            Pageable pageable);
}
