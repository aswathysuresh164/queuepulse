package com.queuepulse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HourlyTrafficDto {

    private final Integer hour;
    private final Long count;
}
