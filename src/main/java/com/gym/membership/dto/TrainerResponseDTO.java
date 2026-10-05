package com.gym.membership.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TrainerResponseDTO {
    
    private Long trainerId;
    private String name;
    private String phone;
    private String specialty;
}
