package ru.baranova.spring.dao.genre;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(Genre genre) {
        String sql = """
                insert into genre (genre_name, genre_description)
                values (:name, :description)
                """;
        Map<String, ?> paramMap = Map.of("name", genre.getName()
                , "description", genre.getDescription());
        jdbc.update(sql
                , paramMap);
    }

    @Override
    public Genre read(Integer id) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new GenreMapper());
    }

    @Override
    public List<Genre> read() {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                """;
        return jdbc.getJdbcOperations().query(sql, new GenreMapper());
    }

    @Override
    public void update(@NonNull Genre genre) {
        String sql = """
                update genre set genre_name = :name, genre_description = :description
                where genre_id = :id
                """;
        Map<String, ?> paramMap = Map.of("id", genre.getId(), "name", genre.getName(), "description", genre.getDescription());
        jdbc.update(sql, paramMap);
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                delete from genre
                where genre_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Integer id = resultSet.getInt("genre_id");
            String name = resultSet.getString("genre_name");
            String description = resultSet.getString("genre_description");

            return new Genre(id, name, description);
        }
    }
}
