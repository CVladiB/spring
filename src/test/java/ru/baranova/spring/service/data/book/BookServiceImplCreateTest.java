package ru.baranova.spring.service.data.book;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplCreateTest {
    @Autowired
    private BookDao bookDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private BookService bookServiceImpl;
    private int minInput;
    private int maxInput;
    private Book insertBook1;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        Book insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        testBook = new Book(null, "TitleTest", insertAuthor2, List.of(insertGenre2));
        bookList = List.of(insertBook1, insertBook2);

        minInput = 3;
        maxInput = 40;
    }

    @Test
    void book__create__correctReturnNewObject() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer newId = bookList.size() + 1;

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(newId);

        testBook.setId(newId);
        Book expected = testBook;
        Book actual = bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__create_IncorrectTitle__returnNull() {
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectAuthorId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = 100;
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.TRUE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = List.of(100);

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isCorrectInputString(inputTitle, minInput, maxInput)).thenReturn(Boolean.FALSE);
        Mockito.when(bookDaoJdbc.create(inputTitle, inputAuthorId, inputGenreIdList)).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectExistTitleAndAuthorId__returnNull() {
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.doReturn(bookList).when(bookServiceImpl).readAll();

        Assertions.assertNull(bookServiceImpl.create(inputTitle, inputAuthorId, inputGenreIdList));
    }
}