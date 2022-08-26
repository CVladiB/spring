package ru.baranova.spring.controller.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;

import java.time.LocalDate;

@SpringBootTest(classes = {CommentShellControllerTestConfig.class, StopSearchConfig.class})
class CommentShellControllerKeyTest {
    @Autowired
    private Shell shell;
    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentShellControllerTestConfig config;
    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment(7, "User", "SomeTextBlaBla", LocalDate.now());
    }

    @Test
    void create_correctKey() {
        shell.evaluate(() -> config.getCreate() + " " + comment.getAuthor() + " " + comment.getText());
        Mockito.verify(commentService).create(comment.getAuthor(), comment.getText());
    }

    @Test
    void create_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + comment.getAuthor() + " " + comment.getText());
        Mockito.verify(commentService, Mockito.never()).create(Mockito.any(), Mockito.any());
    }

    @Test
    void readById_correctKey() {
        Integer id = comment.getId();
        shell.evaluate(() -> config.getReadById() + " " + id);
        Mockito.verify(commentService).readById(id);
    }

    @Test
    void readById_incorrectKey() {
        Integer id = comment.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(commentService, Mockito.never()).readById(Mockito.any());
    }

    @Test
    void readBySurnameAndName_correctKey() {
        shell.evaluate(() -> config.getReadByAuthor() + " " + comment.getAuthor());
        Mockito.verify(commentService).readByAuthorOfComment(comment.getAuthor());
    }

    @Test
    void readBySurnameAndName_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + comment.getAuthor());
        Mockito.verify(commentService, Mockito.never()).readByAuthorOfComment(Mockito.any());
    }

    @Test
    void readAll_correctKey() {
        shell.evaluate(() -> config.getReadAll());
        Mockito.verify(commentService).readAll();
    }

    @Test
    void readAll_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(commentService, Mockito.never()).readAll();
    }

    @Test
    void update_correctKey() {
        shell.evaluate(() -> config.getUpdate() + " " + comment.getId() + " " + comment.getText());
        Mockito.verify(commentService).update(comment.getId(), comment.getText());
    }

    @Test
    void update_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + comment.getId() + " " + comment.getText());
        Mockito.verify(commentService, Mockito.never()).update(Mockito.any(), Mockito.any());
    }

    @Test
    void delete_correctKey() {
        Integer id = comment.getId();
        shell.evaluate(() -> config.getDelete() + " " + id);
        Mockito.verify(commentService).delete(id);
    }

    @Test
    void delete_incorrectKey() {
        Integer id = comment.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(commentService, Mockito.never()).delete(Mockito.any());
    }
}
