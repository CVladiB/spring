package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDaoJdbc;
    private final CheckService checkServiceImpl;
    private int minInput;
    private int maxInput;

    private void init() {
        minInput = 3;
        maxInput = 40;
    }

    @Nullable
    @Override
    public Book create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        init();
        Book book = null;

        if (readAll().stream()
                .filter(bookStream -> bookStream.getTitle().equals(title))
                .filter(bookStream -> bookStream.getAuthor().getId().equals(authorId))
                .map(p -> Boolean.FALSE).findAny().orElse(Boolean.TRUE)) {
            if (checkServiceImpl.isCorrectInputString(title, minInput, maxInput)) {
                book = Book.builder()
                        .title(title)
                        .author(Author.builder().id(authorId).build())
                        .genreList(genreIdList.stream().map(id -> Genre.builder().id(id).build()).toList())
                        .build();
                try {
                    Integer id = bookDaoJdbc.create(title, authorId, genreIdList);
                    if (id != null) {
                        book.setId(id);
                    }
                } catch (DataIntegrityViolationException e) {
                    book = null;
                }
            }
        }
        return book;
    }

    @Nullable
    @Override
    public Book readById(@NonNull Integer id) {
        Book book = null;

        Stream<Integer> idStream = readAll().stream().map(Book::getId);
        try {
            if (checkServiceImpl.isInputExist(id, idStream, true)) {
                book = bookDaoJdbc.getById(id);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }
        return book;
    }

    @Override
    public List<Book> readByTitle(@NonNull String title) {
        List<Book> bookList = new ArrayList<>();

        Stream<String> titleStream = readAll().stream().map(Book::getTitle);
        try {
            if (checkServiceImpl.isInputExist(title, titleStream, true)) {
                bookList = bookDaoJdbc.getByTitle(title);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info(e.getMessage());
        }
        return bookList;
    }

    @Override
    public List<Book> readAll() {
        return bookDaoJdbc.getAll();
    }

    @Nullable
    @Override
    public Book update(@NonNull Integer id, String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        Stream<Integer> idStream = readAll().stream().map(Book::getId);
        Book book = null;

        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            init();
            book = bookDaoJdbc.getById(id);

            if (checkServiceImpl.isCorrectInputString(title, minInput, maxInput)) {
                book.setTitle(title);
            }

            book.setAuthor(Author.builder().id(authorId).build());
            book.setGenreList(genreIdList.stream().map(g -> Genre.builder().id(g).build()).toList());

            try {
                if (bookDaoJdbc.update(id, book.getTitle(), authorId, genreIdList) == 0) {
                    book = null;
                }
            } catch (DataIntegrityViolationException e) {
                book = null;
            }
        }
        return book;
    }

    @Override
    public boolean delete(@NonNull Integer id) {
        boolean isComplete = false;
        Stream<Integer> idStream = readAll().stream().map(Book::getId);
        if (checkServiceImpl.isInputExist(id, idStream, true)) {
            bookDaoJdbc.delete(id);
            if (bookDaoJdbc.delete(id) > 0) {
                isComplete = true;
            }
        }
        return isComplete;
    }

}
