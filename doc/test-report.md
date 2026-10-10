# Test Report

- โปรเจกต์: Group28 Gym Member Management System
- วันที่ทดสอบ: 2026-10-10
- คำสั่ง: `cd code && ./mvnw test -Dtest='*ServiceImplTest'`
- สภาพแวดล้อม: Java 21, Spring Boot 4.1.1, JUnit 5 + Mockito

## ผลการทดสอบ

| Test Class | Tests | Failures | Errors | Skipped |
|---|---|---|---|---|
| MemberServiceImplTest | 16 | 0 | 0 | 0 |
| MembershipServiceImplTest | 9 | 0 | 0 | 0 |
| MembershipPlanServiceImplTest | 7 | 0 | 0 | 0 |
| TrainerServiceImplTest | 7 | 0 | 0 | 0 |
| MemberInfoServiceImplTest | 12 | 0 | 0 | 0 |
| TrainingSessionServiceImplTest | 8 | 0 | 0 | 0 |
| **รวม** | **59** | **0** | **0** | **0** |

ผลลัพธ์: **BUILD SUCCESS** (ผ่าน 59/59)

## หมายเหตุ
- การรัน `./mvnw test` ทั้งโปรเจกต์ มี `MembershipApplicationTests` (context load) เพิ่ม 1 เทสต์ รวมเป็น 60 ผ่านทั้งหมด
- เทสต์ที่เพิ่มล่าสุด: `deleteMember_stillHasMembership_throws409` และ `deleteMember_stillHasTrainingSession_throws409` (ลบสมาชิกที่ยังมีการสมัครแพ็กเกจหรือนัดฝึกไม่ได้ ต้องได้ 409)
