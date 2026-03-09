-- Добавляем автоинкремент для id во всех таблицах
-- (таблицы уже существуют из V1 с id INT без SERIAL)

-- guest
CREATE SEQUENCE IF NOT EXISTS guest_id_seq;
ALTER TABLE guest ALTER COLUMN id SET DEFAULT nextval('guest_id_seq');
SELECT setval('guest_id_seq', COALESCE((SELECT MAX(id) FROM guest), 1));

-- room
CREATE SEQUENCE IF NOT EXISTS room_id_seq;
ALTER TABLE room ALTER COLUMN id SET DEFAULT nextval('room_id_seq');
SELECT setval('room_id_seq', COALESCE((SELECT MAX(id) FROM room), 1));

-- service
CREATE SEQUENCE IF NOT EXISTS service_id_seq;
ALTER TABLE service ALTER COLUMN id SET DEFAULT nextval('service_id_seq');
SELECT setval('service_id_seq', COALESCE((SELECT MAX(id) FROM service), 1));

-- residence
CREATE SEQUENCE IF NOT EXISTS residence_id_seq;
ALTER TABLE residence ALTER COLUMN id SET DEFAULT nextval('residence_id_seq');
SELECT setval('residence_id_seq', COALESCE((SELECT MAX(id) FROM residence), 1));

-- serviceRecord (в PostgreSQL имена без кавычек хранятся в нижнем регистре)
CREATE SEQUENCE IF NOT EXISTS service_record_id_seq;
ALTER TABLE servicerecord ALTER COLUMN id SET DEFAULT nextval('service_record_id_seq');
SELECT setval('service_record_id_seq', COALESCE((SELECT MAX(id) FROM servicerecord), 1));
