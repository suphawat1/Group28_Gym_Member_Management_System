package com.gym.membership.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gym.membership.dto.TrainingSessionRequestDTO;
import com.gym.membership.dto.TrainingSessionResponseDTO;
import com.gym.membership.service.TrainingSessionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/training-sessions")
@RequiredArgsConstructor
public class TrainingSessionWebController {

    private final TrainingSessionService trainingSessionService;

    // หน้ารายการ: GET /training-sessions
    @GetMapping
    public String listTrainingSessions(Model model) {
        model.addAttribute("trainingSessions", trainingSessionService.getAllTrainingSessions());
        return "training-sessions/list";
    }

    // หน้าฟอร์มเปล่า (เพิ่มใหม่): GET /training-sessions/new
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("trainingSession", new TrainingSessionRequestDTO());
        model.addAttribute("trainingSessionId", null);
        return "training-sessions/form";
    }

    // กดบันทึกจากฟอร์มเพิ่มใหม่: POST /training-sessions
    @PostMapping
    public String createTrainingSession(@Valid @ModelAttribute("trainingSession") TrainingSessionRequestDTO dto,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainingSessionId", null);
            return "training-sessions/form";
        }
        trainingSessionService.createTrainingSession(dto);
        return "redirect:/training-sessions";
    }

    // หน้าฟอร์มแก้ไข (มีข้อมูลเดิม): GET /training-sessions/{id}/edit
    @GetMapping("/{id}/edit")
public String showEditForm(@PathVariable Long id, Model model) {
    TrainingSessionResponseDTO session = trainingSessionService.getTrainingSessionById(id);
    TrainingSessionRequestDTO dto = new TrainingSessionRequestDTO();
    dto.setSessionTime(session.getSessionTime());
    dto.setSessionDate(session.getSessionDate());
    dto.setTrainerId(session.getTrainerId());
    dto.setMemberId(session.getMemberId());

    model.addAttribute("trainingSession", dto);
    model.addAttribute("trainingSessionId", id);
    return "training-sessions/form";
}

    // กดบันทึกจากฟอร์มแก้ไข: POST /training-sessions/{id}
    @PostMapping("/{id}")
    public String updateTrainingSession(@PathVariable Long id,
                                        @Valid @ModelAttribute("trainingSession") TrainingSessionRequestDTO dto,
                                        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainingSessionId", id);
            return "training-sessions/form";
        }
        trainingSessionService.updateTrainingSession(id, dto);
        return "redirect:/training-sessions";
    }

    // กดปุ่มลบ: POST /training-sessions/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteTrainingSession(@PathVariable Long id) {
        trainingSessionService.deleteTrainingSession(id);
        return "redirect:/training-sessions";
    }
}
