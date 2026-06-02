package com.queuepulse.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record JoinQueueResponse(
        Long id,
        Long queueId,
        Long organizationId,
        String token,
        Instant joinedAt,
        long position
) {
}
