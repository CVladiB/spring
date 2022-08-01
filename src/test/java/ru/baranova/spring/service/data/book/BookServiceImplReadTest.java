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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplReadTest {
    @Autowired
    private BookDao bookDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private BookService bookServiceImpl;
    private Book insertBook1;
    private Book insertBook2;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        testBook = new Book(null, "TitleTest", insertAuthor2, List.of(insertGenre2));
        bookList = List.of(insertBook1, insertBook2);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer inputId = bookList.size();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getById(inputId)).thenReturn(bookList.get(inputId - 1));

        Book expected = bookList.get(inputId - 1);
        Book actual = bookServiceImpl.readById(inputId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer inputId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookServiceImpl.readById(inputId));
    }

    @Test
    void book__readByTitle__correctReturnListObject() {
        insertBook2.setTitle(insertBook1.getTitle());
        String inputTitle = insertBook1.getTitle();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.getByTitle(inputTitle)).thenReturn(List.of(insertBook1, insertBook2));

        List<Book> expected = List.of(insertBook1, insertBook2);
        List<Book> actual = bookServiceImpl.readByTitle(inputTitle);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__returnNull() {
        String inputTitle = testBook.getTitle();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.getByTitle(inputTitle)).thenReturn(List.of(insertBook1, insertBook2));

        List<Book> expected = new ArrayList<>();
        List<Book> actual = bookServiceImpl.readByTitle(inputTitle);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.doReturn(bookList).when(bookDaoJdbc).getAll();

        List<Book> expected = bookList;
        List<Book> actual = bookServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }
}