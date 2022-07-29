package ru.baranova.spring.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(@NonNull Genre genre) {
        String sql = """
                insert into genre (genre_name, genre_description)
                values (:name, :description)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", genre.getName());
        params.addValue("description", genre.getDescription());

        KeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(sql, params, holder, new String[]{"genre_id"});
        return (Integer) holder.getKey();
    }

    @Override
    public Genre getById(@NonNull Integer id) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.queryForObject(sql, params, new GenreMapper());
    }

    @Override
    public Genre getByName(@NonNull String name) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_name = :name
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        return jdbc.queryForObject(sql, params, new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                """;

        return jdbc.query(sql, new GenreMapper());
    }

    @Override
    public int update(@NonNull Genre genre) {
        String sql = """
                update genre set genre_name = :name, genre_description = :description
                where genre_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", genre.getId());
        params.addValue("name", genre.getName());
        params.addValue("description", genre.getDescription());

        return jdbc.update(sql, params);
    }

    @Override
    public int delete(@NonNull Integer id) {
        String sql = """
                delete from genre
                where genre_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.update(sql, params);
    }

    public static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Integer id = resultSet.getInt("genre_id");
            String name = resultSet.getString("genre_name");
            String description = resultSet.getString("genre_description");

            return new Genre(id, name, description);
        }
    }
}
