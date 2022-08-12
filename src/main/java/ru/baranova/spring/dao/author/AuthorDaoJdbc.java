package ru.baranova.spring.dao.author;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Author;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    @SneakyThrows
    private static Author map(ResultSet resultSet, int rowNum) {
        Integer id = resultSet.getInt("author_id");
        String surname = resultSet.getString("author_surname");
        String name = resultSet.getString("author_name");
        return new Author(id, surname, name);
    }

    @Override
    public Author create(@NonNull String surname, @NonNull String name) {
        String sql = """
                insert into author (author_surname, author_name)
                values (:surname, :name)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("surname", surname);
        params.addValue("name", name);

        KeyHolder holder = new GeneratedKeyHolder();

        if (jdbc.update(sql, params, holder, new String[]{"author_id"}) == 0) {
            throw new DataIntegrityViolationException("");
        }

        return Author.builder()
                .id((Integer) holder.getKey())
                .surname(surname)
                .name(name)
                .build();
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

        return jdbc.queryForObject(sql, params, AuthorDaoJdbc::map);
    }

    @Override
    public List<Author> getBySurnameAndName(String surname, String name) {
        String sql = """
                select author_id, author_surname, author_name
                from author
                where (1 = 1
                        and :surname is not null
                        and :name is not null
                        and author_surname = :surname
                        and author_name = :name)
                    or (1 = 1
                        and ( :surname is null or :name is null)
                        and (author_surname = :surname or author_name = :name))
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("surname", surname, Types.VARCHAR);
        params.addValue("name", name, Types.VARCHAR);

        return jdbc.query(sql, params, AuthorDaoJdbc::map);
    }

    @Override
    public List<Author> getAll() {
        String sql = """
                select author_id, author_surname, author_name
                from author
                """;
        return jdbc.queryForStream(sql, Map.of(), AuthorDaoJdbc::map)
                .collect(Collectors.toList());
    }

    @Override
    @Nullable
    public Author update(@NonNull Integer id, @NonNull String surname, @NonNull String name) {
        String sql = """
                update author set author_surname = :surname, author_name = :name
                where author_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("surname", surname);
        params.addValue("name", name);

        if (jdbc.update(sql, params) == 0) {
            throw new DataIntegrityViolationException("");
        }

        return Author.builder()
                .id(id)
                .surname(surname)
                .name(name)
                .build();
    }

    @Override
    public Boolean delete(@NonNull Integer id) {
        String sql = """
                delete from author
                where author_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int countAffectedRows = jdbc.update(sql, params);
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException("");
        }
        return countAffectedRows > 0;
    }
}
