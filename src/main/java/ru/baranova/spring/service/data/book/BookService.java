package ru.baranova.spring.service.data.book;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.service.data.BaseService;

import java.util.List;

public interface BookService extends BaseService {
    BookEntity create(String title, Integer authorId, List<Integer> genreIdList);

    BookEntity readById(Integer id);

    List<BookEntity> readByTitle(String title);

    List<BookEntity> readByTitleAndAuthorId(@NonNull String title, @NonNull Integer authorId);

    List<BookEntity> readAll();

    BookEntity update(Integer id, String title, Integer authorId, List<Integer> genreIdList);

    boolean delete(Integer id);
}
