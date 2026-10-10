# domain model
```mermaid
erDiagram
    TRAINER |o--o{ MEMBER : "ดูแล"
    MEMBER ||--o| MEMBER_INFO : "มีข้อมูลเพิ่มเติม"
    MEMBER ||--o{ MEMBERSHIP : "สมัครสมาชิก"
    MEMBERSHIP_PLAN ||--o{ MEMBERSHIP : "กำหนดแพ็กเกจ"
    TRAINER ||--o{ TRAINING_SESSION : "สอน"
    MEMBER ||--o{ TRAINING_SESSION : "เข้าฝึก"

    TRAINER {
        long trainerId PK
        string name
        string phone
        string specialty
    }
    MEMBER {
        long memberId PK
        string username
        string email
        string password
        string phone
        long trainerId FK "ไม่บังคับ"
    }
    MEMBER_INFO {
        long infoId PK
        long memberId FK "UNIQUE"
        string fullName
        string gender
        date dateOfBirth
        string emergencyContact
    }
    MEMBERSHIP_PLAN {
        long planId PK
        string planName
        decimal price
        int durationDays
    }
    MEMBERSHIP {
        long membershipId PK
        long memberId FK
        long planId FK
        date startDate
        date endDate
    }
    TRAINING_SESSION {
        long sessionId PK
        long trainerId FK
        long memberId FK
        date sessionDate
        time sessionTime
        string status
    }
```

## คำอธิบาย
- สมาชิก (Member) มีเทรนเนอร์ได้ 0 หรือ 1 คน และมีข้อมูลเพิ่มเติม (MemberInfo) ได้ไม่เกิน 1 รายการ
- สมาชิกสมัครได้หลายครั้ง (Membership) แต่ละครั้งผูกกับแพ็กเกจ (MembershipPlan) 1 แพ็กเกจ
- วันหมดอายุ (endDate) = วันเริ่ม (startDate) + จำนวนวันของแพ็กเกจ (durationDays) ระบบคำนวณให้
- การฝึก (TrainingSession) เชื่อมเทรนเนอร์ 1 คนกับสมาชิก 1 คน สถานะเริ่มต้นเป็น BOOKED