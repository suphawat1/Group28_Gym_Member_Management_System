package com.gym.membership.controller;

import java.math.BigDecimal;

import com.gym.membership.pattern.strategy.PricingService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/pricing")
@RequiredArgsConstructor

public class PricingController {
    private final PricingService pricingService;

    @GetMapping
    public BigDecimal getPrice(@RequestParam Long planId, @RequestParam String customerType) {
        return pricingService.calculatePrice(planId, customerType);
    }
}
