package ru.baranova.spring.dao.book.book_genre;

import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface BookGenreDao {
    void createBookGenreByBookId(List<Genre> genreList, Integer bookId);

    List<Book> getBookByBookId(Integer bookId);

    List<Genre> getGenreByBookId(Integer genre_id);

}
