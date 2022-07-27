package ru.baranova.spring.dao.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
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

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Integer create(@NonNull String title, @NonNull Integer authorId, @NotNull List<Integer> genreId) {
        String sql = """
                insert into book (book_title, author_id)
                values (:title, :author_id)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);
        params.addValue("author_id", authorId);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.update(sql, params, holder, new String[]{"book_id"});
        Integer id = (Integer) holder.getKey();
        createBookGenreByBookId(id, genreId);
        return id;
    }

    private void createBookGenreByBookId(@NonNull Integer bookId, @NonNull List<Integer> genreList) {
        String sqlGenre = """
                insert into book_genre (book_id, genre_id)
                values (:book_id, :genre_id);
                """;
        genreList.forEach(genre ->
                jdbc.update(sqlGenre, Map.of("book_id", bookId, "genre_id", genre))
        );
    }

    @Override
    public Book getById(@NonNull Integer id) {
        String sql = """
                select book_id, book_title, author_id
                from book
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Book book = jdbc.queryForObject(sql, params, new BookMapper());
        Book bookGenre = getBookGenreById(id);
        if (book.getId() == bookGenre.getId()) {
            book.setGenre(bookGenre.getGenre());
        }

        return book;
    }

    @Override
    public List<Book> getByTitle(@NonNull String title) {
        String sql = """
                select book_id, book_title, author_id
                from book
                where book_title = :title
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);

        List<Book> bookList = jdbc.query(sql, params, new BookMapper());
        bookList.forEach(b -> {
            Book bookGenre = getBookGenreById(b.getId());
            if (b.getId() == bookGenre.getId()) {
                b.setGenre(bookGenre.getGenre());
            }
        });

        return bookList;
    }

    @Override
    public List<Book> getAll() {
        String sql = """
                select book_id, book_title, author_id
                from book
                """;

        List<Book> bookList = jdbc.queryForStream(sql, Map.of(), new BookMapper())
                .collect(Collectors.toList());
        bookList.forEach(b -> {
            Book bookGenre = getBookGenreById(b.getId());
            if (b.getId() == bookGenre.getId()) {
                b.setGenre(bookGenre.getGenre());
            }
        });

        return bookList;
    }

    private Book getBookGenreById(@NonNull Integer id) {
        String sql = """
                select book_id, genre_id
                from book_genre
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return jdbc.query(sql, params, new BookGenreMapper());
    }

    @Override
    public void update(@NonNull Integer id, @NonNull String title, @NonNull Integer authorId, List<Integer> genreIdList) {
        String sql = """
                update book set book_title = :title, author_id = :author_id
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("title", title);
        params.addValue("author_id", authorId);

        jdbc.update(sql, params);
        if (genreIdList != null) {
            updateBookGenreByBookId(id, genreIdList);
        }
    }

    private void updateBookGenreByBookId(@NonNull Integer bookId, @NonNull List<Integer> genreList) {
        String sqlGenre = """
                update book_genre set genre_id = :genre_id
                where book_id = :book_id
                """;
        genreList.forEach(genre ->
                jdbc.update(sqlGenre, Map.of("book_id", bookId, "genre_id", genre))
        );
    }

    @Override
    public void delete(@NonNull Integer id) {
        String sql = """
                delete from book
                where book_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
        deleteBookGenreById(id);
    }

    private void deleteBookGenreById(@NonNull Integer id) {
        String sql = """
                delete from book_genre
                where book_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
    }

    @Getter
    public static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Integer id = resultSet.getInt("book_id");
            String title = resultSet.getString("book_title");
            Integer authorId = resultSet.getInt("author_id");

            return Book.builder()
                    .id(id)
                    .title(title)
                    .author(Author.builder().id(authorId).build())
                    .build();
        }
    }

    @Getter
    public static class BookGenreMapper implements ResultSetExtractor<Book> {
        @Override
        public Book extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Integer bookId = null;
            List<Genre> genreList = new ArrayList<>();

            while (resultSet.next()) {
                bookId = resultSet.getInt("book_id");
                Integer genreId = resultSet.getInt("genre_id");
                Genre genre = Genre.builder().id(genreId).build();
                if (!genreList.contains(genre)) {
                    genreList.add(genre);
                }
            }

            return Book.builder()
                    .id(bookId)
                    .genre(genreList)
                    .build();
        }
    }
}
