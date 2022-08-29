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
import java.util.Optional;

@SpringBootTest(classes = {CommentServiceImplTestConfig.class, StopSearchConfig.class})
class CommentServiceImplUpdateTest {
    @Autowired
    private CheckService checkService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;
    private Comment insertComment1;
    private Comment testComment;


    @BeforeEach
    void setUp() {
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
    }

    @Test
    void Comment__update__correctReturnObject() {
        String inputText = testComment.getText();
        Integer inputId = insertComment1.getId();

        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.of(insertComment1));
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.TRUE);
        testComment.setId(inputId);
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(testComment);

        Comment expected = testComment;
        Comment actual = commentService.update(inputId, inputText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void Comment__update_Exception__returnNull() {
        String inputText = testComment.getText();
        Integer inputId = insertComment1.getId();

        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.of(insertComment1));
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.TRUE);
        testComment.setId(inputId);
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(null);

        Assertions.assertNull(commentService.update(inputId, inputText));
    }

    @Test
    void Comment__update_IncorrectDescription__returnNull() {
        String inputText = "smth";
        Integer inputId = insertComment1.getId();
        Mockito.when(commentRepository.findById(inputId)).thenReturn(Optional.of(insertComment1));
        Mockito.when(checkService.doCheck(Mockito.eq(inputText), Mockito.any())).thenReturn(Boolean.FALSE);
        Assertions.assertNull(commentService.update(inputId, inputText));
    }
}
