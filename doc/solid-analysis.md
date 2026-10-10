# SOLID Analysis — Gym Membership Management System

เอกสารนี้ระบุว่าหลัก SOLID แต่ละข้อปรากฏในไฟล์ไหนของโปรเจค พร้อมเหตุผล
(path ทั้งหมดอ้างอิงจาก `src/main/java/com/gym/membership/`)

---

## S — Single Responsibility Principle

> แต่ละ Class มีหน้าที่เดียว

| ไฟล์ | บรรทัด | หน้าที่เดียวของคลาส |
| --- | --- | --- |
| `controller/TrainerController.java` | ทั้งคลาส | รับ HTTP request / ส่ง response เท่านั้น ไม่มี business logic |
| `service/TrainerServiceImpl.java` | ทั้งคลาส | business logic ของ Trainer เท่านั้น ไม่รู้เรื่อง HTTP |
| `domain/repository/TrainerRepository.java` | ทั้งไฟล์ | เข้าถึงฐานข้อมูลเท่านั้น |
| `mapper/TrainerMapper.java` | `toEntity()`, `toResponse()` | แปลง DTO ↔ Entity เท่านั้น |
| `exception/GlobalExceptionHandler.java` | ทั้งคลาส | แปลง exception เป็น HTTP error response เท่านั้น |
| `pattern/observer/TrainerNotifier.java` | `onStatusChanged()` | แจ้งเตือนเทรนเนอร์เท่านั้น |

**เหตุผล:** Business logic, Validation และ Persistence ไม่ถูกรวมไว้ในคลาสเดียว — Validation อยู่ที่ DTO (`@NotBlank`, `@NotNull`) + `@Valid` ใน Controller, Business logic อยู่ที่ Service, Persistence อยู่ที่ Repository เมื่อแก้ส่วนใดส่วนหนึ่งจึงไม่กระทบส่วนอื่น

---

## O — Open/Closed Principle

> เพิ่มฟีเจอร์ใหม่ด้วยการเพิ่มคลาส ไม่ใช่แก้ if-else เดิม

| ไฟล์ | บรรทัด | ตัวอย่าง |
| --- | --- | --- |
| `pattern/strategy/PricingService.java` | field `Map<String, PricingStrategy> strategies` และ `strategies.get(customerType)` | เลือกวิธีคำนวณราคาจาก Map ไม่มี if-else |
| `pattern/state/SessionStatusService.java` | field `Map<String, SessionState> states` | เลือกพฤติกรรมตามสถานะจาก Map ไม่มี if-else |
| `pattern/state/SessionStatusService.java` | field `List<SessionObserver> observers` และ `notifyObservers()` | แจ้งผู้รับทุกตัวผ่าน List |

**เหตุผล:** ถ้าต้องการส่วนลดประเภทใหม่ (เช่น `SENIOR`) แค่สร้างคลาสใหม่ที่ implement `PricingStrategy` พร้อม `@Component("SENIOR")` Spring จะใส่เข้า Map ให้อัตโนมัติ โดย**ไม่ต้องแก้** `PricingService` เลย เช่นเดียวกับการเพิ่มช่องทางแจ้งเตือนใหม่ (เช่น `EmailNotifier`) ที่ไม่ต้องแก้ `SessionStatusService`

---

## L — Liskov Substitution Principle

> Subclass ใช้แทน Superclass ได้โดยไม่พังตรรกะ

| ไฟล์ | บรรทัด | ตัวอย่าง |
| --- | --- | --- |
| `pattern/strategy/RegularPricing.java`, `StudentPricing.java`, `LoyalPricing.java` | `calculatePrice()` | ทุกตัวรับ `BigDecimal` และคืน `BigDecimal` ตามสัญญาของ `PricingStrategy` |
| `pattern/state/BookedState.java`, `CompletedState.java`, `CancelledState.java` | `complete()`, `cancel()` | ทุกตัวใช้แทน `SessionState` ได้ใน `SessionStatusService` |
| `pattern/observer/TrainerNotifier.java`, `MemberNotifier.java` | `onStatusChanged()` | ทุกตัวใช้แทน `SessionObserver` ได้ |

**เหตุผล:** `PricingService` และ `SessionStatusService` เรียกผ่าน interface เท่านั้น ไม่ว่าได้ implementation ตัวไหนมาก็ทำงานถูกต้อง ไม่มีคลาสใดโยน `UnsupportedOperationException`
สถานะ `CompletedState` / `CancelledState` โยน `ResponseStatusException(400)` ซึ่งเป็นกฎทางธุรกิจที่ระบุไว้ (นัดที่จบแล้วเปลี่ยนสถานะไม่ได้) และถูกจัดการเป็น HTTP 400 อย่างถูกต้องโดย `GlobalExceptionHandler` ไม่ได้ทำให้ระบบพัง

---

## I — Interface Segregation Principle

> แยก Interface ย่อยตามการใช้งาน ไม่มี Fat Interface

| ไฟล์ | บรรทัด | จำนวนเมธอด |
| --- | --- | --- |
| `pattern/strategy/PricingStrategy.java` | `calculatePrice()` | 1 |
| `pattern/state/SessionState.java` | `complete()`, `cancel()` | 2 |
| `pattern/observer/SessionObserver.java` | `onStatusChanged()` | 1 |
| `service/TrainerService.java`, `MemberService.java`, ... | ทั้งไฟล์ | แยก 1 interface ต่อ 1 entity |

**เหตุผล:** ไม่มี interface รวม ("GymService" ที่มีทุกเมธอด) คลาสที่ implement จึงไม่ถูกบังคับให้เขียนเมธอดที่ไม่ได้ใช้

---

## D — Dependency Inversion Principle

> Service ขึ้นกับ Interface ไม่ใช่ Concrete Class + ใช้ Constructor Injection เท่านั้น

| ไฟล์ | บรรทัด | ตัวอย่าง |
| --- | --- | --- |
| `controller/TrainerController.java` | `private final TrainerService trainerService;` | Controller ขึ้นกับ interface `TrainerService` ไม่ใช่ `TrainerServiceImpl` |
| `service/MembershipServiceImpl.java` | field `private final ...Repository` | Service ขึ้นกับ Repository interface |
| `pattern/strategy/PricingService.java` | `Map<String, PricingStrategy>` | ขึ้นกับ interface `PricingStrategy` |
| `pattern/state/SessionStatusService.java` | `Map<String, SessionState>`, `List<SessionObserver>` | ขึ้นกับ interface |
| ทุก Controller / Service | `@RequiredArgsConstructor` + `private final` | Constructor Injection ทั้งหมด ไม่มี `@Autowired` บน field |

**เหตุผล:** การพึ่ง interface ทำให้เปลี่ยน implementation ได้โดยไม่แก้ผู้เรียก และทำให้ Unit Test ใส่ mock (`@Mock` + `@InjectMocks`) เข้าไปทาง constructor ได้ทันที

---

> หมายเหตุ: คอลัมน์ "บรรทัด" ระบุเป็นชื่อเมธอด/ฟิลด์ (TODO: เปิดไฟล์จริงแล้วเติมเลขบรรทัด — ใน VS Code กด `Ctrl + G` เพื่อดู/ไปยังบรรทัด)
