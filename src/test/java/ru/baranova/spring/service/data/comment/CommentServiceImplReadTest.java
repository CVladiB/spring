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

import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplReadTest {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;

    private Comment insertComment1;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", new Date());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", new Date());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__readById__correctReturnObject() {
        Integer inputId = commentList.size();

        Mockito.when(commentDao.getById(inputId)).thenReturn(commentList.get(inputId - 1));

        Comment expected = commentList.get(inputId - 1);
        Comment actual = commentService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readById_NonexistentId__returnNull() {
        Integer inputId = commentList.size() + 1;
        Assertions.assertNull(commentService.readById(inputId));
    }

    @Test
    void comment__readById_Exception__returnNull() {
        Integer inputId = commentList.size();
        Mockito.when(commentDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(commentService.readById(inputId));
    }


    @Test
    void comment__readByAuthor__correctReturnObject() {
        String inputAuthor = insertComment1.getAuthor();

        Mockito.when(commentDao.getByAuthorOfComment(inputAuthor)).thenReturn(List.of(commentList.get(0), commentList.get(1)));

        List<Comment> expected = List.of(commentList.get(0), commentList.get(1));
        List<Comment> actual = commentService.readByAuthorOfComment(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readByAuthor_NonexistentName__returnEmptyList() {
        String inputAuthor = "smth";
        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentService.readByAuthorOfComment(inputAuthor);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readByAuthor_Exception__returnEmptyList() {
        String inputAuthor = insertComment1.getAuthor();
        Mockito.when(commentDao.getByAuthorOfComment(inputAuthor)).thenReturn(Collections.emptyList());

        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentService.readByAuthorOfComment(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void comment__readAll__correctReturnLstObject() {
        Mockito.doReturn(commentList).when(commentDao).getAll();
        List<Comment> expected = commentList;
        List<Comment> actual = commentService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readAll_Exception__returnEmptyList() {
        Mockito.when(commentDao.getAll()).thenReturn(Collections.emptyList());
        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentService.readAll();
        Assertions.assertEquals(expected, actual);
    }


}
