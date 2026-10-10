package com.gym.membership.domain.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Index: trainer_id, member_id ใช้ JOIN (ตารางฝึกของเทรนเนอร์/ของสมาชิก)
//        session_date ใช้ดูตารางฝึกรายวัน
@Entity
@Table(name = "training_session", indexes = {
        @Index(name = "idx_training_session_trainer_id", columnList = "trainer_id"),
        @Index(name = "idx_training_session_member_id", columnList = "member_id"),
        @Index(name = "idx_training_session_date", columnList = "session_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;

    // Fetch LAZY: ดึง Trainer/Member เมื่อต้องใช้เท่านั้น ไม่ดึงทุกครั้งที่โหลดรายการการฝึก
    // Cascade ไม่ใส่: การจองฝึกเป็นประวัติ ไม่ควรไปสร้าง/ลบ Trainer หรือ Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id",nullable = false)
    private Trainer trainerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member memberId;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime sessionTime;

    @Column(nullable = false)
    private String status;
}