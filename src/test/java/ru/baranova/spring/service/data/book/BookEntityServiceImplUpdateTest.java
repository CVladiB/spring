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
class BookEntityServiceImplUpdateTest {
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
        List<BookEntity> bookList = List.of(insertBook1, insertBook2, insertBook3);

        minInput = 3;
        maxInput = 40;
    }

    @Test
    void book__update__correctReturnObject() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer inputId = insertBook1.getId();

        Mockito.when(bookDao.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertBook1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(inputTitle);
        testBook.setId(inputId);
        Mockito.when(bookDao.update(inputId, inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(testBook);

        BookEntity expected = testBook;
        BookEntity actual = bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_IncorrectTitle__correctReturnObject() {
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer inputId = insertBook1.getId();

        Mockito.when(bookDao.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertBook1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(insertBook1.getTitle());
        testBook.setTitle(insertBook1.getTitle());
        testBook.setId(inputId);
        Mockito.when(bookDao.update(inputId, insertBook1.getTitle(), inputAuthorId, inputGenreIdList)).thenReturn(testBook);

        BookEntity expected = testBook;
        BookEntity actual = bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NonexistentId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer inputId = insertBook1.getId() + 1;

        Mockito.when(bookDao.getById(inputId)).thenReturn(null);

        Assertions.assertNull(bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__update_ExistNonexistentId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthorId();
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer inputId = insertBook1.getId() + 1;

        Mockito.when(bookDao.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertBook1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(inputTitle);
        Mockito.when(bookDao.update(inputId, inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertNull(bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__update_IncorrectGenreId__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = 100;
        List<Integer> inputGenreIdList = testBook.getGenreListId();
        Integer inputId = insertBook1.getId();

        Mockito.when(bookDao.getById(inputId)).thenReturn(insertBook1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertBook1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(inputTitle);
        Mockito.when(bookDao.update(inputId, inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertNull(bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }
}