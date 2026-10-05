# Group28_Gym_Member_Management_System
Group28 Gym Member Management System
ระบบจัดการสมาชิกฟิตเนส

## สมาชิกในกลุ่ม

| รหัสนักศึกษา | ชื่อ-นามสกุล | หน้าที่ |
|---|---|---|
| 673380433-3 | นายเกียรติพงษ์ เผดิมศักดิ์ | Core Architecture, DB Design, Template (Trainer), ตรวจและ merge งาน |
| 673380604-2 | นายศุภวัทน์ แสนเรียน | Member + MemberInfo |
| 673380400-8 | ชินวัตร แสนเมือง | Membership |
| 673380037-1 | นายฉัตรดนัย ไกรราช | TrainingSession |
| 673380086-8 | นายภูริ ตั้งพงษ์ | MembershipPlan |

---

## 📅 กำหนดการ (ส่งจริง: อาทิตย์ 11 ต.ค. 2569)

| วัน | เป้าหมาย | ส่งอะไรในกลุ่ม |
|---|---|---|
| **พุธ 7 ต.ค.** | ทำข้อ 1 (Setup) ให้ผ่าน | **รูปหน้า Swagger** ในเครื่องตัวเอง |
| **พฤหัส 8 ต.ค.** | API ของตัวเองเสร็จ ทดสอบผ่านข้อ 5 | ลิงก์ **Pull Request** |
| ศุกร์ 9 ต.ค. | merge งานทุกคนเข้า main + เริ่มงานส่วนอื่น (ตารางล่าง) | |
| เสาร์ 10 ต.ค. | งานส่วนอื่นเสร็จ ทดสอบทั้งระบบ | |
| อาทิตย์ 11 ต.ค. | ซ้อมนำเสนอ + ส่งงาน | |

> ⚠️ **ติด Setup เกิน 1 ชั่วโมง แจ้งในกลุ่มทันที** อย่ารอ

### งานส่วนอื่นที่โจทย์บังคับ (แบ่งหลังงาน API เสร็จ)

| งาน | ผู้รับผิดชอบ |
|---|---|
| Frontend (Thymeleaf หรือ React/Vue/Angular) | _ยังไม่กำหนด_ |
| Unit Test (JUnit5 + Mockito) | _ยังไม่กำหนด_ |
| Docker + Deploy ขึ้น Cloud | _ยังไม่กำหนด_ |
| GoF Design Pattern | _ยังไม่กำหนด_ |
| สไลด์นำเสนอ | _ยังไม่กำหนด_ |

---

## 1. Setup (ทำครั้งแรกครั้งเดียว)

**สิ่งที่ต้องมี:** Java 17 ขึ้นไป, PostgreSQL + pgAdmin, Git, VS Code พร้อม extension **Extension Pack for Java**

เช็ก Java ด้วย `java -version` ต้องได้ 17 ขึ้นไป

1. **Clone repo**
   ```
   git clone https://github.com/suphawat1/Group28_Gym_Member_Management_System.git
   cd Group28_Gym_Member_Management_System
   ```
2. **ตั้งค่า Git ให้ commit นับเป็นของเรา** (ใช้อีเมลเดียวกับบัญชี GitHub)
   ```
   git config user.name "ชื่อของเรา"
   git config user.email "อีเมลที่ใช้กับGitHub"
   ```
3. **สร้าง database** ใน pgAdmin ชื่อ `gym_db`
4. **แก้รหัสผ่าน DB** เปิด `src/main/resources/application.properties` แก้ `username` / `password` ให้ตรงกับ PostgreSQL ในเครื่องตัวเอง
5. **สั่ง Git ไม่ให้สนใจไฟล์ตั้งค่าของเรา** (สำคัญมาก ทำทันทีหลังข้อ 4)
   ```
   git update-index --skip-worktree src/main/resources/application.properties
   ```
   คำสั่งนี้ทำให้ Git มองข้ามการแก้ไขไฟล์นี้ในเครื่องเรา จะได้ไม่เผลอ push รหัสผ่านของเราขึ้นไปทับของคนอื่น
6. **รันแอป**
   ```
   mvnw spring-boot:run
   ```
   เห็น `Started MembershipApplication` = ผ่าน (ตารางทั้ง 6 ถูกสร้างใน `gym_db` อัตโนมัติ) หยุดแอปด้วย `Ctrl + C`
7. **เปิด Swagger:** http://localhost:8080/swagger-ui/index.html → แคปหน้าจอส่งในกลุ่ม ✅

---

## 2. โครงสร้างโปรเจค

```
src/main/java/com/gym/membership/
├── domain/
│   ├── entity/        ← Entity ครบ 6 ตัวแล้ว (ห้ามแก้)
│   └── repository/    ← Repository ครบ 6 ตัวแล้ว (ห้ามแก้)
├── dto/               ← RequestDTO / ResponseDTO
├── mapper/            ← แปลง DTO ↔ Entity
├── service/           ← Service (interface) + ServiceImpl
└── controller/        ← REST API
```

**Flow ของข้อมูล:**
```
JSON → Controller → Service → Mapper → Repository → DB
```

---

## 3. วิธีทำงานของตัวเอง (Copy จาก Trainer)

Entity และ Repository **มีให้แล้ว** แต่ละคนสร้าง **6 ไฟล์** โดย copy จากของ Trainer แล้วเปลี่ยนชื่อ

| ลำดับ | ไฟล์ต้นแบบ | ไฟล์ที่ต้องสร้าง (ตัวอย่าง MembershipPlan) |
|---|---|---|
| 1 | `dto/TrainerRequestDTO.java` | `dto/MembershipPlanRequestDTO.java` |
| 2 | `dto/TrainerResponseDTO.java` | `dto/MembershipPlanResponseDTO.java` |
| 3 | `mapper/TrainerMapper.java` | `mapper/MembershipPlanMapper.java` |
| 4 | `service/TrainerService.java` | `service/MembershipPlanService.java` |
| 5 | `service/TrainerServiceImpl.java` | `service/MembershipPlanServiceImpl.java` |
| 6 | `controller/TrainerController.java` | `controller/MembershipPlanController.java` |

> Member + MemberInfo (ศุภวัทน์) ทำ 6 ไฟล์ × 2 ชุด = 12 ไฟล์

**ทำทีละไฟล์ตามลำดับ** แต่ละไฟล์ให้เปลี่ยน:
- ชื่อ class และ import (`Trainer` → ชื่อ Entity ของตัวเอง)
- field ให้ตรงกับ Entity ของตัวเอง (ดูใน `domain/entity/`)
- ชื่อเมธอด เช่น `createTrainer` → `createMembershipPlan`
- URL ใน Controller เช่น `/api/trainers` → `/api/membership-plans`

### กฎของ DTO
- **RequestDTO** (ข้อมูลขาเข้า) = **ไม่มี** id ของตัวเอง เพราะ DB สร้างให้ ใส่ `@NotBlank` (String) หรือ `@NotNull` (ตัวเลข/วันที่) ให้ field ที่ห้ามว่าง
- **ResponseDTO** (ข้อมูลขาออก) = **มี** id ของตัวเองเสมอ ไม่ต้องมี validation

---

## 4. งานที่มี FK (ศุภวัทน์, ชินวัตร, ฉัตรดนัย อ่านส่วนนี้)

Trainer ไม่มี FK แต่งานของพวกเรามี ต้องทำเพิ่ม 3 จุด:

**① RequestDTO รับ FK เป็นตัวเลข `Long`** (ไม่ใช่ object)
```java
@NotNull
private Long memberId;
@NotNull
private Long planId;
```

**② ResponseDTO ส่ง FK กลับเป็นตัวเลข** (เช่น `memberId`, `planId`) ใน Mapper ใช้
```java
dto.setMemberId(membership.getMember().getMemberId());
```

**③ ServiceImpl ต้องไปหา object จริงมาก่อน save** (Mapper ทำเองไม่ได้ เพราะไม่มี Repository)
```java
@Override
public MembershipResponseDTO createMembership(MembershipRequestDTO dto) {
    Member member = memberRepository.findById(dto.getMemberId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));
    MembershipPlan plan = membershipPlanRepository.findById(dto.getPlanId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));

    Membership membership = membershipMapper.toEntity(dto);  // set ได้แค่ field ธรรมดา
    membership.setMember(member);                            // set FK เอง
    membership.setMembershipPlan(plan);

    Membership saved = membershipRepository.save(membership);
    return membershipMapper.toResponse(saved);
}
```
อย่าลืมเพิ่ม Repository ที่ต้องใช้เป็น `private final` ใน ServiceImpl ด้วย และใน `update` ก็ต้องหา FK แบบเดียวกัน

### หมายเหตุเฉพาะงาน
| งาน | สิ่งที่ต้องระวัง |
|---|---|
| **Member** | `MemberResponseDTO` **ห้ามมี `password`** / `trainerId` ว่างได้ (สมาชิกใหม่ยังไม่มีเทรนเนอร์) ถ้าว่างไม่ต้อง findById |
| **MemberInfo** | 1 Member มีได้แค่ 1 MemberInfo (One-to-One) |
| **Membership** | Request รับแค่ `memberId`, `planId`, `startDate` แล้วให้ Service คำนวณ `endDate = startDate.plusDays(plan.getDurationDays())` |
| **TrainingSession** | Request ไม่ต้องรับ `status` ให้ Service ตั้งเป็น `"BOOKED"` เองตอนสร้าง |
| **MembershipPlan** | ไม่มี FK ทำเหมือน Trainer ได้เลย / `price` ใช้ `BigDecimal` |

---

## 5. ทดสอบงานใน Swagger

### ⚠️ งานที่มี FK: เพิ่มข้อมูลทดสอบก่อน (ไม่ต้องรอเพื่อน)
งาน Membership และ TrainingSession ต้องมี Member / MembershipPlan / Trainer อยู่ใน DB ก่อน แต่ API ของเพื่อนอาจยังไม่เสร็จ ให้เพิ่มข้อมูลตรงๆ ด้วย SQL

pgAdmin → คลิกขวา `gym_db` → **Query Tool** → วางแล้วกด ▶ (F5)
```sql
INSERT INTO trainer (name, phone, specialty)
VALUES ('โค้ชทดสอบ', '0800000000', 'เวท');

INSERT INTO member (username, email, password, phone, trainer_id)
VALUES ('testuser', 'test@gym.com', '1234', '0811111111', NULL);

INSERT INTO membership_plan (plan_name, price, duration_days)
VALUES ('รายเดือน', 1500.00, 30);

SELECT * FROM trainer;
SELECT * FROM member;
SELECT * FROM membership_plan;
```
3 บรรทัด `SELECT` สุดท้ายจะโชว์ **id** ที่ได้ เอา id เหล่านี้ไปใส่ตอนทดสอบใน Swagger (เช่น `"memberId": 1`)

### ขั้นตอนทดสอบ
เปิด http://localhost:8080/swagger-ui/index.html → เลือก endpoint → **Try it out** → **Execute**

ทุกคนต้องทดสอบให้ผ่าน 3 กรณีนี้ **ก่อนเปิด Pull Request**:

| ทดสอบ | ควรได้ |
|---|---|
| สร้างด้วยข้อมูลถูกต้อง | **201** + มี id กลับมา |
| สร้างด้วย field บังคับว่าง | **400** |
| GET ด้วย id ที่ไม่มี (เช่น 99) | **404** |

---

## 6. กติกา Git (แต่ละคนต้อง commit อย่างน้อย 15 ครั้ง)

> 🔒 **main ถูกล็อกไว้** push ตรงไม่ได้ ต้องผ่าน Pull Request เท่านั้น และ **เกียรติพงษ์เป็นคนตรวจ + merge**

1. **สร้าง branch ของตัวเอง** (ทำครั้งเดียว ก่อนเริ่มเขียนโค้ด)
   ```
   git checkout main
   git pull
   git checkout -b <รหัสนักศึกษา>-<ชื่องาน>
   ```
   ตัวอย่าง: `git checkout -b 673380086-8-membership-plan`

2. **ก่อน commit ทุกครั้ง ดู `git status` ก่อน** ต้องเห็นเฉพาะไฟล์งานของตัวเอง ถ้าเห็นไฟล์อื่น (เช่นของคนอื่น หรือ `pom.xml`) **ห้าม add**

3. **Commit ทุกครั้งที่ทำเสร็จ 1 ไฟล์** (งานหลักได้ 6+ commit แล้ว)
   ```
   git add src/main/java/com/gym/membership/dto/MembershipPlanRequestDTO.java
   git commit -m "Add MembershipPlanRequestDTO"
   ```
   ข้อความ commit: ภาษาอังกฤษ ขึ้นต้นด้วยกริยา (`Add`, `Fix`, `Update`) ห้าม `git add .` (มันหยิบทุกไฟล์)

4. **Push branch ของตัวเอง**
   ```
   git push -u origin <ชื่อ branch>
   ```
   ครั้งต่อไปพิมพ์แค่ `git push`

5. **ทดสอบผ่านข้อ 5 แล้ว** → เข้า GitHub → กด **Compare & pull request** → เลือก base: `main` → **Create pull request** → ส่งลิงก์ในกลุ่ม

6. **ถ้าเพื่อนให้แก้** แก้ในเครื่อง → commit → push branch เดิม PR จะอัปเดตเอง

> ⚠️ ใช้บัญชี GitHub ของตัวเองเสมอ ไม่งั้น commit จะไม่ถูกนับเป็นของเรา

### สำหรับเจ้าของ repo (ศุภวัทน์): ล็อก main (ทำครั้งเดียว)
GitHub → repo → **Settings** → **Branches** → **Add branch protection rule** (หรือ **Add classic branch protection rule**)
- Branch name pattern: `main`
- ✅ ติ๊ก **Require a pull request before merging**
- กด **Create / Save**

---

## 7. ใช้ AI (Claude/ChatGPT) ช่วยทำงาน

ใช้ได้ แต่ **ต้องใช้ Prompt ด้านล่างนี้เท่านั้น** เพื่อให้ AI ทำตาม Template เดียวกันทั้งกลุ่ม และอธิบายให้เราเข้าใจ (อาจารย์ถามตอนนำเสนอ ต้องตอบได้)

### ขั้นตอน
1. เปิดแชทใหม่
2. แนบไฟล์เหล่านี้:
   - `README.md` (ไฟล์นี้)
   - ไฟล์ Template ของ Trainer ทั้ง 6 ไฟล์ (`TrainerRequestDTO`, `TrainerResponseDTO`, `TrainerMapper`, `TrainerService`, `TrainerServiceImpl`, `TrainerController`)
   - ไฟล์ Entity ของงานตัวเอง (จาก `domain/entity/`)
3. Copy Prompt ด้านล่าง แก้ส่วน `[...]` แล้วส่ง

### Prompt สำเร็จรูป

```
ผมเป็นสมาชิกกลุ่มทำโปรเจค Spring Boot ระบบจัดการสมาชิกฟิตเนส
งานที่ผมรับผิดชอบคือ: [ชื่องาน เช่น MembershipPlan]

ผมแนบ README, ไฟล์ Template ของ Trainer 6 ไฟล์ และ Entity ของงานผมมาให้แล้ว
ช่วยพาผมทำงานตามกติกานี้:

1. ทำตาม pattern ของไฟล์ Trainer ทุกอย่าง (โครงสร้าง, annotation, การตั้งชื่อ, โฟลเดอร์)
   ห้ามใช้วิธีอื่น เช่น ห้ามใช้ @Data, ห้ามใช้ record, ห้ามเปลี่ยนโครงโฟลเดอร์
2. ห้ามแก้ไฟล์ Entity, Repository, pom.xml, application.properties
   และห้ามแตะไฟล์ของงานคนอื่น
3. โปรเจคใช้ Spring Boot 4.1.1, Java 17, springdoc-openapi 3.1.1 (ติดตั้งแล้ว ไม่ต้องเพิ่ม)
4. ถ้างานผมมี FK ให้ทำตามข้อ 4 ใน README
   และทำตาม "หมายเหตุเฉพาะงาน" ของงานผม
5. ทำทีละไฟล์ตามลำดับในข้อ 3 ของ README
   แต่ละไฟล์ให้:
   - อธิบายก่อนว่าไฟล์นี้ทำหน้าที่อะไรในระบบ (ภาษาไทย เข้าใจง่าย)
   - อธิบายว่าแต่ละส่วนในโค้ดทำอะไร และต่างจากของ Trainer ตรงไหน เพราะอะไร
   - บอกคำสั่ง git add และ git commit สำหรับไฟล์นั้น (ห้ามใช้ git add .)
   - รอผมบอกว่า "เสร็จแล้ว" ก่อนไปไฟล์ถัดไป
6. จบแล้วพาผมทดสอบใน Swagger ตามข้อ 5 ของ README
   และพาเปิด Pull Request ตามข้อ 6

เริ่มจากไฟล์แรกได้เลย
```

> 💡 **ถ้า AI ทำต่างจากของ Trainer** ให้ตอบกลับไปว่า "ทำให้เหมือนของ Trainer" ทันที
>
> 💡 **ตอบคำถามเหล่านี้ให้ได้ก่อนนำเสนอ:** DTO มีไว้ทำไม? Mapper ทำอะไร? ทำไม Service ต้องมี interface? 201 / 400 / 404 คืออะไร?

---

## ติดปัญหา?
1. เทียบกับไฟล์ของ Trainer ก่อน (90% ของปัญหาคือเปลี่ยนชื่อไม่ครบ)
2. ดู error ใน terminal บรรทัดที่ขึ้นต้นด้วย `Caused by:`
3. ถามในกลุ่ม แนบ error มาด้วย
