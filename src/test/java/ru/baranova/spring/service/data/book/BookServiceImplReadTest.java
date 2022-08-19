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

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplReadTest {
    @Autowired
    private BookDao bookDao;
    @Autowired
    private BookService bookService;
    private Book insertBook1Entity;
    private Book insertBook2Entity;
    private Book testBook;
    private List<Book> BookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Genre testGenre = new Genre(1, null, null);

        insertBook1Entity = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2Entity = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        Book insertBook3Entity = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1));
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2));
        BookList = List.of(insertBook1Entity, insertBook2Entity, insertBook3Entity);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer inputId = BookList.size();

        Mockito.when(bookDao.getById(inputId)).thenReturn(BookList.get(inputId - 1));

        Book expected = BookList.get(inputId - 1);
        Book actual = bookService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer inputId = BookList.size() + 1;
        Assertions.assertNull(bookService.readById(inputId));
    }

    @Test
    void book__readById_Exception__returnNull() {
        Integer inputId = BookList.size();
        Mockito.when(bookDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(bookService.readById(inputId));
    }

    @Test
    void book__readByTitle__correctReturnListObject() {
        insertBook2Entity.setTitle(insertBook1Entity.getTitle());
        String inputTitle = insertBook1Entity.getTitle();

        Mockito.when(bookDao.getByTitle(inputTitle)).thenReturn(List.of(insertBook1Entity, insertBook2Entity));

        List<Book> expected = List.of(insertBook1Entity, insertBook2Entity);
        List<Book> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__returnNull() {
        String inputTitle = testBook.getTitle();

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_Exception__returnNull() {
        String inputTitle = testBook.getTitle();

        Mockito.when(bookDao.getByTitle(inputTitle)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor__correctReturnListObject() {
        insertBook2Entity.setTitle(insertBook1Entity.getTitle());
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthor().getId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(List.of(insertBook1Entity));

        List<Book> expected = List.of(insertBook1Entity);
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentTitle__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthor().getId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentAuthorId__returnNull() {
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthor().getId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_IncorrectTitleAuthorId__returnNull() {
        String inputTitle = insertBook1Entity.getTitle();
        Integer inputAuthorId = insertBook1Entity.getAuthor().getId();
        Mockito.when(bookDao.getByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.doReturn(BookList).when(bookDao).getAll();
        List<Book> expected = BookList;
        List<Book> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_Exception__EmptyList() {
        Mockito.when(bookDao.getAll()).thenReturn(Collections.emptyList());
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}