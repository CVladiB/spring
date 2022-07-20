package ru.baranova.spring.dao.book;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
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

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void create(@NonNull Book book) {
        Integer id = createBook(book);
        createBookGenre(book.getGenre(), id);
    }

    private Integer createBook(Book book) {
        String sql = """
                insert into book (book_title, author_id)
                values (:title, :author_id)
                returning book_id
                """;
        Map<String, ?> paramMap = Map.of("book_title", book.getTitle(), "author_id", book.getAuthor().getId());
        return jdbc.update(sql, paramMap);
    }

    private void createBookGenre(List<Genre> genreList, Integer bookId) {
        String sqlGenre = """
                insert into book_genre (book_id, genre_id)
                values (:book_id, :genre_id);
                """;
        for (Genre genre : genreList) {
            Map<String, ?> paramMapGenre = Map.of("book_id", bookId, "genre_id", genre.getId());
            jdbc.update(sqlGenre, paramMapGenre);
        }
    }

    @Override
    public Book read(Integer id) {
        String sql = """
                select book_title, author_surname, author_name, genre_name
                from book 
                inner join author using (author_id)
                inner join book_genre using (book_id)
                inner join genre using (genre_id)
                where book_id = :id
                """;
        return jdbc.queryForObject(sql, Map.of("id", id), new BookMapper());
    }

    @Override
    public List<Book> read() {
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
        updateBook(book);
        updateBookGenre(book.getGenre(), book.getId());
    }

    private void updateBook(Book book) {
        String sql = """
                update book set book_title = :title, author_id = :id
                where book_id = :id
                """;
        Map<String, ?> paramMap = Map.of("id", book.getId(), "book_title", book.getTitle(), "author_id", book.getAuthor().getId());
        jdbc.update(sql, paramMap);
    }

    private void updateBookGenre(List<Genre> genreList, Integer bookId) {
        String sqlGenre = """
                update book_genre set genre_id = :genre_id
                where book_id = :book_id
                """;
        for (Genre genre : genreList) {
            Map<String, ?> paramMapGenre = Map.of("book_id", bookId, "genre_id", genre.getId());
            jdbc.update(sqlGenre, paramMapGenre);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = """
                delete from book
                where book_id = :id
                """;
        jdbc.update(sql, Map.of("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {
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
