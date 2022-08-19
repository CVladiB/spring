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

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplCreateTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CheckService checkService;
    @Autowired
    private BookService bookService;
    private int minInput;
    private int maxInput;
    private Book insertBook1;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Genre testGenre = new Genre(1, null, null);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        Book insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        Book insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1));
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2));
        bookList = List.of(insertBook1, insertBook2, insertBook3);

        minInput = 3;
        maxInput = 40;
    }

    @Test
    void book__create__correctReturnNewObject() {
        String inputTitle = testBook.getTitle();
        Author inputAuthor = testBook.getAuthor();
        List<Genre> inputGenreList = testBook.getGenreList();
        Integer newId = bookList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        testBook.setId(newId);
        Mockito.when(bookDao.create(inputTitle, inputAuthor, inputGenreList)).thenReturn(testBook);

        Book expected = testBook;
        Book actual = bookService.create(inputTitle, inputAuthor, inputGenreList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__create_IncorrectTitle__returnNull() {
        String inputTitle = "smth";
        Author inputAuthor = testBook.getAuthor();
        List<Genre> inputGenreList = testBook.getGenreList();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthor, inputGenreList));
    }

    @Test
    void book__create_IncorrectAuthorId__returnNull() {
        String inputTitle = testBook.getTitle();
        Author inputAuthor = testBook.getAuthor();
        List<Genre> inputGenreList = testBook.getGenreList();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.create(inputTitle, inputAuthor, inputGenreList)).thenReturn(null);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthor, inputGenreList));
    }

    @Test
    void book__create_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Author inputAuthor = testBook.getAuthor();
        List<Genre> inputGenreList = testBook.getGenreList();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.create(inputTitle, inputAuthor, inputGenreList)).thenReturn(null);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthor, inputGenreList));
    }
}