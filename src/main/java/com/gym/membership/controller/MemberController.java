package com.gym.membership.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gym.membership.dto.MemberRequestDTO;
import com.gym.membership.dto.MemberResponseDTO;
import com.gym.membership.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponseDTO createMember(@Valid @RequestBody MemberRequestDTO dto) {
        return memberService.createMember(dto);
    }

    @GetMapping
    public List<MemberResponseDTO> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public MemberResponseDTO getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    @PutMapping("/{id}")
    public MemberResponseDTO updateMember(@PathVariable Long id, @Valid @RequestBody MemberRequestDTO dto) {
        return memberService.updateMember(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
    }
}
