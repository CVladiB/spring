package ru.baranova.spring.repository.book;

import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;

import java.util.List;

public interface BookDao {
    Book create(String title, Author author, List<Genre> genreList);

    Book getById(Integer id);

    List<Book> getByTitle(String title);

    List<Book> getByTitleAndAuthor(String title, Integer authorId);

    List<Book> getAll();

    Book update(Integer id, String title, Author author, List<Genre> genreList);

    Boolean delete(Integer id);
}
