-- Создаем таблицу гостей
CREATE TABLE IF NOT EXISTS guest (
    id INT PRIMARY KEY,
    name VARCHAR(255)
);

-- Создаем таблицу комнат
CREATE TABLE IF NOT EXISTS room (
    id INT PRIMARY KEY,
    number INT,
    price DOUBLE PRECISION,
    capacity INT,
    stars INT,
    status VARCHAR(50)
);

-- Создаем таблицу услуг
CREATE TABLE IF NOT EXISTS service (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE PRECISION
);

-- Создаем таблицу истории проживаний
CREATE TABLE IF NOT EXISTS residence (
    id INT PRIMARY KEY,
    guest_id INT REFERENCES guest(id),
    room_id INT REFERENCES room(id),
    checkInDate DATE,
    checkOutDate DATE
);

-- Создаем таблицу истории заказанных услуг
CREATE TABLE IF NOT EXISTS serviceRecord (
    id INT PRIMARY KEY,
    name VARCHAR(255),
    service_date DATE,
    service_id INT REFERENCES service(id),
    guest_id INT REFERENCES guest(id)
);