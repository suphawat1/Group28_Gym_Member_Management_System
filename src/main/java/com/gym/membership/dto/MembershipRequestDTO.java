package com.gym.membership.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequestDTO {
    @NotNull
    private Long memberId;
    @NotNull
    private Long planId;
    @NotNull
    private LocalDate startDate;
}