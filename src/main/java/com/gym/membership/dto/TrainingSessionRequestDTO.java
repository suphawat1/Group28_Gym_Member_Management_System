package com.gym.membership.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSessionRequestDTO {
    
@ NotNull 
     private LocalTime sessionTime;
@ NotNull
    private LocalDate sessionDate;
@ NotNull
    private Long trainerId;   
@ NotNull
    private Long memberId;    
}
