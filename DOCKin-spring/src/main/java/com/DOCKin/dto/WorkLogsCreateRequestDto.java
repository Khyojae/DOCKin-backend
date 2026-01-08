package com.DOCKin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//작업일지 생성용
public class WorkLogsCreateRequestDto {
    private String title;
    private String log_text;

    private Long equipmentId;

    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
}
