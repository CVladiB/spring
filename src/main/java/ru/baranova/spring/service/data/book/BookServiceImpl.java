package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.BookRepository;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.book-service")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
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

    @Transactional
    @Nullable
    @Override
    public Book create(@NonNull String title, @NonNull Author author, @NonNull List<Genre> genreList) {
        Book book = null;
        if (checkService.doCheck(title, correctInputStrTitleMinMaxFn, t -> existTitleAndAuthorFn.apply(title, author.getId()))) {
            book = bookRepository.save(Book.builder()
                    .title(title)
                    .author(author)
                    .genreList(genreList)
                    .commentList(new ArrayList<>())
                    .build());
        }
        return book;
    }

    @Transactional
    @Nullable
    @Override
    public Book readById(@NonNull Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public List<Book> readByTitle(@NonNull String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional
    @Override
    public List<Book> readByTitleAndAuthor(@NonNull String title, @NonNull Integer authorId) {
        return bookRepository.findByTitleAndAuthor(title, authorId);
    }

    @Transactional
    @Override
    public List<Book> readAll() {
        return bookRepository.findAll();
    }

    @Transactional
    @Nullable
    @Override
    public Book update(@NonNull Integer id, String title, @NonNull Author author, @NonNull List<Genre> genreList) {
        Book book = null;
        Optional<Book> bookById = bookRepository.findById(id);
        if (bookById.isPresent() && checkService.doCheck(bookById.get(), checkService::checkExist, t -> existTitleAndAuthorFn.apply(title, author.getId()))) {
            bookById.get().setTitle(checkService.correctOrDefault(title, correctInputStrTitleMinMaxFn, bookById.get()::getTitle));
            bookById.get().setAuthor(author);
            bookById.get().setGenreList(genreList);
            book = bookRepository.save(bookById.get());
        }
        return book;
    }

    @Transactional
    @Override
    public Book updateComment(@NonNull Integer id, @NonNull Comment comment) {
        Book book = null;
        Optional<Book> bookById = bookRepository.findById(id);
        if (bookById.isPresent()) {
            bookById.get().getCommentList().add(comment);
            book = bookRepository.save(bookById.get());
        }
        return book;
    }

    @Transactional
    @Override
    public boolean delete(@NonNull Integer id) {
        Optional<Book> bookById = bookRepository.findById(id);
        boolean isDelete = false;
        if (bookById.isPresent()) {
            bookRepository.delete(bookById.get());
            isDelete = true;
        }
        return isDelete;
    }
}
