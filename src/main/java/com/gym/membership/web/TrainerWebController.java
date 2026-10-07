package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gym.membership.dto.TrainerRequestDTO;
import com.gym.membership.dto.TrainerResponseDTO;
import com.gym.membership.service.TrainerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/trainers")
@RequiredArgsConstructor
public class TrainerWebController {
    private final TrainerService trainerService;

    // หน้ารายการ: GET /trainers
    @GetMapping
    public String listTrainers(Model model) {
        model.addAttribute("trainers", trainerService.getAllTrainers());
        return "trainers/list";
    }

    // หน้าฟอร์มเปล่า (เพิ่มใหม่): GET /trainers/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("trainer", new TrainerRequestDTO());
        model.addAttribute("trainerId", null);
        return "trainers/form";
    }

    // กดบันทึกจากฟอร์มเพิ่มใหม่: POST /trainers
    @PostMapping
    public String createTrainer(@Valid @ModelAttribute("trainer") TrainerRequestDTO dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainerId", null);
            return "trainers/form";
        }
        trainerService.createTrainer(dto);
        return "redirect:/trainers";
    }

    // หน้าฟอร์มแก้ไข (มีข้อมูลเดิม): GET /trainers/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        TrainerResponseDTO trainer = trainerService.getTrainerById(id);
        TrainerRequestDTO dto = new TrainerRequestDTO();
        dto.setName(trainer.getName());
        dto.setPhone(trainer.getPhone());
        dto.setSpecialty(trainer.getSpecialty());

        model.addAttribute("trainer", dto);
        model.addAttribute("trainerId", id);
        return "trainers/form";
    }

    // กดบันทึกจากฟอร์มแก้ไข: POST /trainers/{id}
    @PostMapping("/{id}")
    public String updateTrainer(@PathVariable Long id,
                                @Valid @ModelAttribute("trainer") TrainerRequestDTO dto,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainerId", id);
            return "trainers/form";
        }
        trainerService.updateTrainer(id, dto);
        return "redirect:/trainers";
    }

    // กดปุ่มลบ: POST /trainers/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteTrainer(@PathVariable Long id) {
        trainerService.deleteTrainer(id);
        return "redirect:/trainers";
    }
}