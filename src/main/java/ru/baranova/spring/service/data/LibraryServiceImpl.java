package ru.baranova.spring.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Nullable
    @Override
    public BookDTO create(@NonNull String title
            , @NonNull String authorSurname
            , @NonNull String authorName
            , @NonNull List<String> genreNameList) {
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);
        return getBookDTOIfAuthorAndGenreNotNull(author
                , genreList
                , Author::getId
                , u -> u.stream().map(Genre::getId).toList()
                , (t, u) -> bookService.create(title, t, u));
    }

    @Nullable
    @Override
    public BookDTO create(@NonNull String title
            , @NonNull Integer authorId
            , @NonNull List<Integer> genreIdList) {
        Author author = authorService.readById(authorId);
        List<Genre> genreList = genreIdList.stream()
                .map(genreService::readById)
                .filter(Objects::nonNull)
                .toList();
        return getBookDTOIfAuthorAndGenreNotNull(author
                , genreList
                , Author::getId
                , u -> u.stream().map(Genre::getId).toList()
                , (t, u) -> bookService.create(title, t, u));
    }

    @Nullable
    @Override
    public BookDTO readById(Integer id) {
        return (BookDTO) getFnOrNull(bookService.readById(id), this::checkAndSetFieldsToBook);
    }

    @Override
    public List<BookDTO> readByTitle(String title) {
        return bookService.readByTitle(title)
                .stream()
                .map(this::checkAndSetFieldsToBook)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public List<BookDTO> readAll() {
        return bookService.readAll()
                .stream()
                .map(this::checkAndSetFieldsToBook)
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    @Override
    public BookDTO update(@NonNull Integer id
            , String title
            , String authorSurname
            , String authorName
            , List<String> genreNameList) {
        BookEntity bookEntity = bookService.readById(id);
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);

        return (BookDTO) getObjectOrNull(bookEntity
                , getBookDTOIfAuthorAndGenreNotNull(author
                        , genreList
                        , Author::getId
                        , u -> u.stream().map(Genre::getId).toList()
                        , (t, u) -> bookService.update(id, title, t, u)));
    }

    @Nullable
    @Override
    public BookDTO update(@NonNull Integer id
            , String title
            , Integer authorId
            , List<Integer> genreIdList) {
        BookEntity bookEntity = bookService.readById(id);
        Author author = authorService.readById(authorId);
        List<Genre> genreList = genreIdList.stream().map(genreService::readById).toList();

        return (BookDTO) getObjectOrNull(bookEntity
                , getBookDTOIfAuthorAndGenreNotNull(author
                        , genreList
                        , Author::getId
                        , u -> u.stream().map(Genre::getId).toList()
                        , (t, u) -> bookService.update(id, title, t, u)));
    }

    @Override
    public boolean delete(Integer id) {
        return bookService.delete(id);
    }


    @Nullable
    public Author checkAndCreateAuthorForBook(String authorSurname, String authorName) {
        Author author = null;
        List<Author> authorList = authorService.readBySurnameAndName(authorSurname, authorName);
        if (authorList.isEmpty()) {
            author = authorService.create(authorSurname, authorName);
        } else if (authorList.size() == 1) {
            author = authorList.get(0);
        } else {
            log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST_MANY);
        }
        return author;
    }

    public List<Genre> checkAndCreateGenreForBook(List<String> genreNameList) {
        return genreNameList.stream()
                .map(genreName -> genreService.readByName(genreName) == null ?
                        genreService.create(genreName, null) : genreService.readByName(genreName))
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    public BookDTO checkAndSetFieldsToBook(@NonNull BookEntity bookEntity) {
        Author author = authorService.readById(bookEntity.getAuthorId());
        List<Genre> genreList = bookEntity.getGenreListId().stream()
                .map(genreService::readById)
                .filter(Objects::nonNull)
                .toList();
        return (BookDTO) getFnOrNull(author, t -> bookEntityToBookDTO(bookEntity, t, genreList));
    }

    @Nullable
    public BookDTO bookEntityToBookDTO(BookEntity bookEntity, Author author, List<Genre> genreList) {
        if (author.getId().equals(bookEntity.getAuthorId())
                && Objects.equals(bookEntity.getGenreListId(), genreList.stream().map(Genre::getId).toList())) {
            return BookDTO.builder()
                    .id(bookEntity.getId())
                    .title(bookEntity.getTitle())
                    .author(author)
                    .genreList(genreList)
                    .build();
        } else {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }

    @Nullable
    public <T, U> BookDTO getBookDTOIfAuthorAndGenreNotNull(Author author
            , List<Genre> genreList
            , Function<Author, T> authorFn
            , Function<List<Genre>, U> genreListFn
            , BiFunction<T, U, BookEntity> fn) {
        if (author != null && !genreList.isEmpty()) {
            return (BookDTO) getFnOrNull(fn.apply(authorFn.apply(author), genreListFn.apply(genreList))
                    , t -> bookEntityToBookDTO(t, author, genreList));
        } else {
            log.info(BusinessConstants.EntityServiceLog.WARNING_CREATE);
            return null;
        }
    }
}
