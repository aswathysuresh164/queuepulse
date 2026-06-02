package com.queuepulse.service;

import com.queuepulse.dto.AnalyticsResponse;
import com.queuepulse.dto.HourlyTrafficDto;
import com.queuepulse.repository.AnalyticsRepository;
import com.queuepulse.repository.QueueJoinAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final QueueJoinAnalyticsRepository queueJoinAnalyticsRepository;

    public AnalyticsResponse getAnalytics(Long queueId, Long organizationId) {
        Instant startOfDay = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = startOfDay.plusSeconds(86_400);

        Double averageWaitingTimeSeconds = analyticsRepository.findAverageWaitingTimeSeconds(
                queueId, organizationId);

        long customersServedToday = analyticsRepository.countCustomersServedToday(
                startOfDay, endOfDay, queueId, organizationId);

        List<HourlyTrafficDto> peakHours = queueJoinAnalyticsRepository.findPeakHourTraffic(
                startOfDay, endOfDay, queueId, organizationId, PageRequest.of(0, 1));

        Integer peakHour = null;
        Long peakHourTraffic = null;
        if (!peakHours.isEmpty()) {
            HourlyTrafficDto peak = peakHours.getFirst();
            peakHour = peak.getHour();
            peakHourTraffic = peak.getCount();
        }

        return AnalyticsResponse.builder()
                .averageWaitingTimeSeconds(averageWaitingTimeSeconds)
                .customersServedToday(customersServedToday)
                .peakHour(peakHour)
                .peakHourTraffic(peakHourTraffic)
                .build();
    }
}
