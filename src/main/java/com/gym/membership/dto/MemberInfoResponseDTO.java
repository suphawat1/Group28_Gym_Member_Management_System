package com.gym.membership.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDTO {

    private Long infoId;
    private Long memberId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String emergencyContact;
}
