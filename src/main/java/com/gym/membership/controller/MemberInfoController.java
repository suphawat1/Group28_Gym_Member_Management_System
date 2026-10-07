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

import com.gym.membership.dto.MemberInfoRequestDTO;
import com.gym.membership.dto.MemberInfoResponseDTO;
import com.gym.membership.service.MemberInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member-infos")
@RequiredArgsConstructor
public class MemberInfoController {
    private final MemberInfoService memberInfoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberInfoResponseDTO createMemberInfo(@Valid @RequestBody MemberInfoRequestDTO dto) {
        return memberInfoService.createMemberInfo(dto);
    }

    @GetMapping
    public List<MemberInfoResponseDTO> getAllMemberInfos() {
        return memberInfoService.getAllMemberInfos();
    }

    @GetMapping("/{id}")
    public MemberInfoResponseDTO getMemberInfoById(@PathVariable Long id) {
        return memberInfoService.getMemberInfoById(id);
    }

    @PutMapping("/{id}")
    public MemberInfoResponseDTO updateMemberInfo(@PathVariable Long id,
            @Valid @RequestBody MemberInfoRequestDTO dto) {
        return memberInfoService.updateMemberInfo(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemberInfo(@PathVariable Long id) {
        memberInfoService.deleteMemberInfo(id);
    }
}
