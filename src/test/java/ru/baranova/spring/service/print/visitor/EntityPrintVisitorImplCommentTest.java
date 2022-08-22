package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.app.CheckService;

import java.util.Date;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImplCommentTest {
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkService;


    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void comment__print__correctOutput() {
        Comment testComment = new Comment(1, "TestCommentAuthor", "TestBlaBlaBla", new Date());

        Mockito.when(checkService.doCheck(Mockito.eq(testComment), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = String.format("%s. %s - %s" + System.lineSeparator(), testComment.getDate(), testComment.getAuthor(), testComment.getText());
        printer.print(testComment);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__print_NullId__incorrectNPE() {
        Comment testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", new Date());
        Mockito.when(checkService.doCheck(Mockito.eq(testComment), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(NullPointerException.class, () -> printer.print(testComment));
    }

    @Test
    void comment__print_NullGenre__incorrectNPE() {
        Comment testComment = null;
        Mockito.when(checkService.doCheck(Mockito.eq(testComment), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(NullPointerException.class, () -> printer.print(testComment));
    }
}
