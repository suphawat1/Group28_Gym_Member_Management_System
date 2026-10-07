package com.gym.membership.dto;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class MembershipPlanResponseDTO {

    private Long planId;
    private String planName;
    private BigDecimal price;
    private Integer durationDays;
}