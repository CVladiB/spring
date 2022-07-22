package ru.baranova.spring.dao.book.book_genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.baranova.spring.dao.book.BookDaoJdbc;
import ru.baranova.spring.dao.genre.GenreDaoJdbc;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookGenreDaoJdbc implements BookGenreDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void createBookGenreByBookId(List<Genre> genreList, Integer bookId) {
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
    public List<Book> getBookByBookId(Integer bookId) {
        String sql = """
                select book_title, genre_name
                from book_genre bg
                         inner join book b on b.book_id = bg.book_id
                         inner join genre g on g.genre_id = bg.genre_id
                where bg.book_id = :bookId
                """;
        return jdbc.getJdbcOperations().query(sql, new BookDaoJdbc.BookMapper());
    }

    @Override
    public List<Genre> getGenreByBookId(Integer genre_id) {
        String sql = """
                select book_title, genre_name
                from book_genre bg
                         inner join book b on b.book_id = bg.book_id
                         inner join genre g on g.genre_id = bg.genre_id
                where bg.genre_id = :genre_id
                """;
        return jdbc.getJdbcOperations().query(sql, new GenreDaoJdbc.GenreMapper());
    }

}
