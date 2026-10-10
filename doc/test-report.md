# Test Report

- โปรเจกต์: Group28 Gym Member Management System
- วันที่ทดสอบ: 2026-10-10
- คำสั่ง: `cd code && ./mvnw test -Dtest='*ServiceImplTest'`
- สภาพแวดล้อม: Java 21, Spring Boot 4.1.1, JUnit 5 + Mockito

## ผลการทดสอบ

| Test Class | Tests | Failures | Errors | Skipped |
|---|---|---|---|---|
| MemberServiceImplTest | 14 | 0 | 0 | 0 |
| MembershipServiceImplTest | 9 | 0 | 0 | 0 |
| MembershipPlanServiceImplTest | 7 | 0 | 0 | 0 |
| TrainerServiceImplTest | 7 | 0 | 0 | 0 |
| MemberInfoServiceImplTest | 12 | 0 | 0 | 0 |
| TrainingSessionServiceImplTest | 8 | 0 | 0 | 0 |
| **รวม** | **57** | **0** | **0** | **0** |

ผลลัพธ์: **BUILD SUCCESS** (ผ่าน 57/57)

## หมายเหตุ
- การรัน `./mvnw test` ทั้งโปรเจกต์ มี `MembershipApplicationTests` (context load) เพิ่ม 1 เทสต์ รวมเป็น 58 ผ่านทั้งหมด
- Log ดิบอยู่ที่ `doc/test-run.log`
