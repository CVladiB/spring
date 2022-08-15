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
class LibraryServiceImplCreateTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private LibraryServiceImpl libraryService;
    private Genre testGenre1;
    private Genre testGenre2;
    private List<Genre> genreList;
    private Author testAuthor;
    private List<Author> authorList;
    private BookDTO testBook;
    private BookEntity emptyTestBook;
    private List<BookDTO> bookList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);

        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre1 = new Genre(null, "Name1Test", "DescriptionTest");
        testGenre2 = new Genre(null, "Name2Test", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);

        BookDTO insertBook1 = new BookDTO(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        BookDTO insertBook2 = new BookDTO(2, "Title2", insertAuthor1, List.of(insertGenre2));
        BookDTO insertBook3 = new BookDTO(3, "Title3", insertAuthor2, List.of(insertGenre1));
        testBook = new BookDTO(null, "TitleTest", testAuthor, List.of(testGenre1, testGenre2));
        emptyTestBook = new BookEntity(null, "TitleTest", 3, List.of(3, 4));

        bookList = List.of(insertBook1, insertBook2, insertBook3);
    }

    @Test
    void book__create__correctReturnNewObject() {
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();
        Integer newId = bookList.size() + 1;

        testAuthor.setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        emptyTestBook.setId(newId);
        testBook.setId(newId);
        Mockito.when(bookService.create(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(emptyTestBook);

        BookDTO expected = testBook;
        BookDTO actual = libraryService.create(inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__create_NullAuthor__returnNull() {
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = "smth";
        String inputAuthorName = "smth";
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(null);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__create_NullGenre__returnNull() {
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = List.of("smth");

        testAuthor.setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__create_NullBook__returnNull() {
        String inputTitle = "smth";
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        testAuthor.setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        Mockito.when(bookService.create(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__createById__correctReturnNewObject() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();
        Integer newId = bookList.size() + 1;

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        emptyTestBook.setId(newId);
        testBook.setId(newId);
        Mockito.when(bookService.create(Mockito.eq(inputTitle), Mockito.any(), Mockito.any())).thenReturn(emptyTestBook);

        BookDTO expected = testBook;
        BookDTO actual = libraryService.create(inputTitle, inputAuthorId, inputGenreIdList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__createById_NullAuthor__returnNull() {
        testAuthor.setId(100);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__createById_NullGenre__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(100);
        testGenre2.setId(101);
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__createById_NullBook__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        Mockito.when(bookService.create(inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.create(inputTitle, inputAuthorId, inputGenreIdList));
    }
}