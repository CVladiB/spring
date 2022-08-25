package ru.baranova.spring.service.data.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.repository.entity.comment.CommentDao;
import ru.baranova.spring.service.app.CheckService;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplDeleteTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", new Date());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__delete__true() {
        Integer inputId = commentList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(commentDao.delete(inputId)).thenReturn(true);
        Assertions.assertTrue(commentService.delete(inputId));
    }

    @Test
    void comment__delete_Exception__false() {
        Integer inputId = commentList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(commentDao.delete(inputId)).thenReturn(false);
        Assertions.assertFalse(commentService.delete(inputId));
    }

    @Test
    void comment__delete_NonexistentId__false() {
        Integer inputId = commentList.size() + 1;
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.FALSE);
        Assertions.assertFalse(commentService.delete(inputId));
    }

    @Test
    void comment__delete_ExistNonexistentId__false() {
        Integer inputId = commentList.size();
        Mockito.when(checkService.doCheck(Mockito.any(), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(commentDao.delete(inputId)).thenReturn(false);
        Assertions.assertFalse(commentService.delete(inputId));
    }


}
