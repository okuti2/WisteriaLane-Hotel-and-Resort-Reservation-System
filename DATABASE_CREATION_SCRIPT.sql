-- Drop existing tables if they exist
DROP TABLE IF EXISTS Room_Reservation;
DROP TABLE IF EXISTS Bill;
DROP TABLE IF EXISTS Reservation;
DROP TABLE IF EXISTS Admin;
DROP TABLE IF EXISTS Room;
DROP TABLE IF EXISTS Guest;

CREATE DATABASE IF NOT EXISTS hotelreservationsystem;
USE hotelreservationsystem;

CREATE TABLE Guest(
	guest_id BIGINT PRIMARY KEY auto_increment,
    first_name varchar(250) NOT NULL,
    last_name VARCHAR(250) NOT NULL,
    title VARCHAR(250),
    email VARCHAR(250) NOT NULL,
    address VARCHAR(300) NOT NULL,
    phone_number VARCHAR(20)
);

CREATE TABLE Room(
	room_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number BIGINT NOT NULL,
    room_type VARCHAR(250) NOT NULL,
    available BOOLEAN NOT NULL
);

CREATE TABLE Reservation(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    reservation_date DATE NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    guest_id BIGINT NOT NULL,
    no_of_guests BIGINT NOT NULL,
    paid BOOLEAN NOT NULL,
    cancelled BOOLEAN NOT NULL,
    CONSTRAINT fk_guest_id
    FOREIGN KEY (guest_id) REFERENCES Guest (guest_id)
);

CREATE TABLE Room_Reservation(
	room_id BIGINT,
    reserve_id BIGINT,
    CONSTRAINT room_reserve PRIMARY KEY (room_id, reserve_id),
    CONSTRAINT FK_room
    FOREIGN KEY (room_id) REFERENCES Room (room_id),
    CONSTRAINT FK_reserve
    FOREIGN KEY (reserve_id) REFERENCES Reservation(id)
);

CREATE TABLE Admin(
	admin_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    admin_no BIGINT NOT NULL,
    admin_username VARCHAR(250) NOT NULL,
    admin_password VARCHAR(250) NOT NULL
);

CREATE TABLE Bill(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bill_id BIGINT NOT NULL,
    book_no BIGINT UNIQUE NOT NULL,
    discount_applied BIGINT,
    total_cost BIGINT NOT NULL,
    admin_no BIGINT NOT NULL,
    CONSTRAINT FK_reservation
    FOREIGN KEY (book_no) REFERENCES Reservation (id),
    CONSTRAINT FK_admin
    FOREIGN KEY (admin_no) REFERENCES Admin(admin_id)
);

-- Inserting sample data for rooms
INSERT INTO Room (room_number, room_type, available) VALUES
(101, 'single', true),
(102, 'single', true),
(103, 'single', true),
(104, 'single', true),
(105, 'single', true),
(106, 'single', true),
(107, 'single', true),
(108, 'single', true),
(109, 'single', true),
(201, 'double', true),
(202, 'double', true),
(203, 'double', true),
(204, 'double', true),
(205, 'double', true),
(206, 'double', true),
(207, 'double', true),
(208, 'double', true),
(209, 'double', true),
(301, 'deluxe', true),
(302, 'deluxe', true),
(303, 'deluxe', true),
(304, 'deluxe', true),
(305, 'deluxe', true),
(306, 'deluxe', true),
(307, 'deluxe', true),
(308, 'deluxe', true),
(309, 'deluxe', true),
(401, 'penthouse', true),
(402, 'penthouse', true),
(403, 'penthouse', true),
(404, 'penthouse', true),
(405, 'penthouse', true),
(406, 'penthouse', true),
(407, 'penthouse', true),
(408, 'penthouse', true),
(409, 'penthouse', true);

-- Inserting data into the Admin table 
INSERT INTO Admin (admin_no, admin_username, admin_password) VALUES
(274759, 'okuti2@myseneca.ca', 'password1234'),
(586920, 'email1@email.com', 'password1234'),
(265530, '123', '123')
