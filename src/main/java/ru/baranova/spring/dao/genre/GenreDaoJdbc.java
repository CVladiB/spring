package ru.baranova.spring.dao.genre;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(Genre genre) {
        String sql = """
                insert into genre (genre_name, genre_description)
                values (:name, :description)
                
                """;
//        returning genre_id
        Map<String, ?> paramMap = Map.of("name", genre.getName()
                , "description", genre.getDescription());
        return jdbc.update(sql, paramMap);
    }

    @Override
    public Genre getById(@NonNull Integer id) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new GenreMapper());
    }

    @Override
    public Genre getByName(@NonNull String name) {
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                where genre_name = :name
                """;
        return jdbc.queryForObject(sql, Map.of("name", name), new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        //todo
        String sql = """
                select genre_id, genre_name, genre_description
                from genre
                """;
        return jdbc.getJdbcOperations().query(sql, new GenreMapper());
    }

    @Override
    public void update(Genre genre) {
        String sql = """
                update genre set genre_name = :name, genre_description = :description
                where genre_id = :id
                """;
        Map<String, ?> paramMap = Map.of("id", genre.getId(), "name", genre.getName(), "description", genre.getDescription());
        jdbc.update(sql, paramMap);
    }

    @Override
    public void delete(@NonNull Integer id) {
        String sql = """
                delete from genre
                where genre_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
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
