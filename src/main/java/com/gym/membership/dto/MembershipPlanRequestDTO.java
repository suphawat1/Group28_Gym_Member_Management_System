package com.gym.membership.dto;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPlanRequestDTO {
    @NotBlank
    private String planName;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer durationDays;
}