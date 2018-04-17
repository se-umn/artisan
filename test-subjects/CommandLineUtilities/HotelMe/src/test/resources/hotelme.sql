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

INSERT INTO users(userID, fname, lname, uname, pw) VALUES(1, 'David', 'Nakonechnyy', 'kgb', 'qweasd');
INSERT INTO users(userID, fname, lname, uname, pw) VALUES(2, 'Mikaela', 'Burkhardt', 'mb', 'qweasd');
INSERT INTO users(userID, fname, lname, uname, pw) VALUES(3, 'Test', 'Test', 'test', '1234');

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
#yyyy-mm-dd
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Double', 4, 69);

INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Queen', 4, 109);

INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy King', 4, 139);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy King', 4, 139);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy King', 4, 139);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy King', 4, 139);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy King', 4, 139);

INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Suite', 6, 212);
INSERT INTO rooms(roomType, maxOccupancy, price) VALUES ('Happy Suite', 6, 212);

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

INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (1, 1, 2, 2, '2016-08-03', '2016-08-05');
INSERT INTO reservations(userID, roomID,  totalAdults, totalChildren, checkIn, checkOut) VALUES (1, 1,  2, 2, '2016-08-05', '2016-08-07');
INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (1, 2, 2, 2, '2016-08-02', '2016-08-07');
INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (1, 3, 2, 2, '2016-08-07', '2016-08-09');

#Testing
