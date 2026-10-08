package com.gym.membership.pattern.strategy;

import java.math.BigDecimal;

public class RegularPricing implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        return price;
    }
    
}
