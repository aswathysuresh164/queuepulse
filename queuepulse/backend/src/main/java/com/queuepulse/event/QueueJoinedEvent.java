package com.queuepulse.event;

import java.time.Instant;

public record QueueJoinedEvent(
        Long entryId,
        Long queueId,
        Long organizationId,
        String token,
        Instant joinedAt,
        long position
) {
}
