package com.queuepulse.dto;

import com.queuepulse.entity.RoleName;
import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponse(
        String token,
        String type,
        Long id,
        String name,
        String email,
        Set<RoleName> roles
) {
}
