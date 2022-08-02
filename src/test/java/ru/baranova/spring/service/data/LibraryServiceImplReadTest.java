package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.book.BookService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class})
class LibraryServiceImplReadTest {
    @Autowired
    private BookService bookServiceImpl;
    @Autowired
    private LibraryService libraryServiceImpl;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private Book emptyInsertBook1;
    private Book emptyInsertBook2;
    private Book emptyInsertBook3;
    private List<Book> bookList;
    private List<Book> emptyBookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1));
        bookList = List.of(insertBook1, insertBook2, insertBook3);
        emptyInsertBook1 = new Book(1, "Title1", new Author(1, null, null), List.of(
                new Genre(1, null, null)
                , new Genre(2, null, null)));
        emptyInsertBook2 = new Book(2, "Title2", new Author(1, null, null)
                , List.of(new Genre(2, null, null)));
        emptyInsertBook3 = new Book(3, "Title3", new Author(2, null, null)
                , List.of(new Genre(1, null, null)));
        emptyBookList = List.of(emptyInsertBook1, emptyInsertBook2, emptyInsertBook3);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer id = insertBook1.getId();
        Mockito.when(bookServiceImpl.readById(id)).thenReturn(emptyInsertBook1);
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        Book expected = insertBook1;
        Book actual = libraryServiceImpl.readById(id);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer id = 100;
        Mockito.when(bookServiceImpl.readById(id)).thenReturn(null);
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        Assertions.assertNull(libraryServiceImpl.readById(id));
    }

    @Test
    void book__readById_NullSetFields__returnNull() {
        Integer id = insertBook1.getId();
        Mockito.when(bookServiceImpl.readById(id)).thenReturn(emptyInsertBook1);
        Mockito.doReturn(null).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        Assertions.assertNull(libraryServiceImpl.readById(id));
    }


    @Test
    void book__readByTitle__correctReturnObject() {
        String title = insertBook1.getTitle();
        Mockito.when(bookServiceImpl.readByTitle(title)).thenReturn(List.of(emptyInsertBook1));
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        List<Book> expected = List.of(insertBook1);
        List<Book> actual = libraryServiceImpl.readByTitle(title);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__correctReturnEmptyList() {
        String title = "smth";
        Mockito.when(bookServiceImpl.readByTitle(title)).thenReturn(new ArrayList<>());
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        List<Book> expected = new ArrayList<>();
        List<Book> actual = libraryServiceImpl.readByTitle(title);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NullSetFields__returnNull() {
        String title = insertBook1.getTitle();
        Mockito.when(bookServiceImpl.readByTitle(title)).thenReturn(List.of(emptyInsertBook1));
        Mockito.doReturn(null).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);

        List<Book> expected = new ArrayList<>();
        List<Book> actual = libraryServiceImpl.readByTitle(title);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.when(bookServiceImpl.readAll()).thenReturn(emptyBookList);
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);
        Mockito.doReturn(insertBook2).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook2);
        Mockito.doReturn(insertBook3).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook3);

        List<Book> expected = bookList;
        List<Book> actual = libraryServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_NullSetFields__correctReturnListObject() {
        Mockito.when(bookServiceImpl.readAll()).thenReturn(emptyBookList);
        Mockito.doReturn(insertBook1).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook1);
        Mockito.doReturn(insertBook2).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook2);
        Mockito.doReturn(null).when(libraryServiceImpl).checkAndSetFieldsToBook(emptyInsertBook3);

        List<Book> expected = List.of(insertBook1, insertBook2);
        List<Book> actual = libraryServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_NotFields__correctReturnEmptyList() {
        Mockito.when(bookServiceImpl.readAll()).thenReturn(new ArrayList<>());

        List<Book> expected = new ArrayList<>();
        List<Book> actual = libraryServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

}