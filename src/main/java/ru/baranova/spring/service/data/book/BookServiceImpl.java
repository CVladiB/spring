package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.BusinessConstants;
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

    @Nullable
    @Override
    public BookEntity create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        try {
            if (!checkExist(title, authorId) && checkServiceImpl.isCorrectInputString(title, MIN_INPUT, MAX_INPUT)) {
                bookEntity = bookDaoJdbc.create(title, authorId, genreIdList);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return bookEntity;
    }

    @Nullable
    @Override
    public BookEntity readById(@NonNull Integer id) {
        try {
            return bookDaoJdbc.getById(id);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }

    @Override
    public List<BookEntity> readByTitle(@NonNull String title) {
        try {
            return bookDaoJdbc.getByTitle(title);
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return new ArrayList<>();
        }
    }

    @Override
    public List<BookEntity> readAll() {
        try {
            return bookDaoJdbc.getAll();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public BookEntity update(@NonNull Integer id, String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        BookEntity bookEntity = null;
        try {
            if (checkExist(id) && !checkExist(title, authorId)) {
                if (!checkServiceImpl.isCorrectInputString(title, MIN_INPUT, MAX_INPUT)) {
                    title = bookDaoJdbc.getById(id).getTitle();
                }
                bookEntity = bookDaoJdbc.update(id, title, authorId, genreIdList);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return bookEntity;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        try {
            return checkExist(id) && bookDaoJdbc.delete(id) > 0;
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return false;
        }
    }

    public boolean checkExist(Integer id) {
        boolean isExist = false;
        try {
            if (readById(id) == null) {
                log.info(BusinessConstants.CheckServiceLog.SHOULD_EXIST_INPUT);
            } else {
                isExist = true;
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return isExist;
    }

    public boolean checkExist(String title, Integer authorId) {
        boolean isExist = true;
        try {
            if (Optional.ofNullable(readByTitle(title))
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .noneMatch(bookAuthorId -> bookAuthorId.getAuthorId().equals(authorId))) {
                isExist = false;
            } else {
                log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST);
            }
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
        }
        return isExist;
    }

}
