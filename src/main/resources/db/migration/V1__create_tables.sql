DROP TABLE if EXISTS author CASCADE;
CREATE TABLE author
(
    author_id      SERIAL PRIMARY KEY,
    author_surname VARCHAR(20) NOT NULL,
    author_name    VARCHAR(15) NOT NULL,
    UNIQUE (author_surname, author_name)
);

DROP TABLE if EXISTS genre CASCADE;
CREATE TABLE genre
(
    genre_id          SERIAL PRIMARY KEY,
    genre_name        VARCHAR(20) UNIQUE NOT NULL,
    genre_description VARCHAR(200)
);

DROP TABLE if EXISTS book CASCADE;
CREATE TABLE book
(
    book_id    SERIAL PRIMARY KEY,
    book_title VARCHAR(40) NOT NULL,
    author_id  INT         NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author (author_id) ON DELETE CASCADE,
    UNIQUE (book_title, author_id)
);

DROP TABLE if EXISTS book_genre CASCADE;
CREATE TABLE book_genre
(
    book_id  INT NOT NULL,
    genre_id INT NOT NULL,
    FOREIGN KEY (book_id) REFERENCES book (book_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE,
    UNIQUE (book_id, genre_id)
);