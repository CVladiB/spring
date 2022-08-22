package ru.baranova.spring.dao.entity.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentDaoJpa implements CommentDao {
    private final EntityManager em;
    private BusinessConstants.DaoLog bc;

    @Override
    public Comment create(String author, String text) throws DataAccessException, PersistenceException {
        Comment comment = new Comment(null, author, text, null);
        em.persist(comment);
        return comment;
    }

    @Override
    public Comment getById(Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                select c from Comment c
                where c.id = :id
                """;
        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);
        query.setParameter("id", id);
        Comment comment = query.getSingleResult();
        if (comment == null) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return comment;
    }

    @Override
    public List<Comment> getByAuthorOfComment(String author) throws DataAccessException, PersistenceException {
        String sql = """
                select c from Comment c
                where c.author = :author
                """;
        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);
        query.setParameter("author", author);
        List<Comment> commentList = query.getResultList();
        if (commentList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return commentList;
    }

    @Override
    public List<Comment> getAll() throws DataAccessException, PersistenceException {
        String sql = """
                select c from Comment c
                """;
        TypedQuery<Comment> query = em.createQuery(sql, Comment.class);
        List<Comment> commentList = query.getResultList();
        if (commentList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.NOTHING_IN_BD);
        }
        return commentList;
    }

    @Override
    public Comment update(Integer id, String text) throws DataAccessException, PersistenceException {
        Comment comment = getById(id);
        String sql = """
                update Comment c
                set c.text = :text
                where c.id = :id
                """;
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        query.setParameter("text", text);
        int countAffectedRows = query.executeUpdate();
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        comment.setText(text);
        return comment;
    }

    @Override
    public Boolean delete(Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                delete from Comment c
                where c.id = :id
                """;
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        int countAffectedRows = query.executeUpdate();
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return countAffectedRows > 0;
    }
}
