package com.gym.membership.domain.entity;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Index: member_id เป็น UNIQUE อยู่แล้ว PostgreSQL สร้าง index ให้อัตโนมัติ จึงไม่เพิ่มซ้ำ
@Entity
@Table(name = "member_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long infoId;

    // Fetch LAZY: ดึงข้อมูลสมาชิกเมื่อต้องใช้เท่านั้น (ค่าเริ่มต้นของ @OneToOne คือ EAGER)
    // Cascade ไม่ใส่: MemberInfo เป็นฝั่งลูก การลบ/บันทึกข้อมูลเพิ่มเติมต้องไม่กระทบ Member
    //                 การลบ Member ที่ยังมี MemberInfo ยังถูกกันไว้เหมือนเดิม (RESTRICT)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    private Member member;
    @Column(nullable = false)
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String emergencyContact;
}