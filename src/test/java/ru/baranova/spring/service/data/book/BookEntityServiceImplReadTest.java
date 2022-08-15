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

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookEntityServiceImplReadTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private BookService bookService;
    private BookEntity insertBook1Entity;
    private BookEntity insertBook2Entity;
    private BookEntity testBookEntity;
    private List<BookEntity> bookEntityList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Genre testGenre = new Genre(1, null, null);

        insertBook1Entity = new BookEntity(1, "Title1", insertAuthor1.getId(), List.of(insertGenre1.getId(), insertGenre2.getId()));
        insertBook2Entity = new BookEntity(2, "Title2", insertAuthor1.getId(), List.of(insertGenre2.getId()));
        BookEntity insertBook3Entity = new BookEntity(3, "Title3", insertAuthor2.getId(), List.of(insertGenre1.getId()));
        testBookEntity = new BookEntity(null, "TitleTest", testAuthor.getId(), List.of(insertGenre2.getId()));
        bookEntityList = List.of(insertBook1Entity, insertBook2Entity, insertBook3Entity);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer inputId = bookEntityList.size();

        Mockito.when(bookDao.getById(inputId)).thenReturn(bookEntityList.get(inputId - 1));

        BookEntity expected = bookEntityList.get(inputId - 1);
        BookEntity actual = bookService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer inputId = bookEntityList.size() + 1;
        Assertions.assertNull(bookService.readById(inputId));
    }

    @Test
    void book__readById_Exception__returnNull() {
        Integer inputId = bookEntityList.size();
        Mockito.when(bookDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(bookService.readById(inputId));
    }

    @Test
    void book__readByTitle__correctReturnListObject() {
        insertBook2Entity.setTitle(insertBook1Entity.getTitle());
        String inputTitle = insertBook1Entity.getTitle();

        Mockito.when(bookDao.getByTitle(inputTitle)).thenReturn(List.of(insertBook1Entity, insertBook2Entity));

        List<BookEntity> expected = List.of(insertBook1Entity, insertBook2Entity);
        List<BookEntity> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__returnNull() {
        String inputTitle = testBookEntity.getTitle();

        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_Exception__returnNull() {
        String inputTitle = testBookEntity.getTitle();

        Mockito.when(bookDao.getByTitle(inputTitle)).thenReturn(Collections.emptyList());

        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor__correctReturnListObject() {
        insertBook2Entity.setTitle(insertBook1Entity.getTitle());
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthorId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(List.of(insertBook1Entity));

        List<BookEntity> expected = List.of(insertBook1Entity);
        List<BookEntity> actual = bookService.readByTitleAndAuthorId(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentTitle__returnNull() {
        String inputTitle = testBookEntity.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthorId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readByTitleAndAuthorId(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentAuthorId__returnNull() {
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = testBookEntity.getAuthorId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readByTitleAndAuthorId(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_IncorrectTitleAuthorId__returnNull() {
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = insertBook2Entity.getAuthorId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readByTitleAndAuthorId(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.doReturn(bookEntityList).when(bookDao).getAll();
        List<BookEntity> expected = bookEntityList;
        List<BookEntity> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_Exception__EmptyList() {
        Mockito.when(bookDao.getAll()).thenReturn(Collections.emptyList());
        List<BookEntity> expected = Collections.emptyList();
        List<BookEntity> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}