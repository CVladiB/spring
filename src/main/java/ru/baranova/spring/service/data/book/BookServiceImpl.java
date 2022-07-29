package ru.baranova.spring.service.data.book;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.book.BookDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.ParseService;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDaoJdbc;
    private final CheckService checkServiceImpl;
    private final ParseService parseServiceImpl;
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

        if (checkServiceImpl.isCorrectInputString(title, minInput, maxInput)) {
            book = Book.builder()
                    .title(title)
                    .author(Author.builder().id(authorId).build())
                    .build();
            Integer id = bookDaoJdbc.create(title, authorId, genreIdList);
            if (id != null) {
                book.setId(id);
            } else {
                book = null;
            }
        }
        return book;
    }

    @Nullable
    @Override
    public Book readById(@NonNull Integer id) {
        Book book = null;

        if (checkServiceImpl.isCorrectInputInt(id)) {
            Stream<Integer> idStream = readAll().stream().map(Book::getId);
            if (checkServiceImpl.isInputExist(id, idStream, true)) {
                book = bookDaoJdbc.getById(id);
            }
        }

        return book;
    }

    @Nullable
    @Override
    public List<Book> readByTitle(@NonNull String title) {
        List<Book> bookList = null;

        Stream<String> titleStream = readAll().stream().map(Book::getTitle);
        if (checkServiceImpl.isInputExist(title, titleStream, true)) {
            bookList = bookDaoJdbc.getByTitle(title);
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
        init();
        title = parseServiceImpl.parseDashToNull(title);
        Book book = null;
        Stream<Integer> idStream = readAll().stream().map(Book::getId);

        if (checkServiceImpl.isInputExist(id, idStream, true) &&
                checkServiceImpl.isCorrectInputString(title, minInput, maxInput)) {
            book = bookDaoJdbc.getById(id);
            book.setTitle(title);
            book.setAuthor(Author.builder().id(authorId).build());
            book.setGenreList(genreIdList.stream().map(g -> Genre.builder().id(g).build()).toList());

            if (bookDaoJdbc.update(id, title, authorId, genreIdList) == 0) {
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
