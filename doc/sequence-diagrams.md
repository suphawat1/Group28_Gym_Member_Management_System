# Sequence Diagrams — Gym Membership Management System

ลำดับการทำงานของ 3 flow หลักของระบบ ตั้งแต่ Client ส่ง request จนได้ response
(ชื่อคลาสและเมธอดตรงกับโค้ดใน `code/src/main/java/com/gym/membership/`)

## 1. สมัครแพ็กเกจสมาชิก (คำนวณวันหมดอายุอัตโนมัติ)

`POST /api/memberships`

```mermaid
sequenceDiagram
    actor C as Client
    participant MC as MembershipController
    participant MS as MembershipServiceImpl
    participant MR as MemberRepository
    participant PR as MembershipPlanRepository
    participant MM as MembershipMapper
    participant MSR as MembershipRepository

    C->>MC: POST /api/memberships (memberId, planId, startDate)
    MC->>MC: @Valid ตรวจข้อมูล
    MC->>MS: createMembership(dto)
    MS->>MR: findById(memberId)
    alt ไม่พบสมาชิก
        MR-->>MS: Optional.empty
        MS-->>C: 404 "Member not found"
    end
    MR-->>MS: Member
    MS->>PR: findById(planId)
    alt ไม่พบแพ็กเกจ
        PR-->>MS: Optional.empty
        MS-->>C: 404 "Plan not found"
    end
    PR-->>MS: MembershipPlan
    MS->>MM: toEntity(dto)
    MM-->>MS: Membership
    MS->>MS: setMember(), setMembershipPlan()
    MS->>MS: endDate = startDate + durationDays
    MS->>MSR: save(membership)
    MSR-->>MS: savedMembership
    MS->>MM: toResponse(savedMembership)
    MM-->>MS: MembershipResponseDTO
    MS-->>MC: MembershipResponseDTO
    MC-->>C: 201 Created
```

## 2. คำนวณราคาตามประเภทลูกค้า (Strategy Pattern)

`GET /api/pricing?planId={id}&customerType={REGULAR|STUDENT|LOYAL}`

```mermaid
sequenceDiagram
    actor C as Client
    participant PC as PricingController
    participant PS as PricingService
    participant PR as MembershipPlanRepository
    participant S as PricingStrategy

    C->>PC: GET /api/pricing?planId=1&customerType=STUDENT
    PC->>PS: calculatePrice(planId, customerType)
    PS->>PR: findById(planId)
    alt ไม่พบแพ็กเกจ
        PR-->>PS: Optional.empty
        PS-->>C: 404 "Plan not found"
    end
    PR-->>PS: MembershipPlan
    PS->>PS: strategies.get(customerType)
    alt ไม่มีประเภทลูกค้านี้
        PS-->>C: 400 "Unknown customer type"
    end
    Note over PS,S: ได้ StudentPricing จาก Map (ไม่ใช้ if-else)
    PS->>S: calculatePrice(plan.price)
    S-->>PS: price × 0.80
    PS-->>PC: BigDecimal
    PC-->>C: 200 OK (ราคาหลังส่วนลด)
```

## 3. จบนัดฝึก (State Pattern + Observer Pattern)

`POST /api/training-sessions/{id}/complete`

```mermaid
sequenceDiagram
    actor C as Client
    participant TC as TrainingSessionController
    participant SS as SessionStatusService
    participant TR as TrainingSessionRepository
    participant ST as SessionState
    participant TN as TrainerNotifier
    participant MN as MemberNotifier
    participant TM as TrainingSessionMapper

    C->>TC: POST /api/training-sessions/1/complete
    TC->>SS: completeSession(1)
    SS->>TR: findById(1)
    alt ไม่พบนัด
        TR-->>SS: Optional.empty
        SS-->>C: 404 "Session not found"
    end
    TR-->>SS: TrainingSession (status = BOOKED)
    SS->>SS: states.get("BOOKED")
    Note over SS,ST: ได้ BookedState จาก Map
    SS->>ST: complete()
    alt สถานะเป็น COMPLETED หรือ CANCELLED อยู่แล้ว
        ST-->>C: 400 เช่น "Session already completed"
    end
    ST-->>SS: "COMPLETED"
    SS->>SS: session.setStatus("COMPLETED")
    SS->>TR: save(session)
    TR-->>SS: savedSession
    SS->>SS: notifyObservers(savedSession)
    SS->>TN: onStatusChanged(savedSession)
    TN-->>SS: พิมพ์ [Trainer] ...
    SS->>MN: onStatusChanged(savedSession)
    MN-->>SS: พิมพ์ [Member] ...
    SS->>TM: toResponse(savedSession)
    TM-->>SS: TrainingSessionResponseDTO
    SS-->>TC: TrainingSessionResponseDTO
    TC-->>C: 200 OK (status = COMPLETED)
```

หมายเหตุ: error ทุกกรณี (404 / 400) ถูก `GlobalExceptionHandler` แปลงเป็น JSON รูปแบบเดียวกัน (`timestamp`, `status`, `error`, `message`, `path`)
