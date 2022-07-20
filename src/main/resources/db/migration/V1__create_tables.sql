DROP TABLE if EXISTS author;
CREATE TABLE author (
    author_id SERIAL PRIMARY KEY,
    author_surname VARCHAR(20) NOT NULL,
    author_name VARCHAR(15) NOT NULL
);

DROP TABLE if EXISTS genre;
CREATE TABLE genre (
    genre_id SERIAL PRIMARY KEY,
    genre_name VARCHAR(60) UNIQUE,
    genre_description  VARCHAR(200) NULL
);

DROP TABLE if EXISTS book;
CREATE TABLE book (
    book_id SERIAL PRIMARY KEY,
    book_title VARCHAR(60) NOT NULL,
    author_id INT NOT NULL
);

DROP TABLE if EXISTS book_genre;
CREATE TABLE book_genre (
    book_id INT NOT NULL,
    genre_id INT NOT NULL
);