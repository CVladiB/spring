package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class})
class LibraryServiceImplUpdateTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private LibraryService libraryService;
    private Genre testGenre1;
    private Genre testGenre2;
    private List<Genre> genreList;
    private Author testAuthor;
    private List<Author> authorList;
    private BookDTO insertBook1;
    private BookEntity emptyInsertBook1;
    private BookDTO testBook;
    private BookEntity emptyTestBook;

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

        insertBook1 = new BookDTO(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        emptyInsertBook1 = new BookEntity(1, "Title1", 1, List.of(1, 2));
        testBook = new BookDTO(null, "TitleTest", testAuthor, List.of(testGenre1, testGenre2));
        emptyTestBook = new BookEntity(null, "TitleTest", 3, List.of(3, 4));
    }

    @Test
    void book__update__correctReturnNewObject() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.doReturn(testAuthor)
                .when(libraryService).checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        testBook.getGenreList().get(0).setId(genreList.size() + 1);
        testBook.getGenreList().get(1).setId(genreList.size() + 2);
        Mockito.doReturn(List.of(testGenre1, testGenre2))
                .when(libraryService).checkAndCreateGenreForBook(inputGenreNameList);
        testBook.setId(inputId);
        emptyTestBook.setId(inputId);
        Mockito.when(bookService.update(inputId, inputTitle, testBook.getAuthor().getId()
                        , testBook.getGenreList().stream().map(Genre::getId).toList()))
                .thenReturn(emptyTestBook);


        BookDTO expected = testBook;
        BookDTO actual = libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NullAuthor__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = "smth";
        String inputAuthorName = "smth";
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        Mockito.doReturn(null)
                .when(libraryService).checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        testBook.getGenreList().get(0).setId(genreList.size() + 1);
        testBook.getGenreList().get(1).setId(genreList.size() + 2);
        Mockito.doReturn(List.of(testGenre1, testGenre2))
                .when(libraryService).checkAndCreateGenreForBook(inputGenreNameList);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullGenre__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = List.of("smth");

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.doReturn(testAuthor)
                .when(libraryService).checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        Mockito.doReturn(Collections.emptyList())
                .when(libraryService).checkAndCreateGenreForBook(inputGenreNameList);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullBookById__returnNull() {
        Integer inputId = 100;
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(null);
        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.doReturn(testAuthor)
                .when(libraryService).checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        testBook.getGenreList().get(0).setId(genreList.size() + 1);
        testBook.getGenreList().get(1).setId(genreList.size() + 2);
        Mockito.doReturn(List.of(testGenre1, testGenre2))
                .when(libraryService).checkAndCreateGenreForBook(inputGenreNameList);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullBook__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = "smth";
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.doReturn(testAuthor)
                .when(libraryService).checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        testBook.getGenreList().get(0).setId(genreList.size() + 1);
        testBook.getGenreList().get(1).setId(genreList.size() + 2);
        Mockito.doReturn(List.of(testGenre1, testGenre2))
                .when(libraryService).checkAndCreateGenreForBook(inputGenreNameList);
        Mockito.when(bookService.update(Mockito.eq(inputId), Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(null);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__updateById__correctReturnNewObject() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        Mockito.when(bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList))
                .thenReturn(emptyTestBook);
        Mockito.doReturn(testBook)
                .when(libraryService).checkAndSetFieldsToBook(emptyTestBook);

        testBook.setId(inputId);
        emptyTestBook.setId(inputId);
        BookDTO expected = testBook;
        BookDTO actual = libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__updateById_NullAuthor__returnNull() {
        testAuthor.setId(100);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullGenre__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(100);
        testGenre2.setId(101);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(null);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullBookById__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = testBook.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(null);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullBook__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(emptyInsertBook1);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        Mockito.when(bookService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList)).thenReturn(null);

        Assertions.assertNull(libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }
}