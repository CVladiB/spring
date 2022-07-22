package ru.baranova.spring.dao.author;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(@NonNull Author author) {
        String sql = """
                insert into author (author_surname, author_name)
                values (:author_surname, :author_name)
                
                """;
        //returning author_id
        final Map<String, Object> params = Map.of("author_surname", author.getSurname(), "author_name", author.getName());
        return jdbc.update(sql, params);
    }

    @Override
    public Author getById(Integer id) {
        String sql = """
                select author_id, author_surname, author_name
                from author
                where author_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new AuthorMapper());
    }

    @Override
    public List<Author> getBySurnameAndName(String surname, String name) {
        String sql;
        if (surname == null || name == null) {
            sql = """
                    select author_id, author_surname, author_name
                    from author
                    where author_surname = :surname OR author_name = :name
                    """;
        } else {
            sql = """
                    select author_id, author_surname, author_name
                    from author
                    where author_surname = :surname AND author_name = :name
                    """;
        }
        return jdbc.getJdbcOperations().query(sql, new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        //todo think about it
        String sql = """
                select author_id, author_surname, author_name
                from author
                """;
        return jdbc.getJdbcOperations().query(sql, new AuthorMapper());
    }

    @Override
    public void update(@NonNull Author author) {
        String sql = """
                update author set author_surname = :surname, author_name = :name
                where author_id = :id
                """;
        jdbc.update(sql, Map.of("id", author.getId(), "surname", author.getSurname(), "name", author.getName()));
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                delete from author
                where author_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Integer id = resultSet.getInt("author_id");
            String surname = resultSet.getString("author_surname");
            String name = resultSet.getString("author_name");

            return new Author(id, surname, name);
        }
    }
}
