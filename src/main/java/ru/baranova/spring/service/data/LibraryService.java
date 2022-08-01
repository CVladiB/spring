package ru.baranova.spring.service.data;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

import java.util.List;

public interface LibraryService {
    Book create(String title, String authorSurname, String authorName, List<String> genreNameList);

    Book create(String title, Integer authorId, List<Integer> genreIdList);

    Author checkAndCreateAuthorForBook(String authorSurname, String authorName);

    List<Genre> checkAndCreateGenreForBook(List<String> genreNameList);

    Book readById(Integer id);

    List<Book> readByTitle(String title);

    List<Book> readAll();

    Book checkAndSetFieldsToBook(Book book);

    Book update(Integer id, String title, String authorSurname, String authorName, List<String> genreNameList);

    Book update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    boolean delete(Integer id);
}
