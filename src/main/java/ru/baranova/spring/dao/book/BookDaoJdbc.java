package ru.baranova.spring.dao.book;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.BusinessConstants;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private BusinessConstants.DaoLog bc;

    @SneakyThrows
    private static BookEntity bookMapper(ResultSet resultSet, int rowNum) {
        Integer id = resultSet.getInt("book_id");
        String title = resultSet.getString("book_title");
        Integer authorId = resultSet.getInt("author_id");
        return new BookEntity(id, title, authorId, null);
    }

    @SneakyThrows
    private static BookEntity bookGenreMapper(ResultSet resultSet) {
        Integer bookId = null;
        List<Integer> genreList = new ArrayList<>();
        while (resultSet.next()) {
            bookId = resultSet.getInt("book_id");
            Integer genreId = resultSet.getInt("genre_id");
            if (!genreList.contains(genreId)) {
                genreList.add(genreId);
            }
        }
        return new BookEntity(bookId, null, null, genreList);
    }

    @Override
    public BookEntity create(@NonNull String title, @NonNull Integer authorId, @NotNull List<Integer> genreIdList) throws DataAccessException {
        String sql = """
                insert into book (book_title, author_id)
                values (:title, :author_id)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);
        params.addValue("author_id", authorId);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbc.update(sql, params, holder, new String[]{"book_id"});

        Integer bookId = (Integer) holder.getKey();
        if (bookId != null) {
            createBookGenreByBookId(bookId, genreIdList);
        } else {
            throw new DataIntegrityViolationException("");
        }

        return BookEntity.builder()
                .id(bookId)
                .title(title)
                .authorId(authorId)
                .genreListId(genreIdList)
                .build();
    }

    private void createBookGenreByBookId(@NonNull Integer bookId, @NonNull List<Integer> genreList) throws DataAccessException {
        String sqlGenre = """
                insert into book_genre (book_id, genre_id)
                values (:book_id, :genre_id);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("book_id", bookId);

        genreList.forEach(genre -> {
            params.addValue("genre_id", genre);
            if (jdbc.update(sqlGenre, params) == 0) {
                throw new DataIntegrityViolationException("");
            }
        });
    }

    @Override
    public BookEntity getById(@NonNull Integer id) {
        BookEntity bookEntity = getByIdWithoutGenre(id);
        bookEntity.setGenreListId(getBookGenreById(bookEntity.getId()));
        return bookEntity;
    }

    @Override
    public BookEntity getByIdWithoutGenre(@NonNull Integer id) {
        String sql = """
                select book_id, book_title, author_id
                from book
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        BookEntity book = jdbc.queryForObject(sql, params, BookDaoJdbc::bookMapper);
        if (book == null) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return book;
    }

    private List<Integer> getBookGenreById(@NonNull Integer bookId) {
        String sql = """
                select book_id, genre_id
                from book_genre
                where book_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("id", bookId);

        return Objects.requireNonNull(jdbc.query(sql, params, BookDaoJdbc::bookGenreMapper)).getGenreListId();
    }

    @Override
    public List<BookEntity> getByTitle(@NonNull String title) {
        String sql = """
                select book_id, book_title, author_id
                from book
                where book_title = :title
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);

        List<BookEntity> book = jdbc.query(sql, params, BookDaoJdbc::bookMapper);
        if (book.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return book;
    }

    @Override
    public List<BookEntity> getByTitleAndAuthor(String title, Integer authorId) {
        String sql = """
                select book_id, book_title, author_id
                from book
                where book_title = :title and author_id = :author_id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", title);
        params.addValue("author_id", authorId);

        List<BookEntity> bookList = jdbc.queryForStream(sql, Map.of(), BookDaoJdbc::bookMapper)
                .collect(Collectors.toList());
        if (bookList.isEmpty()) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return bookList;
    }

    @Override
    public List<BookEntity> getAll() {
        String sql = """
                select book_id, book_title, author_id
                from book
                """;
        List<BookEntity> bookList;
        if ((bookList = jdbc.queryForStream(sql, Map.of(), BookDaoJdbc::bookMapper)
                .collect(Collectors.toList())).isEmpty()) {
            throw new DataIntegrityViolationException(bc.NOTHING_IN_BD);
        }
        return bookList;
    }

    @Override
    public BookEntity update(@NonNull Integer id
            , @NonNull String title
            , @NonNull Integer authorId
            , @NotNull List<Integer> genreListId) throws DataAccessException {
        String sql = """
                update book set book_title = :title, author_id = :author_id
                where book_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        params.addValue("title", title);
        params.addValue("author_id", authorId);

        if (jdbc.update(sql, params) > 0) {
            updateBookGenreByBookId(id, genreListId);
        } else {
            throw new DataIntegrityViolationException("");
        }

        return BookEntity.builder()
                .id(id)
                .title(title)
                .authorId(authorId)
                .genreListId(genreListId)
                .build();
    }

    private void updateBookGenreByBookId(@NonNull Integer bookId
            , @NonNull List<Integer> genreListId) throws DataAccessException {
        String sqlGenre = """
                delete from book_genre
                where book_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("id", bookId);

        if (jdbc.update(sqlGenre, params) == 0) {
            throw new DataIntegrityViolationException("");
        }

        createBookGenreByBookId(bookId, genreListId);
    }

    @Override
    public Boolean delete(@NonNull Integer id) throws DataAccessException {
        String sql = """
                delete from book
                where book_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int countAffectedRows = jdbc.update(sql, params);
        if (countAffectedRows == 0) {
            throw new DataIntegrityViolationException(bc.SHOULD_EXIST_INPUT);
        }
        return countAffectedRows > 0;
    }
}
