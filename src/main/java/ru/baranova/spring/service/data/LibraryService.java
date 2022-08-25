package ru.baranova.spring.service.data;

import ru.baranova.spring.model.Book;

import java.util.List;

public interface LibraryService {
    Book create(String title, String authorSurname, String authorName, List<String> genreNameList);

    Book create(String title, Integer authorId, List<Integer> genreIdList);

    Book readById(Integer id);

    List<Book> readByTitle(String title);

    List<Book> readAll();

    Book update(Integer id, String title, String authorSurname, String authorName, List<String> genreNameList);

    Book update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    Book updateAddCommentToBook(Integer bookId, String commentAuthor, String commentText);

    Book updateAddCommentByIdToBook(Integer bookId, Integer commentId);

    Book updateUpdateCommentToBook(Integer bookId, Integer commentId, String commentText);

    boolean delete(Integer id);
}
