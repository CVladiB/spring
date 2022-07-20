package ru.baranova.spring.dao.author;

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
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer id = resultSet.getInt("author_id");
            String surname = resultSet.getString("author_surname");
            String name = resultSet.getString("author_name");

            return new Author(id, surname, name);
        }
    }

    @Override
    public void create(@NonNull Author author) {
        String sql = """
                insert into author (author_name, author_surname)
                values (:name, :surname)
                """;
        jdbc.update(sql, Map.of("name", author.getName(), "surname", author.getSurname()));
    }

    @Override
    public Author read(int id) {
        String sql = """
                select author_id, author_surname, author_name
                from author 
                where author_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new AuthorMapper());
    }

    @Override
    public List<Author> read() {
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
        jdbc.update(sql, Map.of("id", author.getId(), "name", author.getName(), "surname", author.getSurname()));
    }

    @Override
    public void delete(int id) {
        String sql = """
                delete from author
                where author_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
    }
}
