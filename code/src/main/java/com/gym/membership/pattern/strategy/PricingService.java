package com.gym.membership.pattern.strategy;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.domain.repository.MembershipPlanRepository;
import com.gym.membership.domain.entity.MembershipPlan;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PricingService {
    private final Map<String, PricingStrategy> strategies;
    private final MembershipPlanRepository membershipPlanRepository;

    public BigDecimal calculatePrice(Long planId, String customerType) {
        MembershipPlan plan = membershipPlanRepository.findById(planId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        PricingStrategy strategy = strategies.get(customerType);
        if (strategy == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown customer type");
        }
        
        return strategy.calculatePrice(plan.getPrice());
    }
}
