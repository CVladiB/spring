package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplOtherMethodsTest {
    @Autowired
    private BookService bookServiceImpl;
    private BookEntity insertBook1;
    private BookEntity testBook;
    private List<BookEntity> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Genre testGenre = new Genre(1, null, null);

        insertBook1 = new BookEntity(1, "Title1", insertAuthor1.getId(), List.of(insertGenre1.getId(), insertGenre2.getId()));
        BookEntity insertBook2 = new BookEntity(2, "Title2", insertAuthor1.getId(), List.of(insertGenre2.getId()));
        BookEntity insertBook3 = new BookEntity(3, "Title3", insertAuthor2.getId(), List.of(insertGenre1.getId()));
        testBook = new BookEntity(null, "TitleTest", testAuthor.getId(), List.of(insertGenre2.getId()));
        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void checkExistId__true() {
        Integer inputId = 1;
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(bookList.get(0));
        Assertions.assertTrue(bookServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId_Exception__false() {
        Integer inputId = 1;
        Mockito.when(bookServiceImpl.readById(inputId)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertFalse(bookServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId__false() {
        Integer inputId = 100;
        Mockito.when(bookServiceImpl.readById(inputId)).thenReturn(null);
        Assertions.assertFalse(bookServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistTitle_Exception__true() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        Mockito.when(bookServiceImpl.readByTitle(inputTitle)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertFalse(bookServiceImpl.checkExist(inputTitle, inputAuthorId));
    }

    @Test
    void checkExistTitle__true() {
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthorId();
        Mockito.when(bookServiceImpl.readByTitle(inputTitle)).thenReturn(List.of(insertBook1));
        Assertions.assertTrue(bookServiceImpl.checkExist(inputTitle, inputAuthorId));
    }

    @Test
    void checkExistNameSurname_NonExistSurnameAndName__false() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        Mockito.when(bookServiceImpl.readByTitle(inputTitle)).thenReturn(null);
        Assertions.assertFalse(bookServiceImpl.checkExist(inputTitle, inputAuthorId));
    }
}