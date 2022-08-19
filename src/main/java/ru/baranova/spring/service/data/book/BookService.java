package ru.baranova.spring.service.data.book;

import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.BaseService;

import java.util.List;

public interface BookService extends BaseService {
    Book create(String title, Author author, List<Genre> genreList);

    Book readById(Integer id);

    List<Book> readByTitle(String title);

    List<Book> readByTitleAndAuthor(String title, Integer authorId);

    List<Book> readAll();

    Book update(Integer id, String title, Author author, List<Genre> genreList);

    boolean delete(Integer id);
}
