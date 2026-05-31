package com.queuepulse.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record QueueResponse(
        Long id,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}
