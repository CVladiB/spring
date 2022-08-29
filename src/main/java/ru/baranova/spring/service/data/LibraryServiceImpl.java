package ru.baranova.spring.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @Nullable
    @Override
    public Book create(@NonNull String title
            , @NonNull String authorSurname
            , @NonNull String authorName
            , @NonNull List<String> genreNameList) {
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);
        return bookService.create(title, author, genreList);
    }

    @Nullable
    @Override
    public Book create(@NonNull String title
            , @NonNull Integer authorId
            , @NonNull List<Integer> genreIdList) {
        Author author = authorService.readById(authorId);
        List<Genre> genreList = genreIdList.stream()
                .map(genreService::readById)
                .filter(Objects::nonNull)
                .toList();
        return bookService.create(title, author, genreList);
    }

    @Nullable
    @Override
    public Book readById(@NonNull Integer id) {
        return bookService.readById(id);
    }

    @Override
    public List<Book> readByTitle(@NonNull String title) {
        return bookService.readByTitle(title);
    }

    @Override
    public List<Book> readAll() {
        return bookService.readAll();
    }

    @Nullable
    @Override
    public Book update(@NonNull Integer id
            , String title
            , String authorSurname
            , String authorName
            , List<String> genreNameList) {
        Author author = checkAndCreateAuthorForBook(authorSurname, authorName);
        List<Genre> genreList = checkAndCreateGenreForBook(genreNameList);
        return bookService.update(id, title, author, genreList);
    }

    @Nullable
    @Override
    public Book update(@NonNull Integer id
            , String title
            , Integer authorId
            , List<Integer> genreIdList) {
        Author author = authorService.readById(authorId);
        List<Genre> genreList = genreIdList.stream().map(genreService::readById).toList();
        return bookService.update(id, title, author, genreList);
    }

    @Nullable
    @Override
    public Book updateAddCommentToBook(@NonNull Integer bookId, @NonNull String commentAuthor, @NonNull String commentText) {
        Comment comment = checkAndCreateCommentForBook(commentAuthor, commentText);
        return bookService.updateComment(bookId, comment);
    }

    @Nullable
    @Override
    public Book updateAddCommentByIdToBook(@NonNull Integer bookId, @NonNull Integer commentId) {
        Comment comment = commentService.readById(commentId);
        return bookService.updateComment(bookId, comment);
    }

    @Nullable
    @Override
    public Book updateUpdateCommentToBook(@NonNull Integer bookId, @NonNull Integer commentId, @NonNull String commentText) {
        Comment comment = commentService.update(commentId, commentText);
        return bookService.updateComment(bookId, comment);
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

    @Nullable
    public Comment checkAndCreateCommentForBook(@NonNull String commentAuthor, @NonNull String commentText) {
        Comment comment = null;
        List<Comment> commentList = commentService.readByAuthorOfComment(commentAuthor);
        if (commentList.isEmpty()) {
            comment = commentService.create(commentAuthor, commentText);
        } else if (commentList.size() == 1 && commentList.get(0).getText().equals(commentText)) {
            comment = commentList.get(0);
        } else {
            log.info(BusinessConstants.EntityServiceLog.WARNING_EXIST_MANY);
        }
        return comment;
    }

    public List<Genre> checkAndCreateGenreForBook(List<String> genreNameList) {
        return genreNameList.stream()
                .map(genreName -> genreService.readByName(genreName).isEmpty() ?
                        genreService.create(genreName, null) : genreService.readByName(genreName).get(0))
                .filter(Objects::nonNull)
                .toList();
    }
}
