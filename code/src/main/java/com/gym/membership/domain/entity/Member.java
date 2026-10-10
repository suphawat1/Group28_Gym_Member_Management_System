package com.gym.membership.domain.entity;

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

// Index: trainer_id ใช้ JOIN/ค้นหาสมาชิกตามเทรนเนอร์ (PostgreSQL ไม่สร้าง index ให้ FK เอง)
//        username ใช้ค้นหาตอนเข้าสู่ระบบ
@Entity
@Table(name = "member", indexes = {
        @Index(name = "idx_member_trainer_id", columnList = "trainer_id"),
        @Index(name = "idx_member_username", columnList = "username")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    // 1. Fields / ตัวแปรควบคุมข้อมูล
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false)
    private String username;
    private String email;
    @Column(nullable = false)
    private String password;
    private String phone;

    // Fetch LAZY: ค่าเริ่มต้นของ @ManyToOne คือ EAGER (ดึงเทรนเนอร์มาทุกครั้งที่โหลดสมาชิก)
    //             แต่หลายหน้าไม่ได้ใช้ข้อมูลเทรนเนอร์ จึงดึงเมื่อต้องใช้เท่านั้น
    // Cascade ไม่ใส่: เทรนเนอร์มีชีวิตของตัวเอง การบันทึก/ลบสมาชิกต้องไม่ไปบันทึก/ลบเทรนเนอร์
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}