package com.queuepulse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record QueueRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description,
        Boolean active
) {
}
