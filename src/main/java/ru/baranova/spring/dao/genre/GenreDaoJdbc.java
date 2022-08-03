package ru.baranova.spring.dao.genre;

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
import ru.baranova.spring.domain.Genre;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @SneakyThrows
    private static Genre genreMapper(ResultSet resultSet, int rowNum) {
        Integer id = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");
        String description = resultSet.getString("genre_description");

        return new Genre(id, name, description);
    }

    @Override
    public Genre create(@NonNull String name, String description) {
        String sql = """
                insert into genre (genre_name, genre_description)
                values (:name, :description)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        params.addValue("description", description);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.update(sql, params, holder, new String[]{"genre_id"});

        return Genre.builder()
                .id((Integer) holder.getKey())
                .name(name)
                .description(description)
                .build();
    }

    @Override
    public Genre getById(@NonNull Integer id) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbc.queryForObject(sql, params, GenreDaoJdbc::genreMapper);
    }

    @Override
    public Genre getByName(@NonNull String name) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_name = :name
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);

        return jdbc.queryForObject(sql, params, GenreDaoJdbc::genreMapper);
    }

    @Override
    public List<Genre> getAll() {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                """;

        return jdbc.query(sql, GenreDaoJdbc::genreMapper);
    }

    @Override
    @Nullable
    public Genre update(@NonNull Integer id, @NotNull String name, @NotNull String description) {
        String sql = """
                update genre set genre_name = :name, genre_description = :description
                where genre_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("name", name);
        params.addValue("description", description);

        if (jdbc.update(sql, params) == 0) {
            throw new DataIntegrityViolationException("");
        }

        return Genre.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    @Override
    public int delete(@NonNull Integer id) {
        String sql = """
                delete from genre
                where genre_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbc.update(sql, params);
    }
}
