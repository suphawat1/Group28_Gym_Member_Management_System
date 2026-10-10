package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gym.membership.dto.MembershipRequestDTO;
import com.gym.membership.dto.MembershipResponseDTO;
import com.gym.membership.service.MembershipService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/memberships")
@RequiredArgsConstructor
public class MembershipWebController {
    private final MembershipService membershipService;

    // หน้ารายการ: GET /memberships
    @GetMapping
    public String listMemberships(Model model) {
        model.addAttribute("memberships", membershipService.getAllMemberships());
        return "memberships/list";
    }

    // หน้าฟอร์มเปล่า (เพิ่มใหม่): GET /memberships/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("membership", new MembershipRequestDTO());
        model.addAttribute("membershipId", null);
        return "memberships/form";
    }

    // กดบันทึกจากฟอร์มเพิ่มใหม่: POST /memberships
    @PostMapping
    public String createMembership(@Valid @ModelAttribute("membership") MembershipRequestDTO dto,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("membershipId", null);
            return "memberships/form";
        }
        membershipService.createMembership(dto);
        return "redirect:/memberships";
    }

    // หน้าฟอร์มแก้ไข (มีข้อมูลเดิม): GET /memberships/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MembershipResponseDTO membership = membershipService.getMembershipById(id);
        MembershipRequestDTO dto = new MembershipRequestDTO();
        dto.setMemberId(membership.getMemberId());
        dto.setPlanId(membership.getPlanId());
        dto.setStartDate(membership.getStartDate());

        model.addAttribute("membership", dto);
        model.addAttribute("membershipId", id);
        return "memberships/form";
    }

    // กดบันทึกจากฟอร์มแก้ไข: POST /memberships/{id}
    @PostMapping("/{id}")
    public String updateMembership(@PathVariable Long id,
                                   @Valid @ModelAttribute("membership") MembershipRequestDTO dto,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("membershipId", id);
            return "memberships/form";
        }
        membershipService.updateMembership(id, dto);
        return "redirect:/memberships";
    }

    // กดปุ่มลบ: POST /memberships/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteMembership(@PathVariable Long id) {
        membershipService.deleteMembership(id);
        return "redirect:/memberships";
    }
}