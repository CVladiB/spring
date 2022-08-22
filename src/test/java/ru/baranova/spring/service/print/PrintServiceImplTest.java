package ru.baranova.spring.service.print;

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
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {PrintServiceImplTestConfig.class, StopSearchConfig.class})
class PrintServiceImplTest {
    @Autowired
    private PrintService printService;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private PrintServiceImplTestConfig config;
    private Author testAuthor;
    private Genre testGenre;
    private Comment testComment;
    private Book testBook;


    @BeforeEach
    void setUp() {
        testAuthor = new Author(1, "SurnameTest", "NameTest");
        testGenre = new Genre(1, "NameTest", "DescriptionTest");
        testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", new Date());
        testBook = new Book(1, "TitleTest", testAuthor, List.of(testGenre), null);
    }

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void author__printEntity__correctOutput() {
        Mockito.doAnswer(invocationOnMock -> {
            config.getWriter().println("Print author");
            return null;
        }).when(printer).print(testAuthor);
        printService.printEntity(testAuthor);

        String expected = "Print author" + System.lineSeparator();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__printEntity__correctOutput() {
        Mockito.doAnswer(invocationOnMock -> {
            config.getWriter().println("Print genre");
            return null;
        }).when(printer).print(testGenre);
        printService.printEntity(testGenre);

        String expected = "Print genre" + System.lineSeparator();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__printEntity__correctOutput() {
        Mockito.doAnswer(invocationOnMock -> {
            config.getWriter().println("Print comment");
            return null;
        }).when(printer).print(testComment);
        printService.printEntity(testComment);

        String expected = "Print comment" + System.lineSeparator();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printEntity__correctOutput() {
        Mockito.doAnswer(invocationOnMock -> {
            config.getWriter().println("Print author");
            return null;
        }).when(printer).print(testBook);
        printService.printEntity(testBook);

        String expected = "Print author" + System.lineSeparator();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}
