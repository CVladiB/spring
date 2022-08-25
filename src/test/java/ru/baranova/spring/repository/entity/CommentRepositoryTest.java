package ru.baranova.spring.repository.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.baranova.spring.model.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class CommentRepositoryTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private CommentRepository commentRepository;
    private Comment insertComment1;
    private Comment insertComment2;
    private Comment insertComment3;
    private Comment insertComment4;
    private Comment testComment;
    private List<Comment> commentList;
    private Date dateWithoutTime;


    @BeforeEach
    void setUp() throws ParseException {
        dateWithoutTime = sdf.parse(sdf.format(new Date()));
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
        insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
        insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", dateWithoutTime);
        insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", dateWithoutTime);
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", new Date());
        commentList = List.of(insertComment1, insertComment2, insertComment3, insertComment4);
    }

    @Test
    void comment__create__correctReturnNewComment() {
        List<Integer> listExistId = commentRepository.findAll()
                .stream()
                .map(Comment::getId)
                .toList();

        Comment expected = testComment;
        Comment actual = commentRepository.save(testComment);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void comment__findById__correctReturnCommentById() {
        Integer id = insertComment1.getId();
        Comment expected = insertComment1;
        Comment actual = commentRepository.findById(id).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getAuthor(), actual.getAuthor()),
                () -> Assertions.assertEquals(expected.getText(), actual.getText()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void comment__findById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertEquals(Optional.empty(), commentRepository.findById(nonexistentId));
    }

    @Test
    void comment__getByAuthor__correctReturnCommentByName() {
        List<Comment> expected = List.of(insertComment1, insertComment2, insertComment4);
        List<Comment> actual = commentRepository.getByAuthorOfComment(insertComment1.getAuthor());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__getByAuthor_NonexistentAuthor__incorrectException() {
        String nonexistentAuthor = "Name25";
        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentRepository.getByAuthorOfComment(nonexistentAuthor);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__getAll__returnListComment() {
        List<Comment> expected = commentList;
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__getAll_Empty_emptyListResultException() {
        commentRepository.deleteAll(commentList);
        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__update__correctChangeAllFieldCommentById() {
        Integer id = insertComment1.getId();
        testComment.setId(id);
        Comment expected = testComment;
        Comment actual = commentRepository.save(testComment);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__update_NonexistentId__incorrectException() {
        testComment.setId(100);
        Comment actual = commentRepository.save(testComment);

        testComment.setId(commentList.size() + 2);
        Comment expected = testComment;

        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void comment__update_NullText__incorrectException() {
        testComment.setId(insertComment1.getId());
        testComment.setText(null);
        Comment expected = testComment;
        Comment actual = commentRepository.save(testComment);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__delete__correctDelete() {
        List<Comment> actualBeforeDelete = commentRepository.findAll();
        Assertions.assertNotNull(actualBeforeDelete);

        commentRepository.delete(insertComment1);
        commentRepository.delete(insertComment2);
        commentRepository.delete(insertComment3);
        commentRepository.delete(insertComment4);

        List<Comment> expected = Collections.emptyList();
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__delete_NonexistentId__notDelete() {
        List<Comment> expected = commentRepository.findAll();
        Assertions.assertNotNull(expected);
        commentRepository.delete(new Comment(100, "a", "t", new Date()));
        List<Comment> actual = commentRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
