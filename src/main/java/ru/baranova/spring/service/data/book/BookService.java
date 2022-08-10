package ru.baranova.spring.service.data.book;

import ru.baranova.spring.domain.BookEntity;

import java.util.List;

public interface BookService {
    BookEntity create(String title, Integer authorId, List<Integer> genreIdList);

    BookEntity readById(Integer id);

    List<BookEntity> readByTitle(String title);


    List<BookEntity> readAll();

    BookEntity update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    boolean delete(Integer id);
}
