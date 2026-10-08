package com.gym.membership.pattern.strategy;

import java.math.BigDecimal;

public class StudentPricing implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        return price.multiply(	new BigDecimal("0.80"));
    }
    
}
