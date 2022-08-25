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
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.BookRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {BookServiceImplTestConfig.class, StopSearchConfig.class})
class BookServiceImplReadTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;
    private Book insertBook1;
    private Book insertBook2;
    private Book testBook;
    private List<Book> BookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, null, null);
        Author insertAuthor2 = new Author(2, null, null);
        Author testAuthor = new Author(1, null, null);
        Genre insertGenre1 = new Genre(1, null, null);
        Genre insertGenre2 = new Genre(2, null, null);
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", new Date());
        Comment insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", new Date());

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), List.of(insertComment1, insertComment3));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2), List.of(insertComment2));
        Book insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1), List.of(insertComment4));
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2), List.of(insertComment4));
        BookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer inputId = BookList.size();

        Mockito.when(bookRepository.findById(inputId)).thenReturn(Optional.of(BookList.get(inputId - 1)));

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
        Mockito.when(bookRepository.findById(inputId)).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> bookService.readById(inputId));
    }

    @Test
    void book__readByTitle__correctReturnListObject() {
        insertBook2.setTitle(insertBook1.getTitle());
        String inputTitle = insertBook1.getTitle();

        Mockito.when(bookRepository.findByTitle(inputTitle)).thenReturn(List.of(insertBook1, insertBook2));

        List<Book> expected = List.of(insertBook1, insertBook2);
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

        Mockito.when(bookRepository.findByTitle(inputTitle)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor__correctReturnListObject() {
        insertBook2.setTitle(insertBook1.getTitle());
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        Mockito.when(bookRepository.findByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(List.of(insertBook1));

        List<Book> expected = List.of(insertBook1);
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentTitle__returnNull() {
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        Mockito.when(bookRepository.findByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_NonexistentAuthorId__returnNull() {
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        Mockito.when(bookRepository.findByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitleAndAuthor_IncorrectTitleAuthorId__returnNull() {
        String inputTitle = insertBook1.getTitle();
        Integer inputAuthorId = insertBook1.getAuthor().getId();
        Mockito.when(bookRepository.findByTitleAndAuthor(inputTitle, inputAuthorId)).thenReturn(Collections.emptyList());

        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readByTitleAndAuthor(inputTitle, inputAuthorId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.doReturn(BookList).when(bookRepository).findAll();
        List<Book> expected = BookList;
        List<Book> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_Exception__EmptyList() {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        List<Book> expected = Collections.emptyList();
        List<Book> actual = bookService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}
