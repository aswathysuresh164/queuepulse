package com.queuepulse.controller;

import com.queuepulse.dto.AnalyticsResponse;
import com.queuepulse.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public AnalyticsResponse getAnalytics(
            @RequestParam(required = false) Long queueId,
            @RequestParam(required = false) Long organizationId) {
        return analyticsService.getAnalytics(queueId, organizationId);
    }
}
