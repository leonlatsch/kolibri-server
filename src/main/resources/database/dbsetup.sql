-- Create the database for the olivia backend system
-- USE WITH CAUTIN! OVERRIDES EXISTING DATABASES!

-- Cleanup old database

DROP DATABASE IF EXISTS oliviadb;
DROP USER IF EXISTS 'olivia'@'localhost';
FLUSH PRIVILEGES;

-- Create new database with user

CREATE DATABASE IF NOT EXISTS oliviadb;

CREATE USER 'olivia'@'localhost' IDENTIFIED BY 'olivia';

GRANT ALL PRIVILEGES ON oliviadb.* TO 'olivia'@'localhost' IDENTIFIED BY 'olivia';

FLUSH PRIVILEGES;

USE oliviadb;

-- Create tables

CREATE TABLE user (
    uid INT(11) PRIMARY KEY NOT NULL,
    username VARCHAR(25) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(70) NOT NULL,
    profile_pic LONGBLOB,
    profile_pic_tn LONGBLOB
);

CREATE TABLE message (
    mid VARCHAR(36) PRIMARY KEY NOT NULL,
    uid_from INT(11) NOT NULL,
    uid_to INT(11) NOT NULL,
    content LONGBLOB NOT NULL,
    type INT(11) NOT NULL,
    timestamp DATE NOT NULL,
    cid VARCHAR(36) NOT NULL
);

CREATE TABLE chat (
    cid VARCHAR(36) PRIMARY KEY NOT NULL,
    first_member INT(11) NOT NULL,
    second_member INT(11) NOT NULL
);
