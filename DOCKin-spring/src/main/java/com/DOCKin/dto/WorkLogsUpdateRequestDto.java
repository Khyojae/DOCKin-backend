package com.DOCKin.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
//작업일지 수정용
public class WorkLogsUpdateRequestDto {
    private String title;
    private String log_text;
    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;
}
