package ru.baranova.spring.dao.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@JdbcTest
@ContextConfiguration(classes = {BookDaoTestConfig.class, StopSearchConfig.class})
public class BookDaoTest {
    @Autowired
    private BookDao bookDaoJdbc;

    @Test
    void book__create__correctReturnNewBook() {
        // todo syntax error or can't return id
        // todo
        Book expected = new Book(2, "TitleTest"
                , new Author(1, "SurnameTest", "NameTest")
                , List.of(new Genre(2, "NameTest", "DescriptionTest")));
        Integer id = bookDaoJdbc.create(expected);
        Book actual = bookDaoJdbc.getById(2);
        Assertions.assertEquals(expected,actual);
    }

    @Test
    void book__getById__correctReturnBookById() {
        Book expected = new Book(1, "Title"
                , new Author(1, "Surname", "Name")
                , List.of(new Genre(1, "Name1", "Description1"), new Genre(2, "Name2", "Description2")));
        Book actual = bookDaoJdbc.getById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getTitle(),actual.getTitle()),
                () -> Assertions.assertEquals(expected.getAuthor().getSurname(),actual.getAuthor().getSurname()),
                () -> Assertions.assertEquals(expected.getGenre().get(0),actual.getGenre().get(0)),
                () -> Assertions.assertEquals(expected.getId(),actual.getId())
        );
    }
}
