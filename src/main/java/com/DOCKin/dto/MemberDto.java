package com.DOCKin.dto;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//멤버조회 Dto
public class MemberDto {
    private String userId;
    private String name;
    private String password;
}
