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
public class MembershipResponseDTO {
    private Long membershipId;
    private Long memberId;
    private Long planId;
    private LocalDate startDate;
    private LocalDate endDate;
}