

ALTER TABLE room
    ADD COLUMN IF NOT EXISTS guest_id INT REFERENCES guest(id),
    ADD COLUMN IF NOT EXISTS "checkindate" DATE,
    ADD COLUMN IF NOT EXISTS "checkoutdate" DATE;

