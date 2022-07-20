INSERT INTO author (author_surname, author_name)
VALUES ('Surname', 'Name');
INSERT INTO genre(genre_name, genre_description)
VALUES ('Name1', 'Description1'),
       ('Name2', 'Description2');
INSERT INTO book(book_title, author_id)
VALUES ('Title', 1);
INSERT INTO book_genre(book_id, genre_id)
VALUES (1, 1),
       (1, 2);