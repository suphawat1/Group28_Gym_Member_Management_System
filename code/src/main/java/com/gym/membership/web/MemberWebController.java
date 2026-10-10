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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;
import com.gym.membership.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberWebController {
    private final MemberService memberService;

    // หน้ารายการ: GET /members
    @GetMapping
    public String listMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members/list";
    }

    // หน้าฟอร์มเปล่า (เพิ่มใหม่): GET /members/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("member", new MemberRequestDTO());
        model.addAttribute("memberId", null);
        return "members/form";
    }

    // กดบันทึกจากฟอร์มเพิ่มใหม่: POST /members
    @PostMapping
    public String createMember(@Valid @ModelAttribute("member") MemberRequestDTO dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("memberId", null);
            return "members/form";
        }
        try {
            memberService.createMember(dto);
        } catch (ResponseStatusException ex) {
            if (!addNotFoundError(ex, result)) {
                throw ex;
            }
            model.addAttribute("memberId", null);
            return "members/form";
        }
        return "redirect:/members";
    }

    // หน้าฟอร์มแก้ไข (มีข้อมูลเดิม): GET /members/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MemberResponseDTO member = memberService.getMemberById(id);
        MemberRequestDTO dto = new MemberRequestDTO();
        dto.setUsername(member.getUsername());
        dto.setEmail(member.getEmail());
        dto.setPhone(member.getPhone());
        dto.setTrainerId(member.getTrainerId());
        // ไม่ set password: ResponseDTO ไม่มี password (ไม่เปิดเผยรหัสผ่าน) ผู้ใช้ต้องกรอกใหม่ตอนแก้ไข

        model.addAttribute("member", dto);
        model.addAttribute("memberId", id);
        return "members/form";
    }

    // กดบันทึกจากฟอร์มแก้ไข: POST /members/{id}
    @PostMapping("/{id}")
    public String updateMember(@PathVariable Long id,
                               @Valid @ModelAttribute("member") MemberRequestDTO dto,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("memberId", id);
            return "members/form";
        }
        try {
            memberService.updateMember(id, dto);
        } catch (ResponseStatusException ex) {
            if (!addNotFoundError(ex, result)) {
                throw ex; // เช่น "Member not found" ยังเป็น 404 ตามเดิม
            }
            model.addAttribute("memberId", id);
            return "members/form";
        }
        return "redirect:/members";
    }

    // กดปุ่มลบ: POST /members/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteMember(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            memberService.deleteMember(id);
        } catch (ResponseStatusException ex) {
            if (ex.getStatusCode().value() != 409) {
                throw ex;
            }
            // 409 = Member นี้ยังมีข้อมูลผูกอยู่ ส่งข้อความกลับไปแสดงที่หน้ารายการ
            redirectAttributes.addFlashAttribute("errorMessage",
                    "ลบไม่ได้: สมาชิกนี้ยังมีข้อมูลผูกอยู่ (ข้อมูลส่วนตัว / การสมัครแพ็กเกจ / นัดฝึก) กรุณาลบข้อมูลเหล่านั้นก่อน");
        }
        return "redirect:/members";
    }

    // แปลง ResponseStatusException เป็น error ของช่องในฟอร์ม
    // คืน true ถ้าจัดการได้ (trainer ไม่พบ), false ถ้าเป็นกรณีอื่น
    private boolean addNotFoundError(ResponseStatusException ex, BindingResult result) {
        if ("Trainer not found".equals(ex.getReason())) {
            result.rejectValue("trainerId", "notfound", "ไม่พบ Trainer ID นี้");
            return true;
        }
        return false;
    }
}
