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
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookEntityServiceImplCreateTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CheckService checkService;
    @Autowired
    private BookService bookService;
    private int minInput;
    private int maxInput;
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

        minInput = 3;
        maxInput = 40;
    }

    @Test
    void book__create__correctReturnNewObject() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer newId = bookList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        testBook.setId(newId);
        Mockito.when(bookDao.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(testBook);

        BookEntity expected = testBook;
        BookEntity actual = bookService.create(inputTitle, inputAuthorId, inputGenreIdList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__create_IncorrectTitle__returnNull() {
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectAuthorId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = 100;
        List<Integer> inputGenreIdList = testBook.getGenreListId();

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__create_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = List.of(100);

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputTitle), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(bookDao.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertNull(bookService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }
}