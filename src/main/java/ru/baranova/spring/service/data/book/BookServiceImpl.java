package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.service.app.AppService;
import ru.baranova.spring.service.app.CheckService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT = 40;
    private final BookDao bookDaoJdbc;
    private final CheckService checkServiceImpl;
    private final AppService appServiceImpl;

    @Nullable
    @Override
    public BookEntity create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        boolean checkTitleAndAuthorId = Optional.ofNullable(appServiceImpl.evaluate(() -> readByTitle(title)))
                .orElseGet(Collections::emptyList)
                .stream()
                .noneMatch(bookAuthorId -> bookAuthorId.getAuthorId().equals(authorId));

        if ((checkServiceImpl.checkIfNotExist(() -> readByTitle(title).isEmpty()) || checkTitleAndAuthorId)
                && (checkServiceImpl.isCorrectInputString(title, MIN_INPUT, MAX_INPUT))) {
            bookEntity = appServiceImpl.evaluate(() -> bookDaoJdbc.create(title, authorId, genreIdList));
        }

        return bookEntity;
    }

    @Nullable
    @Override
    public BookEntity readById(@NonNull Integer id) {
        return appServiceImpl.evaluate(() -> bookDaoJdbc.getById(id));
    }

    @Override
    public List<BookEntity> readByTitle(@NonNull String title) {
        List<BookEntity> bookEntities = appServiceImpl.evaluate(() -> bookDaoJdbc.getByTitle(title));
        return bookEntities == null ? new ArrayList<>() : bookEntities;
    }

    @Override
    public List<BookEntity> readAll() {
        List<BookEntity> bookEntities = appServiceImpl.evaluate(bookDaoJdbc::getAll);
        return bookEntities == null ? new ArrayList<>() : bookEntities;
    }

    @Nullable
    @Override
    public BookEntity update(@NonNull Integer id, String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        boolean checkTitleAndAuthorId = Optional.ofNullable(appServiceImpl.evaluate(() -> readByTitle(title)))
                .orElseGet(Collections::emptyList)
                .stream()
                .noneMatch(bookAuthorId -> bookAuthorId.getAuthorId().equals(authorId));

        if (checkServiceImpl.checkExist(() -> readById(id) != null) &&
                (checkServiceImpl.checkIfNotExist(() -> readByTitle(title).isEmpty()) || checkTitleAndAuthorId)) {
            String finalTitle = checkServiceImpl.isCorrectInputString(title, MIN_INPUT, MAX_INPUT) ?
                    title : appServiceImpl.evaluate(() -> bookDaoJdbc.getById(id).getTitle());

            bookEntity = appServiceImpl.evaluate(() -> bookDaoJdbc.update(id, finalTitle, authorId, genreIdList));
        }

        return bookEntity;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        return checkServiceImpl.checkExist(() -> readById(id) != null)
                && appServiceImpl.evaluate(() -> bookDaoJdbc.delete(id));
    }
}
