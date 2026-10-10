# การออกแบบ Entity: Index / Fetch Type / Cascade

| เรื่อง | การตัดสินใจ | เหตุผล |
|---|---|---|
| Index | สร้างที่คอลัมน์ FK, `end_date`, `session_date`, `username` | PostgreSQL ไม่สร้าง index ให้ FK อัตโนมัติ ใช้เร่ง JOIN และการค้นหา |
| Fetch Type | `LAZY` ที่ `@ManyToOne` / `@OneToOne` ทุกตัว | ค่าเริ่มต้นเป็น EAGER ซึ่งดึงข้อมูลที่เชื่อมมาทุกครั้งแม้ไม่ได้ใช้ |
| Cascade | ไม่ใส่ที่ฝั่งลูก | Cascade จากลูกไปแม่ผิดหลักการ ประวัติการสมัคร/การฝึกต้องไม่ไปสร้างหรือลบสมาชิก การลบข้อมูลแม่ที่ยังมีข้อมูลลูกจึงถูกกันไว้ (RESTRICT) |
| Trainer, MembershipPlan | ไม่แก้ | ไม่มี FK และ PK มี index อยู่แล้ว |