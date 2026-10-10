package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gym.membership.dto.MembershipPlanRequestDTO;
import com.gym.membership.dto.MembershipPlanResponseDTO;
import com.gym.membership.service.MembershipPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/membership-plans")
@RequiredArgsConstructor
public class MembershipPlanWebController {
    private final MembershipPlanService planService;

    @GetMapping
    public String listPlans(Model model) {
        model.addAttribute("plans", planService.getAllMembershipPlans());
        return "membership-plans/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("plan", new MembershipPlanRequestDTO());
        model.addAttribute("planId", null);
        return "membership-plans/form";
    }

    @PostMapping
    public String createPlan(@Valid @ModelAttribute("plan") MembershipPlanRequestDTO dto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("planId", null);
            return "membership-plans/form";
        }
        planService.createMembershipPlan(dto);
        return "redirect:/membership-plans";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MembershipPlanResponseDTO plan = planService.getMembershipPlanById(id);
        MembershipPlanRequestDTO dto = new MembershipPlanRequestDTO();
        dto.setPlanName(plan.getPlanName());
        dto.setPrice(plan.getPrice());
        dto.setDurationDays(plan.getDurationDays());

        model.addAttribute("plan", dto);
        model.addAttribute("planId", id);
        return "membership-plans/form";
    }

    @PostMapping("/{id}")
    public String updatePlan(@PathVariable Long id,
                             @Valid @ModelAttribute("plan") MembershipPlanRequestDTO dto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("planId", id);
            return "membership-plans/form";
        }
        planService.updateMembershipPlan(id, dto);
        return "redirect:/membership-plans";
    }

    @PostMapping("/{id}/delete")
    public String deletePlan(@PathVariable Long id) {
        planService.deleteMembershipPlan(id);
        return "redirect:/membership-plans";
    }
}