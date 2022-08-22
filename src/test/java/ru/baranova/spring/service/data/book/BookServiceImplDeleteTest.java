package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.book.BookDao;
import ru.baranova.spring.service.app.CheckService;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplDeleteTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private BookService bookService;
    @Autowired
    private CheckService checkService;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        Book insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), Collections.emptyList());
        Book insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2), Collections.emptyList());
        bookList = List.of(insertBook1, insertBook2);
    }

    @Test
    void book__delete__true() {
        Integer inputId = bookList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.delete(inputId)).thenReturn(Boolean.TRUE);
        Assertions.assertTrue(bookService.delete(inputId));
    }

    @Test
    void book__delete_Exception__false() {
        Integer inputId = bookList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.delete(inputId)).thenReturn(false);
        Assertions.assertFalse(bookService.delete(inputId));
    }

    @Test
    void book__delete_NonexistentId__false() {
        Integer inputId = bookList.size() + 1;
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.FALSE);
        Assertions.assertFalse(bookService.delete(inputId));
    }

    @Test
    void book__delete_ExistNonexistentId__false() {
        Integer inputId = bookList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.delete(inputId)).thenReturn(Boolean.FALSE);
        Assertions.assertFalse(bookService.delete(inputId));
    }
}