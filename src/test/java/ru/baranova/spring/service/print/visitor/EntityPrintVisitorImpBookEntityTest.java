package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.BookEntity;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImpBookEntityTest {
    @Autowired
    private EntityPrintVisitor entityPrintVisitorImpl;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;

    private BookEntity testBook;


    @BeforeEach
    void setUp() {
        testBook = new BookEntity(1, "TitleTest", 1, List.of(1, 2));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void book__print__correctOutput() {
        String expectedAuthor = String.format("authorId: %s", testBook.getAuthorId());
        String expectedGenre = String.format("genreId: %s, %s,", testBook.getGenreListId().get(0), testBook.getGenreListId().get(1));

        String expected = String.format("%d. \"%s\", %s, %s \r\n", testBook.getId(), testBook.getTitle()
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
        testBook.setTitle(null);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testBook));
    }

    @Test
    void book__print_NullAuthorId__correctOutput() {
        testBook.setAuthorId(null);
        String expectedGenre = String.format("genreId: %s, %s,", testBook.getGenreListId().get(0), testBook.getGenreListId().get(1));

        String expected = String.format("%d. \"%s\", %s \r\n", testBook.getId(), testBook.getTitle()
                , expectedGenre);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullGenre__correctOutput() {
        testBook.setGenreListId(Collections.emptyList());
        String expectedAuthor = String.format("authorId: %s", testBook.getAuthorId());

        String expected = String.format("%d. \"%s\", %s\r\n", testBook.getId(), testBook.getTitle()
                , expectedAuthor);
        entityPrintVisitorImpl.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}