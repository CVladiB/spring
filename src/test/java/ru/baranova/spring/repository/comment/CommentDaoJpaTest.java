package ru.baranova.spring.repository.comment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Comment;

import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

@DataJpaTest
@Import(value = {CommentDaoTestConfig.class, StopSearchConfig.class})
class CommentDaoJpaTest {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private CommentDaoTestConfig config;
    private Comment insertComment1;
    private Comment insertComment2;
    private Comment insertComment3;
    private Comment testComment;
    private List<Comment> commentList;


    @BeforeEach
    void setUp() {
        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", config.getDateWithoutTime());
        insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", config.getDateWithoutTime());
        insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", config.getDateWithoutTime());
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", new Date());
        commentList = List.of(insertComment1, insertComment2, insertComment3);
    }

    @Test
    void comment__create__correctReturnNewComment() {
        // todo
        List<Integer> listExistId = commentDao.getAll()
                .stream()
                .map(Comment::getId)
                .toList();

        Comment expected = testComment;
        Comment actual = commentDao.create(testComment.getAuthor(), testComment.getText());

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(expected.getText(), actual.getText());
    }

    @Test
    void comment__getById__correctReturnCommentById() {
        Integer id = insertComment1.getId();
        Comment expected = insertComment1;
        Comment actual = commentDao.getById(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getAuthor(), actual.getAuthor()),
                () -> Assertions.assertEquals(expected.getText(), actual.getText()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void comment__getById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertThrows(PersistenceException.class, () -> commentDao.getById(nonexistentId));
    }

    @Test
    void comment__getByAuthor__correctReturnCommentByName() {
        List<Comment> expected = List.of(insertComment1, insertComment2);
        List<Comment> actual = commentDao.getByAuthorOfComment(insertComment1.getAuthor());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__getByAuthor_NonexistentAuthor__incorrectException() {
        String nonexistentAuthor = "Name25";
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentDao.getByAuthorOfComment(nonexistentAuthor));
    }

    @Test
    void comment__getAll__returnListComment() {
        List<Comment> expected = commentList;
        List<Comment> actual = commentDao.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__getAll_Empty_emptyListResultException() {
        commentList.stream().map(Comment::getId).forEach(commentDao::delete);
        Assertions.assertThrows(DataAccessException.class, () -> commentDao.getAll());
    }

    @Test
    void comment__update__correctChangeAllFieldCommentById() {
        Integer id = insertComment1.getId();
        testComment.setId(id);
        Comment expected = testComment;
        Comment actual = commentDao.update(testComment.getId(), testComment.getText());
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void comment__update_NonexistentId__incorrectException() {
        testComment.setId(100);
        Assertions.assertThrows(PersistenceException.class,
                () -> commentDao.update(testComment.getId(), testComment.getText()));
    }

    @Test
    void comment__update_NullText__incorrectException() {
        testComment.setText(null);
        Assertions.assertThrows(PersistenceException.class,
                () -> commentDao.update(testComment.getId(), testComment.getText()));
    }

    @Test
    void comment__delete__correctDelete() {
        List<Comment> actualBeforeDelete = commentDao.getAll();
        Integer inputId = insertComment1.getId();
        Integer inputId2 = insertComment2.getId();
        Integer inputId3 = insertComment3.getId();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertTrue(commentDao.delete(inputId));
        Assertions.assertTrue(commentDao.delete(inputId2));
        Assertions.assertTrue(commentDao.delete(inputId3));
        Assertions.assertThrows(DataAccessException.class, () -> commentDao.getAll());
    }

    @Test
    void comment__delete_NonexistentId__notDelete() {
        List<Comment> actualBeforeDelete = commentDao.getAll();
        Integer inputId = actualBeforeDelete.size() + 1;
        Integer inputId2 = actualBeforeDelete.size() + 2;

        List<Comment> expected = actualBeforeDelete;
        List<Comment> actual = commentDao.getAll();

        Assertions.assertNotNull(actualBeforeDelete);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentDao.delete(inputId));
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> commentDao.delete(inputId2));
        Assertions.assertEquals(expected, actual);
    }
}