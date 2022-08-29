package ru.baranova.spring.service.data.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.repository.entity.CommentRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplReadTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    private Comment insertComment1;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", LocalDate.now());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__readById__correctReturnObject() {
        Integer inputId = commentList.size();

        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.of(commentList.get(inputId - 1)));

        Comment expected = commentList.get(inputId - 1);
        Comment actual = commentService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readById_NonexistentId__returnNull() {
        Integer inputId = commentList.size() + 1;
        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertNull(commentService.readById(inputId));
    }

    @Test
    void comment__readById_Exception__returnNull() {
        Integer inputId = commentList.size();
        Mockito.when(commentRepository.findById(inputId)).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class, () -> commentService.readById(inputId));
    }


    @Test
    void comment__readByAuthor__correctReturnObject() {
        String inputAuthor = insertComment1.getAuthor();

        Mockito.when(commentRepository.getByAuthorOfComment(inputAuthor)).thenReturn(List.of(commentList.get(0), commentList.get(1)));

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
        Mockito.when(commentRepository.getByAuthorOfComment(inputAuthor)).thenReturn(Collections.emptyList());

        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentService.readByAuthorOfComment(inputAuthor);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void comment__readAll__correctReturnLstObject() {
        Mockito.doReturn(commentList).when(commentRepository).findAll();
        List<Comment> expected = commentList;
        List<Comment> actual = commentService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__readAll_Exception__returnEmptyList() {
        Mockito.when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentService.readAll();
        Assertions.assertEquals(expected, actual);
    }


}
