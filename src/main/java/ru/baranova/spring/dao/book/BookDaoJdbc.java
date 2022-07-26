package ru.baranova.spring.dao.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(@NonNull Book book) {
        String sql = """
                insert into book (book_title, author_id)
                values (:title, :author_id)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.update(sql, params, holder, new String[]{"book_id"});
        Integer id = (Integer) holder.getKey();
        createBookGenreByBookId(book.getGenre(), id);
        return id;
    }

    private void createBookGenreByBookId(@NonNull List<Genre> genreList, @NonNull Integer bookId) {
        String sqlGenre = """
                insert into book_genre (book_id, genre_id)
                values (:book_id, :genre_id);
                """;
        genreList.forEach(genre ->
                jdbc.update(sqlGenre, Map.of("book_id", bookId, "genre_id", genre.getId()))
        );
    }

    @Override
    public Book getById(@NonNull Integer id) {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book
                        inner join author using (author_id)
                        inner join book_genre using (book_id)
                        inner join genre using (genre_id)
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.queryForObject(sql, params, new BookMapper());
    }

    @Override
    public List<Book> getByTitle(@NonNull String title) {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book
                        inner join author using (author_id)
                        inner join book_genre using (book_id)
                        inner join genre using (genre_id)
                where book_title = :title
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);

        return jdbc.query(sql, params, new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book
                inner join author using (author_id)
                inner join book_genre using (book_id)
                inner join genre using (genre_id)
                """;

        return jdbc.queryForStream(sql, Map.of(), new BookMapper())
                .collect(Collectors.toList());
    }

    @Override
    public void update(@NonNull Book book) {
        String sql = """
                update book set book_title = :title, author_id = :id
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("author_id", book.getAuthor().getId());

        jdbc.update(sql, params);
        createBookGenreByBookId(book.getGenre(), book.getId());
    }

    @Override
    public void delete(@NonNull Integer id) {
        String sql = """
                delete from book
                where book_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        jdbc.update(sql, params);
    }

    @Getter
    public static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Integer id = resultSet.getInt("book_id");
            String title = resultSet.getString("book_title");

            Integer authorId = resultSet.getInt("author_id");
            String authorSurname = resultSet.getString("author_surname");
            String authorName = resultSet.getString("author_name");
            Author author = new Author(authorId, authorSurname, authorName);

            Integer[] genreArgId = (Integer[]) resultSet.getArray("genre_id").getArray();
            String[] genreArgName = (String[]) resultSet.getArray("genre_name").getArray();
            String[] genreArgDescription = (String[]) resultSet.getArray("genre_description").getArray();
            List<Genre> genre = new ArrayList<>();
            for (int i = 0; i < genreArgId.length; i++) {
                Integer genreId = genreArgId[i];
                String genreName = genreArgName[i];
                String genreDescription = genreArgDescription[i];
                genre.add(new Genre(genreId, genreName, genreDescription));
            }

            return new Book(id, title, author, genre);
        }
    }
}
