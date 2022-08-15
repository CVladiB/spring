package ru.baranova.spring.service.data;

import ru.baranova.spring.domain.BookDTO;

import java.util.List;

public interface LibraryService extends BaseService {
    BookDTO create(String title, String authorSurname, String authorName, List<String> genreNameList);

    BookDTO create(String title, Integer authorId, List<Integer> genreIdList);

    BookDTO readById(Integer id);

    List<BookDTO> readByTitle(String title);

    List<BookDTO> readAll();

    BookDTO update(Integer id, String title, String authorSurname, String authorName, List<String> genreNameList);

    BookDTO update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    boolean delete(Integer id);
}
