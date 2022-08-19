package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.book.BookService;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryServiceImplReadTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private LibraryServiceImpl libraryService;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private List<Book> bookList;


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
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer id = insertBook1.getId();

        Mockito.when(bookService.readById(id)).thenReturn(insertBook1);

        Book expected = insertBook1;
        Book actual = libraryService.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer id = 100;
        Mockito.when(bookService.readById(id)).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readById(id));
    }

    @Test
    void book__readByTitle__correctReturnObject() {
        String title = insertBook1.getTitle();

        Mockito.when(bookService.readByTitle(title)).thenReturn(List.of(insertBook1));

        List<Book> expected = List.of(insertBook1);
        List<Book> actual = libraryService.readByTitle(title);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__correctReturnEmptyList() {
        String title = "smth";
        Mockito.when(bookService.readByTitle(title)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readByTitle(title));
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.when(bookService.readAll()).thenReturn(bookList);
        List<Book> expected = bookList;
        List<Book> actual = libraryService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_NotFields__correctReturnEmptyList() {
        Mockito.when(bookService.readAll()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readAll());
    }
}