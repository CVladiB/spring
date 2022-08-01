package ru.baranova.spring.service.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.config.LibraryServiceImplTestConfig;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class})
class LibraryServiceImplTest {
    @Autowired
    private BookService bookServiceImpl;
    @Autowired
    private AuthorService authorServiceImpl;
    @Autowired
    private GenreService genreServiceImpl;
    private Genre insertGenre1;
    private Genre insertGenre2;
    private Genre testGenre;
    private List<Genre> genreList;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private Author testAuthor;
    private List<Author> authorList;
    private Book insertBook1;
    private Book insertBook2;
    private Book insertBook3;
    private Book testBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);

        insertGenre1 = new Genre(1, "Name1", "Description1");
        insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new Book(2, "Title2", insertAuthor1, List.of(insertGenre2));
        insertBook3 = new Book(3, "Title3", insertAuthor2, List.of(insertGenre1));
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre2));

        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void create() {
    }

    @Test
    void createById() {
    }

    @Test
    void readById() {
    }

    @Test
    void readByTitle() {
    }

    @Test
    void readAll() {
    }

    @Test
    void update() {
    }

    @Test
    void updateById() {
    }

    @Test
    void delete() {
    }
}