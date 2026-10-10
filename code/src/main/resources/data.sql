-- Gym Member Management System : sample data
-- รันหลัง schema.sql / รันซ้ำได้ (ON CONFLICT DO NOTHING)

INSERT INTO trainer (trainer_id, name, phone, specialty) VALUES
    (1, 'Somchai Jaidee',   '0801111111', 'Weight Training'),
    (2, 'Napat Rungrueng',  '0802222222', 'Cardio'),
    (3, 'Kanya Sukjai',     '0803333333', 'Yoga')
ON CONFLICT (trainer_id) DO NOTHING;

INSERT INTO membership_plan (plan_id, plan_name, price, duration_days) VALUES
    (1, 'Monthly',   1500.00,  30),
    (2, 'Quarterly', 4000.00,  90),
    (3, 'Yearly',   14000.00, 365),
    (4, 'Weekly',     500.00,   7)
ON CONFLICT (plan_id) DO NOTHING;

-- password เป็นค่าตัวอย่างเท่านั้น
INSERT INTO member (member_id, username, email, password, phone, trainer_id) VALUES
    (1, 'testuser', 'test@gym.com',     'demo1234', '0811111111', NULL),
    (2, 'anan',     'anan@gym.com',     'demo1234', '0812222222', 1),
    (3, 'benja',    'benja@gym.com',    'demo1234', '0813333333', 2),
    (4, 'chaiwat',  'chaiwat@gym.com',  'demo1234', '0814444444', 1),
    (5, 'dara',     'dara@gym.com',     'demo1234', '0815555555', 3)
ON CONFLICT (member_id) DO NOTHING;

INSERT INTO member_info (info_id, member_id, full_name, gender, date_of_birth, emergency_contact) VALUES
    (1, 2, 'Anan Boonmee',    'Male',   DATE '2002-03-14', '0899999991'),
    (2, 3, 'Benja Srisuk',    'Female', DATE '2001-07-21', '0899999992'),
    (3, 4, 'Chaiwat Thongdee','Male',   DATE '1999-11-05', '0899999993')
ON CONFLICT (info_id) DO NOTHING;

-- end_date = start_date + duration_days ของแพ็กเกจ
INSERT INTO membership (membership_id, member_id, plan_id, start_date, end_date) VALUES
    (1, 1, 1, DATE '2026-10-01', DATE '2026-10-01' + 30),
    (2, 2, 2, DATE '2026-09-15', DATE '2026-09-15' + 90),
    (3, 3, 3, DATE '2026-01-01', DATE '2026-01-01' + 365),
    (4, 4, 1, DATE '2026-08-01', DATE '2026-08-01' + 30),
    (5, 5, 4, DATE '2026-10-05', DATE '2026-10-05' + 7)
ON CONFLICT (membership_id) DO NOTHING;

INSERT INTO training_session (session_id, trainer_id, member_id, session_date, session_time, status) VALUES
    (1, 1, 2, DATE '2026-10-12', TIME '09:00', 'BOOKED'),
    (2, 1, 4, DATE '2026-10-12', TIME '10:00', 'BOOKED'),
    (3, 2, 3, DATE '2026-10-13', TIME '17:30', 'BOOKED'),
    (4, 3, 5, DATE '2026-10-08', TIME '08:00', 'COMPLETED'),
    (5, 2, 3, DATE '2026-10-06', TIME '18:00', 'COMPLETED')
ON CONFLICT (session_id) DO NOTHING;

-- ตั้งค่า sequence ให้ต่อจาก id ที่ใส่เอง ไม่งั้นการเพิ่มข้อมูลผ่านแอปจะ id ชน
SELECT setval(pg_get_serial_sequence('trainer', 'trainer_id'),               (SELECT MAX(trainer_id) FROM trainer));
SELECT setval(pg_get_serial_sequence('membership_plan', 'plan_id'),          (SELECT MAX(plan_id) FROM membership_plan));
SELECT setval(pg_get_serial_sequence('member', 'member_id'),                 (SELECT MAX(member_id) FROM member));
SELECT setval(pg_get_serial_sequence('member_info', 'info_id'),              (SELECT MAX(info_id) FROM member_info));
SELECT setval(pg_get_serial_sequence('membership', 'membership_id'),         (SELECT MAX(membership_id) FROM membership));
SELECT setval(pg_get_serial_sequence('training_session', 'session_id'),      (SELECT MAX(session_id) FROM training_session));