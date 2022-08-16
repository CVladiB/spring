package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryServiceImplReadTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private LibraryServiceImpl libraryService;
    private BookDTO insertBook1;
    private BookDTO insertBook2;
    private BookDTO insertBook3;
    private BookEntity emptyInsertBook1;
    private BookEntity emptyInsertBook2;
    private BookEntity emptyInsertBook3;
    private List<BookDTO> bookList;
    private List<BookEntity> emptyBookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");

        insertBook1 = new BookDTO(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        insertBook2 = new BookDTO(2, "Title2", insertAuthor1, List.of(insertGenre2));
        insertBook3 = new BookDTO(3, "Title3", insertAuthor2, List.of(insertGenre1));
        bookList = List.of(insertBook1, insertBook2, insertBook3);
        emptyInsertBook1 = new BookEntity(1, "Title1", 1, List.of(1, 2));
        emptyInsertBook2 = new BookEntity(2, "Title2", 1, List.of(2));
        emptyInsertBook3 = new BookEntity(3, "Title3", 2, List.of(1));
        emptyBookList = List.of(emptyInsertBook1, emptyInsertBook2, emptyInsertBook3);
    }

    @Test
    void book__readById__correctReturnObject() {
        Integer id = insertBook1.getId();

        Mockito.when(bookService.readById(id)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(insertBook1.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));

        BookDTO expected = insertBook1;
        BookDTO actual = libraryService.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readById_NonexistentId__returnNull() {
        Integer id = 100;
        Mockito.when(bookService.readById(id)).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readById(id));
    }

    @Test
    void book__readById_NullSetFields__returnNull() {
        Integer id = insertBook1.getId();

        Mockito.when(bookService.readById(id)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(null);
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readById(id));
    }

    @Test
    void book__readByTitle__correctReturnObject() {
        String title = insertBook1.getTitle();

        Mockito.when(bookService.readByTitle(title)).thenReturn(List.of(emptyInsertBook1));
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(insertBook1.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));

        List<BookDTO> expected = List.of(insertBook1);
        List<BookDTO> actual = libraryService.readByTitle(title);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readByTitle_NonexistentTitle__correctReturnEmptyList() {
        String title = "smth";
        Mockito.when(bookService.readByTitle(title)).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readByTitle(title));
    }

    @Test
    void book__readByTitle_NullSetFields__returnNull() {
        String title = insertBook1.getTitle();

        Mockito.when(bookService.readByTitle(title)).thenReturn(List.of(emptyInsertBook1));
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(null);
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readByTitle(title));
    }

    @Test
    void book__readAll__correctReturnListObject() {
        Mockito.when(bookService.readAll()).thenReturn(emptyBookList);
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(insertBook1.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));
        Mockito.when(authorService.readById(emptyInsertBook2.getAuthorId())).thenReturn(insertBook2.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook2.getGenreListId().get(0))).thenReturn(insertBook2.getGenreList().get(0));
        Mockito.when(authorService.readById(emptyInsertBook3.getAuthorId())).thenReturn(insertBook3.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook3.getGenreListId().get(0))).thenReturn(insertBook3.getGenreList().get(0));

        List<BookDTO> expected = bookList;
        List<BookDTO> actual = libraryService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_NullSetFields__correctReturnListObject() {
        Mockito.when(bookService.readAll()).thenReturn(emptyBookList);
        Mockito.when(authorService.readById(emptyInsertBook1.getAuthorId())).thenReturn(insertBook1.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(0))).thenReturn(insertBook1.getGenreList().get(0));
        Mockito.when(genreService.readById(emptyInsertBook1.getGenreListId().get(1))).thenReturn(insertBook1.getGenreList().get(1));
        Mockito.when(authorService.readById(emptyInsertBook2.getAuthorId())).thenReturn(insertBook2.getAuthor());
        Mockito.when(genreService.readById(emptyInsertBook2.getGenreListId().get(0))).thenReturn(insertBook2.getGenreList().get(0));
        Mockito.when(authorService.readById(emptyInsertBook3.getAuthorId())).thenReturn(null);
        Mockito.when(genreService.readById(emptyInsertBook3.getGenreListId().get(0))).thenReturn(insertBook3.getGenreList().get(0));

        List<BookDTO> expected = List.of(insertBook1, insertBook2);
        List<BookDTO> actual = libraryService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__readAll_NotFields__correctReturnEmptyList() {
        Mockito.when(bookService.readAll()).thenReturn(Collections.emptyList());
        Assertions.assertThrows(NullPointerException.class, () -> libraryService.readAll());
    }
}