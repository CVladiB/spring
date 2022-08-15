package ru.baranova.spring.service.print;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

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
    private BookEntity testBookEntity;


    @BeforeEach
    void setUp() {
        testAuthor = new Author(1, "SurnameTest", "NameTest");
        testGenre = new Genre(1, "NameTest", "DescriptionTest");
        testBookEntity = new BookEntity(1, "TitleTest", testAuthor.getId(), List.of(testGenre.getId()));
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

        String expected = "Print author\r\n";
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

        String expected = "Print genre\r\n";
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__printEntity__correctOutput() {
        Mockito.doAnswer(invocationOnMock -> {
            config.getWriter().println("Print author");
            return null;
        }).when(printer).print(testBookEntity);
        printService.printEntity(testBookEntity);

        String expected = "Print author\r\n";
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }
}