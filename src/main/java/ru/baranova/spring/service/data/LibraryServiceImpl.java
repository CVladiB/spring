package ru.baranova.spring.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final BookService bookServiceImpl;
    private final AuthorService authorServiceImpl;
    private final GenreService genreServiceImpl;

    @Nullable
    @Override
    public Book create(@NonNull String title, @NonNull String authorSurname, @NonNull String authorName, @NonNull List<String> genreNameList) {
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);
        Book book = null;
        if (author != null && !genreList.isEmpty()) {
            book = bookServiceImpl.create(title, author.getId(), genreList.stream().map(Genre::getId).toList());
        }

        if (book == null) {
            log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
            return null;
        } else {
            book.setAuthor(author);
            book.setGenreList(genreList);
        }

        return book;
    }

    @Nullable
    @Override
    public Book create(@NonNull String title, @NonNull Integer authorId, @NonNull List<Integer> genreIdList) {
        Book book = null;
        Author author = authorServiceImpl.readById(authorId);
        List<Genre> genreList = genreIdList.stream()
                .map(genreServiceImpl::readById)
                .filter(Objects::nonNull)
                .toList();
        if (author != null && !genreList.isEmpty()) {
            book = bookServiceImpl.create(title, authorId, genreIdList);
        }

        if (book == null) {
            log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
        } else {
            book = checkAndSetFieldsToBook(book);
        }

        return book;
    }

    @Nullable
    @Override
    public Author checkAndCreateAuthorForBook(String authorSurname, String authorName) {
        Author author = null;
        List<Author> authorList = authorServiceImpl.readBySurnameAndName(authorSurname, authorName);
        if (authorList.isEmpty()) {
            author = authorServiceImpl.create(authorSurname, authorName);
            if (author == null) {
                log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
            }
        } else if (authorList.size() == 1) {
            author = authorList.get(0);
        } else {
            log.info(BusinessConstants.LibraryServiceLog.WARNING_EXIST_MANY);
        }

        return author;
    }

    @Override
    public List<Genre> checkAndCreateGenreForBook(List<String> genreNameList) {
        return genreNameList.stream()
                .map(genreName -> genreServiceImpl.readByName(genreName) == null ?
                        genreServiceImpl.create(genreName, null) : genreServiceImpl.readByName(genreName))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    @Override
    public Book readById(Integer id) {
        Book book = bookServiceImpl.readById(id);
        if (book != null) {
            book = checkAndSetFieldsToBook(book);
        }
        return book;
    }

    @Override
    public List<Book> readByTitle(String title) {
        return bookServiceImpl.readByTitle(title)
                .stream()
                .map(this::checkAndSetFieldsToBook)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<Book> readAll() {
        return bookServiceImpl.readAll()
                .stream()
                .map(this::checkAndSetFieldsToBook)
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    @Override
    public Book checkAndSetFieldsToBook(Book book) {
        if (book.getAuthor() != null) {
            Author author = authorServiceImpl.readById(book.getAuthor().getId());
            if (author == null) {
                return null;
            } else {
                book.setAuthor(author);
            }
        }

        if (!book.getGenreList().isEmpty()) {
            List<Genre> genreList = book.getGenreList().stream()
                    .map(Genre::getId)
                    .map(genreServiceImpl::readById)
                    .toList();
            if (genreList.isEmpty() || genreList.contains(null)) {
                return null;
            } else {
                book.setGenreList(genreList);
            }
        }
        return book;
    }

    @Nullable
    @Override
    public Book update(Integer id, String title, String authorSurname, String authorName, List<String> genreNameList) {
        Book book = bookServiceImpl.readById(id);
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);

        if (book != null && author != null && !genreList.isEmpty()) {
            book = bookServiceImpl.update(id, title, author.getId(), genreList.stream().map(Genre::getId).toList());

            if (book == null) {
                log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
            } else {
                book.setAuthor(author);
                book.setGenreList(genreList);
            }
        } else {
            book = null;
        }
        return book;
    }

    @Nullable
    @Override
    public Book update(@NonNull Integer id, String title, Integer authorId, List<Integer> genreIdList) {
        Book book = bookServiceImpl.readById(id);
        Author author = authorServiceImpl.readById(authorId);
        List<Genre> genreList = genreIdList.stream().map(genreServiceImpl::readById).toList();
        if (book != null && author != null && !genreList.isEmpty()) {
            book = bookServiceImpl.update(id, title, authorId, genreIdList);

            if (book == null) {
                log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
            } else {
                book = checkAndSetFieldsToBook(book);
            }
        } else {
            book = null;
        }
        return book;
    }


    @Override
    public boolean delete(Integer id) {
        return bookServiceImpl.delete(id);
    }
}
