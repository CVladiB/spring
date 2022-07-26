INSERT INTO author (author_surname, author_name)
VALUES ('Surname1', 'Name1'),
       ('Surname2', 'Name2');

INSERT INTO genre(genre_name, genre_description)
VALUES ('Name1', 'Description1'),
       ('Name2', 'Description2');

INSERT INTO book(book_title, author_id)
VALUES ('Title1', 1), ('Title2', 1), ('Title3', 2);

INSERT INTO book_genre(book_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (3, 1);