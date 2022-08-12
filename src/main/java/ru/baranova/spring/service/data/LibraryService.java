package ru.baranova.spring.service.data;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface LibraryService extends BaseService {
    BookDTO create(String title, String authorSurname, String authorName, List<String> genreNameList);

    BookDTO create(String title, Integer authorId, List<Integer> genreIdList);

    Author checkAndCreateAuthorForBook(String authorSurname, String authorName);

    List<Genre> checkAndCreateGenreForBook(List<String> genreNameList);

    BookDTO readById(Integer id);

    List<BookDTO> readByTitle(String title);

    List<BookDTO> readAll();

    BookDTO checkAndSetFieldsToBook(BookEntity bookEntity);

    BookDTO update(Integer id, String title, String authorSurname, String authorName, List<String> genreNameList);

    BookDTO update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    boolean delete(Integer id);
}
