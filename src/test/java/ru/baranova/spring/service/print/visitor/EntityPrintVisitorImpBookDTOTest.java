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
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImpBookDTOTest {
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkService;
    private BookDTO testBook;


    @BeforeEach
    void setUp() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        testBook = new BookDTO(1, "TitleTest", testAuthor, List.of(testGenre));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void book__print__correctOutput() {
        String expectedAuthor = String.format("%s %s." + System.lineSeparator(), testBook.getAuthor().getSurname()
                , testBook.getAuthor().getName().charAt(0));
        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());

        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(printer).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\", %s, жанр: " + System.lineSeparator() + "%s", testBook.getId(), testBook.getTitle()
                , expectedAuthor, expectedGenre);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullId__incorrectNPE() {
        testBook.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> printer.print(testBook));
    }

    @Test
    void book__print_NullTitle__incorrectNPE() {
        testBook.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> printer.print(testBook));
    }

    @Test
    void book__print_NullAuthorId__correctOutput() {
        testBook.getAuthor().setId(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);

        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(printer).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"" + System.lineSeparator() + ", жанр: " + System.lineSeparator() + "%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorSurname__correctOutput() {
        testBook.getAuthor().setSurname(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);

        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(printer).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"" + System.lineSeparator() + ", жанр: " + System.lineSeparator() + "%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorName__correctOutput() {
        testBook.getAuthor().setName(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);

        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(printer).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"" + System.lineSeparator() + ", жанр: " + System.lineSeparator() + "%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthor__correctOutput() {
        testBook.setAuthor(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);

        String expectedGenre = String.format("%d. %s - %s", testBook.getGenreList().get(0).getId()
                , testBook.getGenreList().get(0).getName()
                , testBook.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(printer).print(testBook.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"" + System.lineSeparator() + ", жанр: " + System.lineSeparator() + "%s", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullGenre__correctOutput() {
        testBook.setGenreList(null);
        String expectedAuthor = String.format("%s %s." + System.lineSeparator(), testBook.getAuthor().getSurname()
                , testBook.getAuthor().getName().charAt(0));

        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.TRUE);


        String expected = String.format("%d. \"%s\", %s", testBook.getId(), testBook.getTitle()
                , expectedAuthor);
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}
