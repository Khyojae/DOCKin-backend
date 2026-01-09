package com.DOCKin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//작업일지 조회용
public class Work_logsDto {
    private Long log_id;
    private String user_id;
    private Long equipment_id;
    private String title;
    private String log_text;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
