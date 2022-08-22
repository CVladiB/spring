package ru.baranova.spring.dao.entity.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDaoJpa implements GenreDao {
    @PersistenceContext
    private final EntityManager em;
    private BusinessConstants.DaoLog bc;

    @Override
    public Genre create(@NonNull String name, String description) throws DataAccessException, PersistenceException {
        Genre genre = new Genre(null, name, description);
        em.persist(genre);
        return genre;
    }

    @Override
    public Genre getById(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                select g from Genre g
                where g.id = :id
                """;
        TypedQuery<Genre> query = em.createQuery(sql, Genre.class);
        query.setParameter("id", id);
        Genre genre = query.getSingleResult();
        if (genre == null) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return genre;
    }

    @Override
    public Genre getByName(@NonNull String name) throws DataAccessException, PersistenceException {
        String sql = """
                select g from Genre g
                where g.name = :name
                """;
        TypedQuery<Genre> query = em.createQuery(sql, Genre.class);
        query.setParameter("name", name);
        Genre genre = query.getSingleResult();
        if (genre == null) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return genre;
    }

    @Override
    public List<Genre> getAll() throws DataAccessException, PersistenceException {
        String sql = """
                select g from Genre g
                """;
        TypedQuery<Genre> query = em.createQuery(sql, Genre.class);
        List<Genre> genreList = query.getResultList();
        if (genreList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.NOTHING_IN_BD);
        }
        return genreList;
    }

    @Override
    public Genre update(@NonNull Integer id
            , @NotNull String name
            , @NotNull String description) throws DataAccessException, PersistenceException {
        Genre genre = new Genre(id, name, description);
        String sql = """
                update Genre g
                set g.name = :name, g.description = :description
                where g.id = :id
                """;
        Query query = em.createQuery(sql);
        query.setParameter("id", id);
        query.setParameter("name", name);
        query.setParameter("description", description);
        int countAffectedRows = query.executeUpdate();
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }

        return genre;
    }

    @Override
    public Boolean delete(@NonNull Integer id) throws DataAccessException, PersistenceException {
        String sql = """
                delete from Genre g
                where g.id = :id
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
