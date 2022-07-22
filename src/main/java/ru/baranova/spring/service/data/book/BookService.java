package ru.baranova.spring.service.data.book;

import ru.baranova.spring.domain.Book;

import java.util.List;

public interface BookService {
    Book create(String title, String authorSurname, String authorName, String... genre);

    Book read(Integer id);

    List<Book> getByTitle(String title);


    List<Book> readAll();

    Book update(Integer id, String title, String authorSurname, String authorName, String... genreArg);

    void delete(Integer id);

}
