package ru.baranova.spring.dao.book;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.dao.book.book_genre.BookGenreDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;
    private final BookGenreDao bookGenreDaoJdbc;

    @Override
    public Integer create(@NonNull Book book) {
        String sql = """
                insert into book (book_title, author_id)
                values (:book_title, :author_id)
                
                """;
//        returning book_id
        Map<String, ?> paramMap = Map.of("book_title", book.getTitle(), "author_id", book.getAuthor().getId());
        Integer id = jdbc.update(sql, paramMap);
        bookGenreDaoJdbc.createBookGenreByBookId(book.getGenre(), id);
        return id;
    }

    @Override
    public Book getById(@NonNull Integer id) {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book b
                         inner join author a on a.author_id = b.author_id
                         inner join book_genre bg on bg.book_id = b.book_id
                         inner join genre g on g.genre_id = bg.genre_id
                where b.book_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new BookMapper());
    }

    @Override
    public List<Book> getByTitle(@NonNull String title) {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book b
                         inner join author a on a.author_id = b.author_id
                         inner join book_genre bg on bg.book_id = b.book_id
                         inner join genre g on g.genre_id = bg.genre_id
                where b.book_title = :title
                """;
        return jdbc.getJdbcOperations().query(sql, new BookMapper());
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
        return jdbc.getJdbcOperations().query(sql, new BookMapper());
    }

    @Override
    public void update(@NonNull Book book) {
        String sql = """
                update book set book_title = :title, author_id = :id
                where book_id = :id
                """;
        Map<String, ?> paramMap = Map.of("id", book.getId(), "book_title", book.getTitle(), "author_id", book.getAuthor().getId());
        jdbc.update(sql, paramMap);
        bookGenreDaoJdbc.createBookGenreByBookId(book.getGenre(), book.getId());
    }

    @Override
    public void delete(@NonNull Integer id) {
        String sql = """
                delete from book
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
