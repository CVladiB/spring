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
    private final BookDao bookDaoJdbc;
    private final CheckService checkServiceImpl;
    private final Function<String, List<String>> correctInputStrTitleMinMaxFn;
    private final BiFunction<String, Integer, List<String>> existTitleAndAuthorIdFn;

    public BookServiceImpl(BookDao bookDaoJdbc, CheckService checkServiceImpl) {
        this.bookDaoJdbc = bookDaoJdbc;
        this.checkServiceImpl = checkServiceImpl;
        correctInputStrTitleMinMaxFn = str -> checkServiceImpl.checkCorrectInputStrLength(str, MIN_INPUT, MAX_INPUT);
        existTitleAndAuthorIdFn = (title, authorId) -> checkServiceImpl.checkIfNotExist(() -> readByTitleAndAuthorId(title, authorId));
    }

    @Nullable
    @Override
    public BookEntity create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        if (checkServiceImpl.doCheck(title, correctInputStrTitleMinMaxFn, t -> existTitleAndAuthorIdFn.apply(title, authorId))) {
            bookEntity = bookDaoJdbc.create(title, authorId, genreIdList);
        }
        return bookEntity;
    }

    @Nullable
    @Override
    public BookEntity readById(@NonNull Integer id) {
        return bookDaoJdbc.getById(id);
    }

    @Override
    public List<BookEntity> readByTitle(@NonNull String title) {
        return getOrEmptyList(bookDaoJdbc.getByTitle(title));
    }

    @Override
    public List<BookEntity> readByTitleAndAuthorId(@NonNull String title, @NonNull Integer authorId) {
        return getOrEmptyList(bookDaoJdbc.getByTitleAndAuthor(title, authorId));
    }

    @Override
    public List<BookEntity> readAll() {
        return getOrEmptyList(bookDaoJdbc.getAll());
    }

    @Nullable
    @Override
    public BookEntity update(@NonNull Integer id, String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        BookEntity bookEntityById = bookDaoJdbc.getById(id);
        if (checkServiceImpl.doCheck(bookEntityById, checkServiceImpl::checkExist, t -> existTitleAndAuthorIdFn.apply(title, authorId))) {
            bookEntity = bookDaoJdbc.update(id
                    , checkServiceImpl.correctOrDefault(title, correctInputStrTitleMinMaxFn, bookEntityById::getTitle)
                    , authorId
                    , genreIdList);
        }
        return bookEntity;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        return checkServiceImpl.doCheck(bookDaoJdbc.getById(id), checkServiceImpl::checkExist)
                && bookDaoJdbc.delete(id);
    }
}
