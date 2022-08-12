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
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class})
class LibraryServiceImplOtherMethodsTest {
    @Autowired
    private AuthorService authorServiceImpl;
    @Autowired
    private GenreService genreServiceImpl;
    @Autowired
    private LibraryService libraryServiceImpl;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private Genre testGenre1;
    private Genre testGenre2;
    private List<Genre> genreList;
    private Author testAuthor;
    private List<Author> authorList;
    private BookDTO insertBook1;
    private BookEntity emptyInsertBook1;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);

        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre1 = new Genre(null, "Name1Test", "DescriptionTest");
        testGenre2 = new Genre(null, "Name2Test", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);

        insertBook1 = new BookDTO(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2));
        emptyInsertBook1 = new BookEntity(1, "Title1", 1, List.of(1, 2));
    }

    @Test
    void author__checkAndCreateAuthorForBook__returnNewAuthor() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor2.getName();

        Mockito.when(authorServiceImpl.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(Collections.emptyList());
        testAuthor.setId(authorList.size() + 1);
        testAuthor.setName(inputAuthorSurname);
        testAuthor.setName(inputAuthorName);
        Mockito.when(authorServiceImpl.create(inputAuthorSurname, inputAuthorName))
                .thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = libraryServiceImpl.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__checkAndCreateAuthorForBook_ErrorCreate__returnNull() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor2.getName();

        Mockito.when(authorServiceImpl.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(Collections.emptyList());
        testAuthor.setId(authorList.size() + 1);
        testAuthor.setName(inputAuthorSurname);
        testAuthor.setName(inputAuthorName);
        Mockito.when(authorServiceImpl.create(inputAuthorSurname, inputAuthorName)).thenReturn(null);

        Assertions.assertNull(libraryServiceImpl.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName));
    }

    @Test
    void author__checkAndCreateAuthorForBook__returnOneExistAuthor() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor1.getName();

        Mockito.when(authorServiceImpl.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(List.of(insertAuthor1));

        Author expected = insertAuthor1;
        Author actual = libraryServiceImpl.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__checkAndCreateAuthorForBook_ManyAuthors__returnNull() {
        testAuthor.setId(3);
        testAuthor.setSurname(insertAuthor1.getSurname());
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = "-";

        Mockito.when(authorServiceImpl.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(List.of(insertAuthor1, testAuthor));

        Assertions.assertNull(libraryServiceImpl.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName));
    }

    @Test
    void genre__checkAndCreateGenreForBook__returnNewGenre() {
        List<String> inputGenreNameList = Stream.of(testGenre1, testGenre2).map(Genre::getName).toList();

        Mockito.when(genreServiceImpl.readByName(testGenre1.getName())).thenReturn(null);
        Mockito.when(genreServiceImpl.readByName(testGenre2.getName())).thenReturn(null);
        Mockito.when(genreServiceImpl.create(testGenre1.getName(), null)).thenReturn(testGenre1);
        Mockito.when(genreServiceImpl.create(testGenre2.getName(), null)).thenReturn(testGenre2);

        List<Genre> expected = List.of(testGenre1, testGenre2);
        List<Genre> actual = libraryServiceImpl.checkAndCreateGenreForBook(inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__checkAndCreateGenreForBook_ErrorCreate__returnEmptyList() {
        List<String> inputGenreNameList = Stream.of(testGenre1, testGenre2).map(Genre::getName).toList();

        Mockito.when(genreServiceImpl.readByName(testGenre1.getName())).thenReturn(null);
        Mockito.when(genreServiceImpl.readByName(testGenre2.getName())).thenReturn(null);
        Mockito.when(genreServiceImpl.create(testGenre1.getName(), null)).thenReturn(null);
        Mockito.when(genreServiceImpl.create(testGenre2.getName(), null)).thenReturn(null);

        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = libraryServiceImpl.checkAndCreateGenreForBook(inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__checkAndCreateGenreForBook__returnExistGenre() {
        List<String> inputGenreNameList = genreList.stream().map(Genre::getName).toList();
        Mockito.when(genreServiceImpl.readByName(genreList.get(0).getName())).thenReturn(genreList.get(0));
        Mockito.when(genreServiceImpl.readByName(genreList.get(1).getName())).thenReturn(genreList.get(1));

        List<Genre> expected = genreList;
        List<Genre> actual = libraryServiceImpl.checkAndCreateGenreForBook(inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book_checkAndSetFieldsToBook__returnFilledBook() {
        BookEntity inputBookEntity = emptyInsertBook1;

        Mockito.when(authorServiceImpl.readById(inputBookEntity.getAuthorId())).thenReturn(insertBook1.getAuthor());
        for (int i = 0; i < inputBookEntity.getGenreListId().size(); i++) {
            Mockito.when(genreServiceImpl.readById(inputBookEntity.getGenreListId().get(i)))
                    .thenReturn(insertBook1.getGenreList().get(i));
        }

        BookDTO expected = insertBook1;
        BookDTO actual = libraryServiceImpl.checkAndSetFieldsToBook(inputBookEntity);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book_checkAndSetFieldsToBook_ErrorAuthor__returnNull() {
        emptyInsertBook1.setAuthorId(100);
        BookEntity inputBookEntity = emptyInsertBook1;
        Mockito.when(authorServiceImpl.readById(inputBookEntity.getAuthorId())).thenReturn(null);
        Assertions.assertNull(libraryServiceImpl.checkAndSetFieldsToBook(inputBookEntity));

    }

    @Test
    void book_checkAndSetFieldsToBook_ErrorGenre__returnNull() {
        emptyInsertBook1.setGenreListId(List.of(100));
        BookEntity inputBookEntity = emptyInsertBook1;

        Mockito.when(authorServiceImpl.readById(inputBookEntity.getAuthorId())).thenReturn(insertBook1.getAuthor());
        Mockito.when(genreServiceImpl.readById(inputBookEntity.getGenreListId().get(0))).thenReturn(null);

        Assertions.assertNull(libraryServiceImpl.checkAndSetFieldsToBook(inputBookEntity));
    }

    @Test
    void book_checkAndSetFieldsToBook__returnNull() {
        BookEntity inputBookEntity = emptyInsertBook1;

        Mockito.when(authorServiceImpl.readById(inputBookEntity.getAuthorId())).thenReturn(insertBook1.getAuthor());
        for (int i = 0; i < inputBookEntity.getGenreListId().size(); i++) {
            Mockito.when(genreServiceImpl.readById(inputBookEntity.getGenreListId().get(i)))
                    .thenReturn(insertBook1.getGenreList().get(0));
        }

        Assertions.assertNull(libraryServiceImpl.checkAndSetFieldsToBook(inputBookEntity));
    }
}