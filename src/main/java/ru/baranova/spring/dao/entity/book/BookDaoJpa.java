package ru.baranova.spring.dao.entity.book;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookDaoJpa implements BookDao {
    @PersistenceContext
    private final EntityManager em;
    private BusinessConstants.DaoLog bc;

    @Override
    public Book create(@NonNull String title
            , @NonNull Author author
            , @NonNull List<Genre> genreList) throws DataAccessException, PersistenceException {
        Book book = new Book(null, title, author, genreList, new ArrayList<>());
        em.persist(book);
        return book;
    }

    @Override
    public Book getById(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                select b from Book b
                join fetch b.author
                join fetch b.commentList
                where b.id = :id
                """;
        TypedQuery<Book> query = em.createQuery(sql, Book.class);
        query.setParameter("id", id);
        Book book = query.getSingleResult();
        if (book == null) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return book;
    }

    @Override
    public List<Book> getByTitle(@NonNull String title) throws DataAccessException, PersistenceException {
        String sql = """
                select b from Book b
                join fetch b.author
                where b.title = :title
                """;
        TypedQuery<Book> query = em.createQuery(sql, Book.class);
        query.setParameter("title", title);
        List<Book> bookList = query.getResultList();
        if (bookList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return bookList;
    }

    @Override
    public List<Book> getByTitleAndAuthor(String title, Integer authorId) throws DataAccessException, PersistenceException {
        String sql = """
                select b from Book b
                join fetch b.author
                where b.title = :title and b.author.id = :author_id
                """;
        TypedQuery<Book> query = em.createQuery(sql, Book.class);
        query.setParameter("title", title);
        query.setParameter("author_id", authorId);
        List<Book> bookList = query.getResultList();
        if (bookList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return bookList;
    }

    @Override
    public List<Book> getAll() throws DataAccessException, PersistenceException {
        String sql = """
                select b from Book b
                join fetch b.author
                """;
        TypedQuery<Book> query = em.createQuery(sql, Book.class);
        List<Book> bookList = query.getResultList();
        if (bookList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.NOTHING_IN_BD);
        }
        return bookList;
    }

    @Override
    public Book update(@NonNull Book bookById
            , @NonNull String title
            , @NonNull Author author
            , @NonNull List<Genre> genreList) throws DataAccessException, PersistenceException {
        Book book = bookById;
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenreList(genreList);
        String sql = """
                update Book b
                set b.title = :title, b.author.id = :author_id
                where b.id = :id
                """;
        Query query = em.createQuery(sql);
        query.setParameter("id", bookById.getId());
        query.setParameter("title", title);
        query.setParameter("author_id", author.getId());
        int countAffectedRows = query.executeUpdate();
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return updateGenre(book);
    }

    @Override
    public Book updateComment(@NonNull Book bookById, @NonNull List<Comment> commentList) {
        Book book = bookById;
        book.setCommentList(commentList);
        return em.merge(book);
    }

    private Book updateGenre(Book book) throws DataAccessException, PersistenceException {
        return em.merge(book);
    }

    @Override
    public Boolean delete(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                delete from Book b
                where b.id = :id
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
