package com.queuepulse.dto;

import com.queuepulse.entity.QueueStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record QueueResponse(
        Long id,
        String name,
        Long organizationId,
        QueueStatus status,
        Instant createdAt
) {
}
