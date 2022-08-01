package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplDeleteTest {
    @Autowired
    private BookDao bookDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private BookService bookServiceImpl;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        Book insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        Book insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        bookList = List.of(insertBook1, insertBook2);
    }

    @Test
    void book__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertTrue(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_NonexistentId__false() {
        Integer inputId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }

    @Test
    void book__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertFalse(bookServiceImpl.delete(inputId));
    }
}