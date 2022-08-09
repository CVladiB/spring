package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookEntityServiceImplDeleteTest {
    @Autowired
    private BookDao bookDaoJdbc;
    @Autowired
    private BookService bookServiceImpl;
    private List<BookEntity> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        BookEntity insertBook1 = new BookEntity(1, "Title1", insertAuthor1.getId(), List.of(insertGenre1.getId(), insertGenre2.getId()));
        BookEntity insertBook2 = new BookEntity(2, "Title2", insertAuthor1.getId(), List.of(insertGenre2.getId()));
        bookList = List.of(insertBook1, insertBook2);
    }

    @Test
    void book__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = bookList.size();
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(bookList.get(1));
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);
        Assertions.assertTrue(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_Exception__false() {
        Integer inputId = bookList.size();
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(bookList.get(1));
        Mockito.doThrow(EmptyResultDataAccessException.class).when(bookDaoJdbc).delete(inputId);
        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_NonexistentId__false() {
        Integer inputId = bookList.size() + 1;
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(null);
        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = bookList.size();
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(bookList.get(1));
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);
        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }
}