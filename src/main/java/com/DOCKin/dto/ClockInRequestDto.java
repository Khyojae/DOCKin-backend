package com.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClockInRequestDto {
    private String inLocation;
}
