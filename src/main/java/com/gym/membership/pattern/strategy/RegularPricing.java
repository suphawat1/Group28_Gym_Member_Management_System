package com.gym.membership.pattern.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component("REGULAR")
public class RegularPricing implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal price) {
        return price;
    }

}
