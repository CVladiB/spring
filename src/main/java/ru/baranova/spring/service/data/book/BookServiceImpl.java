package ru.baranova.spring.service.data.book;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final static int MIN_INPUT = 3;
    private final static int MAX_INPUT = 40;
    private final BookDao bookDao;
    private final CheckService checkService;
    private final Function<String, List<String>> correctInputStrTitleMinMaxFn;
    private final BiFunction<String, Integer, List<String>> existTitleAndAuthorIdFn;

    public BookServiceImpl(BookDao bookDao, CheckService checkService) {
        this.bookDao = bookDao;
        this.checkService = checkService;
        correctInputStrTitleMinMaxFn = str -> checkService.checkCorrectInputStrLength(str, MIN_INPUT, MAX_INPUT);
        existTitleAndAuthorIdFn = (title, authorId) -> checkService.checkIfNotExist(() -> readByTitleAndAuthorId(title, authorId));
    }

    @Nullable
    @Override
    public BookEntity create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        if (checkService.doCheck(title, correctInputStrTitleMinMaxFn, t -> existTitleAndAuthorIdFn.apply(title, authorId))) {
            bookEntity = bookDao.create(title, authorId, genreIdList);
        }
        return bookEntity;
    }

    @Nullable
    @Override
    public BookEntity readById(@NonNull Integer id) {
        return bookDao.getById(id);
    }

    @Override
    public List<BookEntity> readByTitle(@NonNull String title) {
        return getOrEmptyList(bookDao.getByTitle(title));
    }

    @Override
    public List<BookEntity> readByTitleAndAuthorId(@NonNull String title, @NonNull Integer authorId) {
        return getOrEmptyList(bookDao.getByTitleAndAuthor(title, authorId));
    }

    @Override
    public List<BookEntity> readAll() {
        return getOrEmptyList(bookDao.getAll());
    }

    @Nullable
    @Override
    public BookEntity update(@NonNull Integer id, String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        BookEntity bookEntityById = bookDao.getById(id);
        if (checkService.doCheck(bookEntityById, checkService::checkExist, t -> existTitleAndAuthorIdFn.apply(title, authorId))) {
            bookEntity = bookDao.update(id
                    , checkService.correctOrDefault(title, correctInputStrTitleMinMaxFn, bookEntityById::getTitle)
                    , authorId
                    , genreIdList);
        }
        return bookEntity;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        return checkService.doCheck(bookDao.getById(id), checkService::checkExist)
                && bookDao.delete(id);
    }
}
