package com.gym.membership.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {

    private Long memberId;
    private String username;
    private String email;
    private String phone;
    private Long trainerId;
}
