package com.DOCKin.worklog.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "댓글 업데이트 req dto")
public class CommentUpdateRequestDto {

    @Schema(description = "댓글 내용", example = "댓글 내용 적으면 됨", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

}
