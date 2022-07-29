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

import java.util.ArrayList;
import java.util.List;

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
        if (author != null && genreList != null) {
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
        List<Genre> genreList = genreIdList.stream().map(genreServiceImpl::readById).toList();
        if (author != null && !genreList.contains(null)) {
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
    private Author checkAndCreateAuthorForBook(String authorSurname, String authorName) {
        Author author = null;
        List<Author> authorList = authorServiceImpl.readBySurnameAndName(authorSurname, authorName);
        if (authorList == null || authorList.isEmpty()) {
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

    @Nullable
    private List<Genre> checkAndCreateGenreForBook(List<String> genreNameList) {
        List<Genre> genreList = new ArrayList<>();
        for (String genreName : genreNameList) {
            Genre genre = genreServiceImpl.readByName(genreName);
            if (genre == null) {
                genre = genreServiceImpl.create(genreName, null);
                if (genre == null) {
                    log.info(BusinessConstants.LibraryServiceLog.WARNING_CREATE);
                    return null;
                } else {
                    genreList.add(genre);
                }
            } else {
                genreList.add(genre);
            }
        }

        return genreList;
    }

    @Nullable
    @Override
    public Book readById(Integer id) {
        Book book = bookServiceImpl.readById(id);
        if (book == null) {
            return null;
        }
        book = checkAndSetFieldsToBook(book);
        return book;
    }

    @Nullable
    @Override
    public List<Book> readByTitle(String title) {
        List<Book> bookList = bookServiceImpl.readByTitle(title);
        if (bookList != null) {
            for (Book book : bookList) {
                book = checkAndSetFieldsToBook(book);
                if (book == null) {
                    return null;
                }
            }
        }
        return bookList;
    }

    @Nullable
    @Override
    public List<Book> readAll() {
        List<Book> bookList = bookServiceImpl.readAll();
        if (bookList != null) {
            for (Book book : bookList) {
                book = checkAndSetFieldsToBook(book);
                if (book == null) {
                    return null;
                }
            }
        }
        return bookList;
    }

    private Book checkAndSetFieldsToBook(Book book) {
        if (book.getAuthor() != null) {
            Author author = authorServiceImpl.readById(book.getAuthor().getId());
            if (author == null) {
                return null;
            } else {
                book.setAuthor(author);
            }
        }

        if (book.getGenreList() != null) {
            List<Genre> genreList = new ArrayList<>();
            for (Genre genreOnlyId : book.getGenreList()) {
                Genre genre = genreServiceImpl.readById(genreOnlyId.getId());
                if (genre == null) {
                    return null;
                } else {
                    genreList.add(genre);
                }
            }
            if (genreList.isEmpty()) {
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

        if (book != null && author != null && genreList != null && !genreList.contains(null)) {
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
        if (book != null && author != null && !genreList.contains(null)) {
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
