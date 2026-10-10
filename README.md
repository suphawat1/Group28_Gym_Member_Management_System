# ระบบจัดการสมาชิกฟิตเนส (Gym Membership Management System)
 
ระบบเว็บแอปพลิเคชันสำหรับฟิตเนส ใช้จัดการข้อมูลเทรนเนอร์ สมาชิก ข้อมูลส่วนตัวสมาชิก แพ็กเกจสมาชิก การสมัครแพ็กเกจ และการนัดฝึกกับเทรนเนอร์ไว้ในที่เดียว
ระบบคำนวณวันหมดอายุสมาชิกให้อัตโนมัติ คำนวณราคาแพ็กเกจตามประเภทลูกค้า (ทั่วไป / นักศึกษา / ลูกค้าประจำ) และควบคุมสถานะนัดฝึก (BOOKED → COMPLETED / CANCELLED) พร้อมแจ้งเตือนเทรนเนอร์และสมาชิกเมื่อสถานะเปลี่ยน
ให้บริการผ่านทั้ง REST API (มีเอกสาร Swagger) และหน้าเว็บ Thymeleaf
พัฒนาด้วย Spring Boot ตามสถาปัตยกรรมแบบ Layered Architecture, หลัก SOLID และ GoF Design Patterns กลุ่ม Behavioral
 
รายวิชา CP353002 Principles of Software Design and Development — Group 28
 
## สมาชิกกลุ่ม
 
| ลำดับ | ชื่อ-นามสกุล | รหัสนักศึกษา | Section | Branch | หน้าที่รับผิดชอบ |
| --- | --- | --- | --- | --- | --- |
| 1 | เกียรติพงษ์ เผดิมศักดิ์ | 673380433-3 | 04 | `kiattiphong_6733804333_04` | หัวหน้าทีม, โครงสร้างโปรเจค, Entity/Repository, Trainer (ทุกชั้น), GoF Behavioral (Strategy, State, Observer), Global Exception Handler, Unit Test, README, SOLID / Design Patterns docs |
| 2 | ศุภวัทน์ แสนเรียน | 673380604-2 | 03 | `suphawat_6733806042_03` | Member, MemberInfo, Docker, Deployment (Render), โครงสร้าง repo, Component / Deployment Diagram |
| 3 | ฉัตรดนัย ไกรราช | 673380397-1 | 03 | `chatdanay_6733803971_03` | Frontend redesign, Use Case / Activity Diagram |
| 4 | ภูริ ตั้งพงษ์ | 673380086-8 | 04 | `phuri_6733800868_04` | Unit Test, Pagination & Sorting, Sequence Diagram, สไลด์ |
| 5 | ชินวัตร แสนเมือง | 673380400-8 | 04 | `chinnawat_6733804008_04` | หน้า Home, schema.sql / data.sql, Index / Cascade / Fetch Type, Class Diagram |
 
> หมายเหตุ: ฉัตรดนัยใช้บัญชี GitHub `daveza5584-oss` (และ `chatdanay1`) ทั้งสองบัญชีเป็นของฉัตรดนัย
 
## Tech Stack
 
| ส่วน | เทคโนโลยี |
| --- | --- |
| Language | Java 17 |
| Framework | Spring Boot 4.1.1 |
| Build Tool | Maven (Maven Wrapper `mvnw`) |
| Database | PostgreSQL 17 |
| ORM | Spring Data JPA (Hibernate 7) |
| Validation | Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@NotNull`) |
| API Docs | springdoc-openapi (Swagger UI) |
| Frontend | Thymeleaf |
| Testing | JUnit 5 + Mockito |
| Container | Docker + Docker Compose |
| Other | Lombok, Git + GitHub |
 
## System Architecture
 
ระบบใช้ **Layered Architecture** แยกชั้นชัดเจน และห้ามข้ามชั้น (Controller ไม่เรียก Repository ตรง)
 
```
Presentation Layer   controller/ (REST API)  +  web/ (Thymeleaf Controller)  +  templates/
        ↓
Service Layer        service/ (interface + Impl)  +  pattern/ (Strategy, State, Observer)
        ↓
Repository Layer     domain/repository/ (Spring Data JPA)
        ↓
Domain               domain/entity/  +  dto/ (Request/Response)  +  mapper/  +  exception/
```
 
- **MVC**: Model = Entity/DTO, View = Thymeleaf templates, Controller = `*WebController` / `*Controller`
- **DTO + Mapper**: API รับ/ส่งเป็น DTO เสมอ ไม่ส่ง Entity ออกไปตรงๆ (เช่น `MemberResponseDTO` ไม่มี password)
- **Dependency Injection**: Constructor Injection ผ่าน `@RequiredArgsConstructor` + `private final`
- **Global Exception Handler**: `exception/GlobalExceptionHandler` (`@RestControllerAdvice`) แปลง error ทุกตัวเป็นรูปแบบเดียวกัน
ตัวอย่างรูปแบบ error:
```json
{
  "timestamp": "2026-10-10T14:53:52",
  "status": 400,
  "error": "Bad Request",
  "message": "Session already completed",
  "path": "/api/training-sessions/1/complete"
}
```
 
## Database Design (ER Diagram)
 
ER Diagram และ Data Dictionary ฉบับเต็มอยู่ที่ [`doc/er-diagram.md`](doc/er-diagram.md)
 
มี 6 ตาราง
 
| ตาราง | คำอธิบาย |
| --- | --- |
| `trainer` | เทรนเนอร์ |
| `member` | บัญชีสมาชิก |
| `member_info` | ข้อมูลส่วนตัวสมาชิก |
| `membership_plan` | แพ็กเกจสมาชิก |
| `membership` | การสมัครแพ็กเกจ |
| `training_session` | นัดฝึกกับเทรนเนอร์ |
 
ความสัมพันธ์
 
| ความสัมพันธ์ | ประเภท | FK |
| --- | --- | --- |
| member ↔ member_info | One-to-One | `member_info.member_id` (UNIQUE) |
| trainer → member | One-to-Many | `member.trainer_id` |
| member → membership | One-to-Many | `membership.member_id` |
| membership_plan → membership | One-to-Many | `membership.plan_id` |
| trainer → training_session | One-to-Many | `training_session.trainer_id` |
| member → training_session | One-to-Many | `training_session.member_id` |
 
## Installation & Setup
 
สิ่งที่ต้องมี: Java 17+, PostgreSQL (หรือ Docker), Git
 
```bash
git clone https://github.com/suphawat1/Group28_Gym_Member_Management_System.git
cd Group28_Gym_Member_Management_System
```
 
สร้างฐานข้อมูลชื่อ `gym_db` แล้วตั้งค่าใน `code/src/main/resources/application.properties`
 
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gym_db
spring.datasource.username=postgres
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```
 
## How to Run
 
**แบบที่ 1: รันในเครื่อง**
```bash
cd code
./mvnw spring-boot:run        # Windows: mvnw spring-boot:run
```
 
**แบบที่ 2: Docker Compose (รันแอป + PostgreSQL พร้อมกัน)**
```bash
cd code
docker compose up --build
```
 
เปิดใช้งาน
- หน้าเว็บ: http://localhost:8080/
- Swagger UI: http://localhost:8080/swagger-ui/index.html
## API Documentation
 
เอกสาร API ฉบับเต็มดูได้ที่ Swagger UI: `/swagger-ui/index.html`
 
ทุก Resource มี CRUD ครบ (GET ทั้งหมด, GET by id, POST, PUT, DELETE)
 
| Resource | Base Path |
| --- | --- |
| Trainer | `/api/trainers` |
| Member | `/api/members` |
| MemberInfo | `/api/member-infos` |
| MembershipPlan | `/api/membership-plans` |
| Membership | `/api/memberships` |
| TrainingSession | `/api/training-sessions` |
 
Endpoint พิเศษ
 
| Method | Path | คำอธิบาย |
| --- | --- | --- |
| GET | `/api/pricing?planId={id}&customerType={REGULAR\|STUDENT\|LOYAL}` | คำนวณราคาตามประเภทลูกค้า (Strategy) |
| POST | `/api/training-sessions/{id}/complete` | เปลี่ยนนัดเป็น COMPLETED (State + Observer) |
| POST | `/api/training-sessions/{id}/cancel` | เปลี่ยนนัดเป็น CANCELLED (State + Observer) |
| GET | `/api/members/page?page=0&size=10&sortBy=username&direction=asc` | ดูสมาชิกแบบแบ่งหน้าและเรียงลำดับ (Pagination & Sorting) |
 
HTTP Status ที่ใช้: 200, 201, 204, 400, 404, 409, 500
 
## How to Run Tests
 
```bash
cd code
./mvnw test                   # Windows: mvnw test
```
 
Unit Test ครอบคลุม Service ทั้ง 6 ตัว (JUnit 5 + Mockito)
Unit Test อยู่ในโฟลเดอร์ `test/` และ Test Report อยู่ที่ [`doc/test-report.md`](doc/test-report.md) (ผลล่าสุด: Tests run 58, Failures 0, Errors 0)
 
## Deployment URL
 
TODO: ใส่ URL ที่ deploy แล้ว
 
## Project Structure
 
```
.
├── code/                 # Source code + Configuration (Spring Boot project, Dockerfile, docker-compose.yml)
│   └── src/main/java/com/gym/membership/
│       ├── controller/   # REST Controller
│       ├── web/          # Thymeleaf Controller
│       ├── service/      # Service interface + Impl
│       ├── domain/
│       │   ├── entity/       # JPA Entity
│       │   └── repository/   # Spring Data JPA Repository
│       ├── dto/          # Request / Response DTO
│       ├── mapper/       # DTO ↔ Entity
│       ├── exception/    # Global Exception Handler + ErrorResponse
│       └── pattern/
│           ├── strategy/ # PricingStrategy (Regular / Student / Loyal)
│           ├── state/    # SessionState (Booked / Completed / Cancelled)
│           └── observer/ # SessionObserver (TrainerNotifier / MemberNotifier)
├── test/                 # Unit Test (JUnit 5 + Mockito)
├── doc/                  # เอกสารทั้งหมด
│   ├── er-diagram.md     # ER Diagram + Data Dictionary
│   ├── test-report.md    # Test Report
│   ├── slide/            # สไลด์นำเสนอ
│   ├── solid-analysis.md
│   └── design-patterns.md
├── img/                  # รูป Diagram (Component, Deployment ฯลฯ)
└── README.md
```