package com.gym.membership.pattern.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component("STUDENT")
public class StudentPricing implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        return price.multiply(	new BigDecimal("0.80"));
    }
    
}
