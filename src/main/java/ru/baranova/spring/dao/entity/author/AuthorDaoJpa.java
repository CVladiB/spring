package ru.baranova.spring.dao.entity.author;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorDaoJpa implements AuthorDao {
    @PersistenceContext
    private final EntityManager em;
    private BusinessConstants.DaoLog bc;

    @Override
    public Author create(@NonNull String surname, @NonNull String name) throws DataAccessException, PersistenceException {
        Author author = new Author(null, surname, name);
        em.persist(author);
        return author;
    }

    @Override
    public Author getById(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                select a from Author a
                where a.id = :id
                """;
        TypedQuery<Author> query = em.createQuery(sql, Author.class);
        query.setParameter("id", id);
        Author author = query.getSingleResult();
        return author;
    }

    @Override
    public List<Author> getBySurnameAndName(String surname, String name) throws DataAccessException, PersistenceException {
        String sql = """
                select a from Author a
                where (1 = 1
                        and :surname is not null
                        and :name is not null
                        and a.surname = :surname
                        and a.name = :name)
                    or (1 = 1
                        and ( :surname is null or :name is null)
                        and (a.surname = :surname or a.name = :name))
                """;
        TypedQuery<Author> query = em.createQuery(sql, Author.class);
        query.setParameter("surname", surname);
        query.setParameter("name", name);
        List<Author> authorList = query.getResultList();
        if (authorList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return authorList;
    }

    @Override
    public List<Author> getAll() throws DataAccessException, PersistenceException {
        String sql = """
                select a from Author a
                """;
        TypedQuery<Author> query = em.createQuery(sql, Author.class);
        List<Author> authorList = query.getResultList();
        if (authorList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.NOTHING_IN_BD);
        }
        return authorList;
    }

    @Override
    public Author update(@NonNull Integer id, @NonNull String surname, @NonNull String name) throws DataAccessException, PersistenceException {
        Author author = new Author(id, surname, name);
        String sql = """
                update Author a
                set a.surname = :surname, a.name = :name
                where a.id = :id
                """;
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        query.setParameter("surname", surname);
        query.setParameter("name", name);
        int countAffectedRows = query.executeUpdate();
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return author;
    }

    @Override
    public Boolean delete(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                delete from Author a
                where a.id = :id
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
