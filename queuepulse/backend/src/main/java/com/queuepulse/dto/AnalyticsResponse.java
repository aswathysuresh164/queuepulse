package com.queuepulse.dto;

import lombok.Builder;

@Builder
public record AnalyticsResponse(
        Double averageWaitingTimeSeconds,
        long customersServedToday,
        Integer peakHour,
        Long peakHourTraffic
) {
}
