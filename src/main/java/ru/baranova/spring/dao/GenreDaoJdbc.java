package ru.baranova.spring.dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Repository
public class GenreDaoJdbc implements GenreDao{
    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc (NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            Integer id = resultSet.getInt("genre_id");
            String name = resultSet.getString("genre_name");
            String description = resultSet.getString("genre_description");

            return new Genre(id, name, description);
        }
    }

    @Override
    public void create(Genre genre) {
        throw new RuntimeException("not implement");
    }

    @Override
    public Genre read (int id) {
        String sql = "select genre_id, genre_name, genre_description from genre where genre_id = :id";
        return jdbc.queryForObject(sql,Map.of("id", id), new GenreMapper());
    }

    @Override
    public void update(Genre genre){
        throw new RuntimeException("not implement");
    }

    @Override
    public void delete(int id){
        throw new RuntimeException("not implement");
    }
}
