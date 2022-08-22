package ru.baranova.spring.service.data.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.entity.comment.CommentDao;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.app.CheckService;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplCreateTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;
    private Comment testComment;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", new Date());
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", new Date());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__create__correctReturnNewObject() {
        String inputAuthor = testComment.getAuthor();
        String inputText = testComment.getText();
        Integer newId = commentList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(inputAuthor), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.TRUE);
        testComment.setId(newId);
        Mockito.when(commentDao.create(inputAuthor, inputText)).thenReturn(testComment);

        Comment expected = testComment;
        Comment actual = commentService.create(inputAuthor, inputText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__create_Exception__returnNull() {
        String inputAuthor = testComment.getAuthor();
        String inputText = testComment.getText();
        Integer newId = commentList.size() + 1;

        Mockito.when(checkService.doCheck(Mockito.eq(inputAuthor), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.TRUE);
        testComment.setId(newId);
        Mockito.when(commentDao.create(inputAuthor, inputText)).thenReturn(null);

        Assertions.assertNull(commentService.create(inputAuthor, inputText));
    }

    @Test
    void comment__create_IncorrectAuthor__returnNull() {
        String inputAuthor = "smth";
        String inputText = testComment.getText();

        Mockito.when(checkService.doCheck(Mockito.eq(inputAuthor), Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.TRUE);

        Assertions.assertNull(commentService.create(inputAuthor, inputText));
    }

    @Test
    void comment__create_IncorrectText__returnNull() {
        String inputAuthor = testComment.getAuthor();
        String inputText = "smth";

        Mockito.when(checkService.doCheck(Mockito.eq(inputAuthor), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(commentService.create(inputAuthor, inputText));
    }


}
