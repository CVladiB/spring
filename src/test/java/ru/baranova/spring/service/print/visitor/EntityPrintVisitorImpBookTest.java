package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
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
import ru.baranova.spring.service.app.CheckService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImpBookTest {
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkService;
    private Book testBook;


    @BeforeEach
    void setUp() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre, testGenre), List.of(testComment, testComment));
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void book__print__correctOutput() {
        String expectedTitle = String.format("%d. \"%s\"", testBook.getId(), testBook.getTitle());
        String expectedAuthor = String.format(", %s %s.", testBook.getAuthor().getSurname(), testBook.getAuthor().getName().charAt(0));
        String expectedGenre = String.format(", жанр: %s", testBook.getGenreList()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", ")));
        String expectedComment = "Комментарии:" + System.lineSeparator() + testBook.getCommentList()
                .stream()
                .map(comment -> String.format("%s. %s - %s", comment.getDate(), comment.getAuthor(), comment.getText()))
                .collect(Collectors.joining(System.lineSeparator()));

        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(0)), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(1)), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = expectedTitle + expectedAuthor + expectedGenre + System.lineSeparator() + expectedComment + System.lineSeparator();
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
        String expectedTitle = String.format("%d. \"%s\"", testBook.getId(), testBook.getTitle());
        String expectedGenre = String.format(", жанр: %s", testBook.getGenreList()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", ")));
        String expectedComment = "Комментарии:" + System.lineSeparator() + testBook.getCommentList()
                .stream()
                .map(comment -> String.format("%s. %s - %s", comment.getDate(), comment.getAuthor(), comment.getText()))
                .collect(Collectors.joining(System.lineSeparator()));

        testBook.getAuthor().setId(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(0)), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(1)), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = expectedTitle + expectedGenre + System.lineSeparator() + expectedComment + System.lineSeparator();
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__print_NullAuthor__correctOutput() {
        String expectedTitle = String.format("%d. \"%s\"", testBook.getId(), testBook.getTitle());
        String expectedGenre = String.format(", жанр: %s", testBook.getGenreList()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", ")));
        String expectedComment = "Комментарии:" + System.lineSeparator() + testBook.getCommentList()
                .stream()
                .map(comment -> String.format("%s. %s - %s", comment.getDate(), comment.getAuthor(), comment.getText()))
                .collect(Collectors.joining(System.lineSeparator()));

        testBook.setAuthor(null);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(0)), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(1)), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = expectedTitle + expectedGenre + System.lineSeparator() + expectedComment + System.lineSeparator();
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullGenre__correctOutput() {
        String expectedTitle = String.format("%d. \"%s\"", testBook.getId(), testBook.getTitle());
        String expectedAuthor = String.format(", %s %s.", testBook.getAuthor().getSurname(), testBook.getAuthor().getName().charAt(0));
        String expectedComment = "Комментарии:" + System.lineSeparator() + testBook.getCommentList()
                .stream()
                .map(comment -> String.format("%s. %s - %s", comment.getDate(), comment.getAuthor(), comment.getText()))
                .collect(Collectors.joining(System.lineSeparator()));

        testBook.setGenreList(Collections.emptyList());
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(0)), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getCommentList().get(1)), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = expectedTitle + expectedAuthor + System.lineSeparator() + expectedComment + System.lineSeparator();
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printNullComment__correctOutput() {
        String expectedTitle = String.format("%d. \"%s\"", testBook.getId(), testBook.getTitle());
        String expectedAuthor = String.format(", %s %s.", testBook.getAuthor().getSurname(), testBook.getAuthor().getName().charAt(0));
        String expectedGenre = String.format(", жанр: %s", testBook.getGenreList()
                .stream()
                .map(Genre::getName)
                .collect(Collectors.joining(", ")));

        testBook.setCommentList(Collections.emptyList());
        Mockito.when(checkService.doCheck(Mockito.eq(testBook.getAuthor()), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = expectedTitle + expectedAuthor + expectedGenre + System.lineSeparator();
        printer.print(testBook);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}
