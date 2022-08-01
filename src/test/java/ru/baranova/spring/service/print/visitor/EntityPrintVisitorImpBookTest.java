package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
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
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImpBookTest {
    @Autowired
    private EntityPrintVisitor entityPrintVisitorImpl;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkServiceImpl;
    private Book testBook;


    @BeforeEach
    void setUp() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void book__print__correctOutput() {
        String expectedAuthor = String.format("%s %s.\r\n", testBook.getAuthor().getSurname()
                , testBook.getAuthor().getName().charAt(0));
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());

        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(true);
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\", %s, жанр: \r\n%s", testBook.getId(), testBook.getTitle()
                , expectedAuthor, expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullId__incorrectNPE() {
        testBook.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testBook));
    }

    @Test
    void book__print_NullTitle__incorrectNPE() {
        testBook.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testBook));
    }

    @Test
    void book__print_NullAuthorId__correctOutput() {
        testBook.getAuthor().setId(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorSurname__correctOutput() {
        testBook.getAuthor().setSurname(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorName__correctOutput() {
        testBook.getAuthor().setName(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthor__correctOutput() {
        testBook.setAuthor(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullGenre__correctOutput() {
        testBook.setGenreList(null);
        String expectedAuthor = String.format("%s %s.\r\n", testBook.getAuthor().getSurname()
                , testBook.getAuthor().getName().charAt(0));

        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBook.getAuthor())).thenReturn(true);

        String expected = String.format("%d. \"%s\", %s", testBook.getId(), testBook.getTitle()
                , expectedAuthor);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }
}