package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;
import com.gym.membership.service.MemberInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/member-infos")
@RequiredArgsConstructor
public class MemberInfoWebController {
    private final MemberInfoService memberInfoService;

    // หน้ารายการ: GET /member-infos
    @GetMapping
    public String listMemberInfos(Model model) {
        model.addAttribute("memberInfos", memberInfoService.getAllMemberInfos());
        return "memberinfos/list";
    }

    // หน้าฟอร์มเปล่า: GET /member-infos/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("memberInfo", new MemberInfoRequestDTO());
        model.addAttribute("infoId", null);
        return "memberinfos/form";
    }

    // บันทึกเพิ่มใหม่: POST /member-infos
    @PostMapping
    public String createMemberInfo(@Valid @ModelAttribute("memberInfo") MemberInfoRequestDTO dto,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("infoId", null);
            return "memberinfos/form";
        }
        try {
            memberInfoService.createMemberInfo(dto);
        } catch (ResponseStatusException ex) {
            if (!addFieldError(ex, result)) {
                throw ex;
            }
            model.addAttribute("infoId", null);
            return "memberinfos/form";
        }
        return "redirect:/member-infos";
    }

    // หน้าฟอร์มแก้ไข: GET /member-infos/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MemberInfoResponseDTO info = memberInfoService.getMemberInfoById(id);
        MemberInfoRequestDTO dto = new MemberInfoRequestDTO();
        dto.setMemberId(info.getMemberId());
        dto.setFullName(info.getFullName());
        dto.setGender(info.getGender());
        dto.setDateOfBirth(info.getDateOfBirth());
        dto.setEmergencyContact(info.getEmergencyContact());

        model.addAttribute("memberInfo", dto);
        model.addAttribute("infoId", id);
        return "memberinfos/form";
    }

    // บันทึกแก้ไข: POST /member-infos/{id}
    @PostMapping("/{id}")
    public String updateMemberInfo(@PathVariable Long id,
                                   @Valid @ModelAttribute("memberInfo") MemberInfoRequestDTO dto,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("infoId", id);
            return "memberinfos/form";
        }
        try {
            memberInfoService.updateMemberInfo(id, dto);
        } catch (ResponseStatusException ex) {
            if (!addFieldError(ex, result)) {
                throw ex; // เช่น "MemberInfo not found" ยังเป็น 404 ตามเดิม
            }
            model.addAttribute("infoId", id);
            return "memberinfos/form";
        }
        return "redirect:/member-infos";
    }

    // ลบ: POST /member-infos/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteMemberInfo(@PathVariable Long id) {
        memberInfoService.deleteMemberInfo(id);
        return "redirect:/member-infos";
    }

    // แปลง error จาก Service เป็น error ของช่อง memberId ในฟอร์ม
    private boolean addFieldError(ResponseStatusException ex, BindingResult result) {
        if ("Member not found".equals(ex.getReason())) {
            result.rejectValue("memberId", "notfound", "ไม่พบ Member ID นี้");
            return true;
        }
        if ("Member already has MemberInfo".equals(ex.getReason())) {
            result.rejectValue("memberId", "duplicate", "Member นี้มี MemberInfo อยู่แล้ว");
            return true;
        }
        return false;
    }
}
