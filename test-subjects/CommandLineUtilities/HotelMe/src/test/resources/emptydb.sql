DROP DATABASE IF EXISTS hotelme;
CREATE DATABASE hotelme;
USE hotelme;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS reservations;

CREATE TABLE users
(
	userID INTEGER NOT NULL AUTO_INCREMENT,
    fname VARCHAR(30),
    lname VARCHAR(30),
    uname VARCHAR(20),
    pw VARCHAR(20),
    PRIMARY KEY(userID)
);


/*
	$69 is 2 doubles
    $109 is 2 queen
    $139 is 2 kings
    $212 is suite
*/
CREATE TABLE rooms
(
	roomID INTEGER NOT NULL AUTO_INCREMENT,
    roomType VARCHAR(30),
    maxOccupancy INTEGER(2),
    price FLOAT,
    PRIMARY KEY(roomID)
);

CREATE TABLE reservations
(
	reserveID INTEGER NOT NULL AUTO_INCREMENT,
    userID Integer,
    roomID Integer,
    totalAdults INTEGER(2),
    totalChildren INTEGER(2),
    checkIn DATE,
    checkOut DATE,
    PRIMARY KEY(reserveID),
    FOREIGN KEY (userID) REFERENCES users(userID),
	FOREIGN KEY (roomID) REFERENCES rooms(roomID)
);