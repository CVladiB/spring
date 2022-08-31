package ru.baranova.spring.controller.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.CommentRestController;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = {StopSearchConfig.class, CommentRestController.class})
class CommentRestControllerTest {
    @MockBean
    private CommentService commentService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Comment comment;
    private Comment insertComment1;
    private Comment insertComment2;
    private Comment insertComment3;
    private Comment insertComment4;
    private Comment testComment;
    private List<Comment> commentList;
    private LocalDate localDate;


    @BeforeEach
    void setUp() throws ParseException {
        localDate = LocalDate.now();
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", localDate);
        insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", localDate);
        insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", localDate);
        insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", localDate);
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", localDate);
        commentList = List.of(insertComment1, insertComment2, insertComment3, insertComment4);
        comment = new Comment(7, "User", "Some Text BlaBla", LocalDate.now());
    }

    @Test
    void create_correct() throws Exception {
        Mockito.when(commentService.create(comment.getAuthor(), comment.getText())).thenReturn(comment);
        mvc.perform(put("/comment/cc")
                        .param("author", comment.getAuthor())
                        .param("text", comment.getText()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Test
    void readById_correct() throws Exception {
        Mockito.when(commentService.readById(insertComment1.getId())).thenReturn(insertComment1);
        mvc.perform(get("/comment/cr-id").param("id", insertComment1.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(insertComment1)));
    }

    @Test
    void readByAuthor_correct() throws Exception {
        Mockito.when(commentService.readByAuthorOfComment(insertComment1.getAuthor())).thenReturn(List.of(insertComment1));
        mvc.perform(get("/comment/cr")
                        .param("author", insertComment1.getAuthor())
                        .param("text", insertComment1.getText()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(insertComment1))));
    }


    @Test
    void readAll_correct() throws Exception {
        Mockito.when(commentService.readAll()).thenReturn(commentList);
        mvc.perform(get("/comment/cr-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentList)));
    }

    @Test
    void update_correct() throws Exception {
        comment.setId(insertComment1.getId());
        Mockito.when(commentService.update(comment.getId(), comment.getText())).thenReturn(comment);
        mvc.perform(patch("/comment/cu")
                        .param("id", comment.getId().toString())
                        .param("author", comment.getAuthor())
                        .param("text", comment.getText()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));
    }

    @Test
    void delete_correct() throws Exception {
        mvc.perform(delete("/comment/cd").param("id", insertComment1.getId().toString()))
                .andExpect(status().isOk());
        Mockito.verify(commentService, Mockito.times(1)).delete(insertComment1.getId());
    }

}
