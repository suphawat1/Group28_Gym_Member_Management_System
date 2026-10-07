package com.gym.membership.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDTO {
    @NotBlank
    private String username;
    private String email;
    @NotBlank
    private String password;
    private String phone;
    private Long trainerId;
}
