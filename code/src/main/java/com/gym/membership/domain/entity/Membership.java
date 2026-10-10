package com.gym.membership.domain.entity;

import java.time.LocalDate;

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

// Index: member_id, plan_id ใช้ JOIN (ดูการเป็นสมาชิกของแต่ละคน/แต่ละแพ็กเกจ)
//        end_date ใช้ค้นสมาชิกที่ใกล้หมดอายุหรือหมดอายุแล้ว
@Entity
@Table(name = "membership", indexes = {
        @Index(name = "idx_membership_member_id", columnList = "member_id"),
        @Index(name = "idx_membership_plan_id", columnList = "plan_id"),
        @Index(name = "idx_membership_end_date", columnList = "end_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "membership_id")
    private Long membershipId;

    // Fetch LAZY: รายการการเป็นสมาชิกมีเยอะ ถ้า EAGER จะดึง Member และ Plan ของทุกแถวมาด้วยโดยไม่จำเป็น
    // Cascade ไม่ใส่: Membership เป็นประวัติการสมัคร ไม่ควรไปสร้าง/ลบ Member หรือ Plan
    //                 การลบ Member/Plan ที่ยังมีประวัติการสมัครจึงถูกกันไว้ (RESTRICT) เพื่อไม่ให้ประวัติหาย
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private MembershipPlan membershipPlan;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
}