package com.DOCKin.worklog.dto;

import com.DOCKin.worklog.model.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 res dto")
public class CommentResponseDto {

    @Schema(description = "댓글 번호", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long commentId;

    @Schema(description = "작업 일지 고유 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long logId;

    @Schema(description = "댓글 작성자id, 사번을 그냥 적으면 됨", example = "1004", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "댓글 내용", example = "댓글 내용을 적으면 됨", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "댓글 작성 시간", example = "2026-01-23 20:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @Schema(description = "댓글 수정 시간",example = "2026-01-23 20:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updatedAt;

    public static CommentResponseDto from(Comment save){
        return CommentResponseDto.builder()
                .commentId(save.getCommentId())
                .logId(save.getLogId().getLogId())
                .userId(save.getUserId().getUserId())
                .content(save.getContent())
                .createdAt(save.getCreatedAt())
                .updatedAt(save.getUpdatedAt())
                .build();

    }

}
