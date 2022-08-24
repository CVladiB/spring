package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.dao.entity.book.BookDao;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.book-service")
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final CheckService checkService;
    @Setter
    private int minInput;
    @Setter
    private int maxInputTitle;
    private Function<String, List<String>> correctInputStrTitleMinMaxFn;
    private BiFunction<String, Integer, List<String>> existTitleAndAuthorFn;

    @PostConstruct
    private void initFunction() {
        correctInputStrTitleMinMaxFn = str -> checkService.checkCorrectInputStrLength(str, minInput, maxInputTitle);
        existTitleAndAuthorFn = (title, author) -> checkService.checkIfNotExist(() -> readByTitleAndAuthor(title, author));
    }

    @Nullable
    @Override
    public Book create(@NonNull String title, @NonNull Author author, @NonNull List<Genre> genreList) {
        Book book = null;
        if (checkService.doCheck(title, correctInputStrTitleMinMaxFn, t -> existTitleAndAuthorFn.apply(title, author.getId()))) {
            book = bookDao.create(title, author, genreList);
        }
        return book;
    }

    @Nullable
    @Override
    public Book readById(@NonNull Integer id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> readByTitle(@NonNull String title) {
        return getOrEmptyList(bookDao.getByTitle(title));
    }

    @Override
    public List<Book> readByTitleAndAuthor(@NonNull String title, @NonNull Integer authorId) {
        return getOrEmptyList(bookDao.getByTitleAndAuthor(title, authorId));
    }

    @Override
    public List<Book> readAll() {
        return getOrEmptyList(bookDao.getAll());
    }

    @Nullable
    @Override
    public Book update(@NonNull Integer id, String title, @NonNull Author author, @NonNull List<Genre> genreList) {
        Book book = null;
        Book bookById = bookDao.getById(id);
        if (checkService.doCheck(bookById, checkService::checkExist, t -> existTitleAndAuthorFn.apply(title, author.getId()))) {
            book = bookDao.update(bookById
                    , checkService.correctOrDefault(title, correctInputStrTitleMinMaxFn, bookById::getTitle)
                    , author
                    , genreList);
        }
        return book;
    }

    @Override
    public Book updateComment(@NonNull Integer id, @NonNull Comment comment) {
        Book bookById = bookDao.getById(id);
        bookById.getCommentList().add(comment);
        return bookDao.updateComment(bookById, bookById.getCommentList());
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        return checkService.doCheck(bookDao.getById(id), checkService::checkExist)
                && bookDao.delete(id);
    }
}
