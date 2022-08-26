package ru.baranova.spring.controller.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.CommentShellController;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {CommentShellControllerTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class CommentShellControllerTest {
    @Autowired
    private CommentShellController commentShellController;
    @Autowired
    private CommentService commentService;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private CommentShellControllerTestConfig config;
    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment(7, "User", "Some Text BlaBla", LocalDate.now());
    }

    @Test
    void create_correct() {
        Mockito.when(commentService.create(comment.getAuthor(), comment.getText())).thenReturn(comment);
        String expected = String.format(config.getCOMPLETE_CREATE(), comment.getId());
        String actual = commentShellController.create(comment.getAuthor(), comment.getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrect() {
        Mockito.when(commentService.create(comment.getAuthor(), comment.getText())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.create(comment.getAuthor(), comment.getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Integer id = comment.getId();
        Mockito.when(commentService.readById(id)).thenReturn(comment);
        Mockito.doNothing().when(printer).print(comment);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = commentShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrectRead() {
        Integer id = comment.getId();
        Mockito.when(commentService.readById(id)).thenReturn(comment);
        Mockito.doThrow(NullPointerException.class).when(printer).print(comment);
        String expected = config.getWARNING();
        String actual = commentShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = comment.getId();
        Mockito.when(commentService.readById(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByAuthor_correct() {
        String inputAuthor = comment.getAuthor();
        Mockito.when(commentService.readByAuthorOfComment(inputAuthor))
                .thenReturn(List.of(comment));
        Mockito.doNothing().when(printer).print((Comment) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = commentShellController.readByName(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByAuthor_incorrectRead() {
        String inputAuthor = comment.getAuthor();
        Mockito.when(commentService.readByAuthorOfComment(inputAuthor)).thenReturn(List.of(comment));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Comment) Mockito.any());
        String expected = config.getWARNING();
        String actual = commentShellController.readByName(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByName_incorrect() {
        String inputAuthor = comment.getAuthor();
        Mockito.when(commentService.readByAuthorOfComment(inputAuthor)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.readByName(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        Comment comment1 = comment;
        Comment comment2 = new Comment(8, "NewUser", "Some Text BlaBla", LocalDate.now());
        Mockito.when(commentService.readAll())
                .thenReturn(List.of(comment1, comment2));
        Mockito.doNothing().when(printer).print((Comment) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = commentShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrectRead() {
        Comment comment1 = comment;
        Comment comment2 = new Comment(8, "NewUser", "Some Text BlaBla", LocalDate.now());
        Mockito.when(commentService.readAll()).thenReturn(List.of(comment1, comment2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Comment) Mockito.any());
        String expected = config.getWARNING();
        String actual = commentShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(commentService.readAll()).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        Mockito.when(commentService.update(comment.getId(), comment.getText())).thenReturn(comment);
        String expected = config.getCOMPLETE_UPDATE();
        String actual = commentShellController.update(comment.getId(), comment.getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrect() {
        Mockito.when(commentService.update(comment.getId(), comment.getText())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.update(comment.getId(), comment.getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = comment.getId();
        Mockito.when(commentService.delete(id)).thenReturn(true);
        String expected = config.getCOMPLETE_DELETE();
        String actual = commentShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = comment.getId();
        Mockito.when(commentService.delete(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = commentShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

}
