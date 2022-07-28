package ru.baranova.spring.service.data.book;

import ru.baranova.spring.domain.Book;

import java.util.List;

public interface BookService {
    Book create(String title, Integer authorId, List<Integer> genreId);

    Book readById(Integer id);

    List<Book> readByTitle(String title);


    List<Book> readAll();

    Book update(Integer id, String title, Integer authorId, List<Integer> genreId);

    boolean delete(Integer id);

}
