package ru.baranova.spring.dao.author;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(@NonNull Author author) {
        String sql = """
                insert into author (author_surname, author_name)
                values (:surname, :name)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("surname", author.getSurname());
        params.addValue("name", author.getName());

        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.update(sql, params, holder, new String[]{"author_id"});

        return (Integer) holder.getKey();
    }

    @Override
    public Author getById(@NonNull Integer id) {
        String sql = """
                select author_id, author_surname, author_name
                from author
                where author_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.queryForObject(sql, params, new AuthorMapper());
    }

    @Override
    public List<Author> getBySurnameAndName(String surname, String name) {
        String sql = """
                select author_id, author_surname, author_name
                from author
                where (:surname is not null and :name is not null and 
                                    (author_surname = :surname and author_name = :name))
                    or (((:surname is not null and :name is null) or (:surname is null and :name is not null)) 
                                    and (author_surname = :surname or author_name = :name))
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("surname", surname);
        params.addValue("name", name);

        return jdbc.query(sql, params, new AuthorMapper());
    }

    @Override
    public List<Author> getAll() {
        String sql = """
                select author_id, author_surname, author_name
                from author
                """;
        return jdbc.queryForStream(sql, Map.of(), new AuthorMapper())
                .collect(Collectors.toList());
    }

    @Override
    public void update(@NonNull Author author) {
        String sql = """
                update author set author_surname = :surname, author_name = :name
                where author_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", author.getId());
        params.addValue("surname", author.getSurname());
        params.addValue("name", author.getName());

        jdbc.update(sql, params);
    }

    @Override
    public void delete(@NonNull Integer id) {
        String sql = """
                delete from author
                where author_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        jdbc.update(sql, params);
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
