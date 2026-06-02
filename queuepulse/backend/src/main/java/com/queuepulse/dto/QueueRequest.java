package com.queuepulse.dto;

import com.queuepulse.entity.QueueStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record QueueRequest(
        @NotBlank @Size(max = 120) String name,
        @NotNull Long organizationId,
        QueueStatus status
) {
}
