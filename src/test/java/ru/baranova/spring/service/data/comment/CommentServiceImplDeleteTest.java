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
import ru.baranova.spring.service.app.CheckService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplDeleteTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        Comment insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        Comment insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        Comment insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", LocalDate.now());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__delete__true() {
        Integer inputId = commentList.size();
        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.of(commentList.get(1)));
        Mockito.doNothing().when(commentRepository).delete(commentList.get(1));
        Assertions.assertTrue(commentService.delete(inputId));
    }

    @Test
    void comment__delete_NonexistentId__false() {
        Integer inputId = commentList.size();
        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertFalse(commentService.delete(inputId));
    }
}
