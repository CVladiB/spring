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
class EntityPrintVisitorImpBookTestEntity {
    @Autowired
    private EntityPrintVisitor entityPrintVisitorImpl;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkServiceImpl;
    private BookDTO testBookEntity;


    @BeforeEach
    void setUp() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        testBookEntity = new BookDTO(1, "TitleTest", testAuthor, List.of(testGenre));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void book__print__correctOutput() {
        String expectedAuthor = String.format("%s %s.\r\n", testBookEntity.getAuthor().getSurname()
                , testBookEntity.getAuthor().getName().charAt(0));
        String expectedGenre = String.format("%d. %s - %s", testBookEntity.getGenreList().get(0).getId()
                , testBookEntity.getGenreList().get(0).getName()
                , testBookEntity.getGenreList().get(0).getDescription());

        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(true);
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBookEntity.getGenreList().get(0));

        String expected = String.format("%d. \"%s\", %s, жанр: \r\n%s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedAuthor, expectedGenre);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullId__incorrectNPE() {
        testBookEntity.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testBookEntity));
    }

    @Test
    void book__print_NullTitle__incorrectNPE() {
        testBookEntity.setId(null);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testBookEntity));
    }

    @Test
    void book__print_NullAuthorId__correctOutput() {
        testBookEntity.getAuthor().setId(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBookEntity.getGenreList().get(0).getId()
                , testBookEntity.getGenreList().get(0).getName()
                , testBookEntity.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBookEntity.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorSurname__correctOutput() {
        testBookEntity.getAuthor().setSurname(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBookEntity.getGenreList().get(0).getId()
                , testBookEntity.getGenreList().get(0).getName()
                , testBookEntity.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBookEntity.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthorName__correctOutput() {
        testBookEntity.getAuthor().setName(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBookEntity.getGenreList().get(0).getId()
                , testBookEntity.getGenreList().get(0).getName()
                , testBookEntity.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBookEntity.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthor__correctOutput() {
        testBookEntity.setAuthor(null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(false);
        String expectedGenre = String.format("%d. %s - %s", testBookEntity.getGenreList().get(0).getId()
                , testBookEntity.getGenreList().get(0).getName()
                , testBookEntity.getGenreList().get(0).getDescription());
        Mockito.doAnswer(invocation -> {
            config.getWriter().printf(expectedGenre);
            return null;
        }).when(entityPrintVisitorImpl).print(testBookEntity.getGenreList().get(0));

        String expected = String.format("%d. \"%s\"\r\n, жанр: \r\n%s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullGenre__correctOutput() {
        testBookEntity.setGenreList(null);
        String expectedAuthor = String.format("%s %s.\r\n", testBookEntity.getAuthor().getSurname()
                , testBookEntity.getAuthor().getName().charAt(0));

        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testBookEntity.getAuthor())).thenReturn(true);

        String expected = String.format("%d. \"%s\", %s", testBookEntity.getId(), testBookEntity.getTitle()
                , expectedAuthor);
        entityPrintVisitorImpl.print(testBookEntity);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }
}