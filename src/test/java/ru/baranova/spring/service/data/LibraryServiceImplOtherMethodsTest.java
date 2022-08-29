package ru.baranova.spring.service.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryServiceImplOtherMethodsTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LibraryServiceImpl libraryService;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private Author testAuthor;
    private Genre testGenre1;
    private Genre testGenre2;
    private Comment insertComment1;
    private Comment insertComment2;
    private Comment insertComment3;
    private Comment insertComment4;
    private Comment testComment;
    private List<Genre> genreList;
    private List<Author> authorList;
    private List<Comment> commentList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);

        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre1 = new Genre(null, "Name1Test", "DescriptionTest");
        testGenre2 = new Genre(null, "Name2Test", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);

        insertComment1 = new Comment(1, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        insertComment2 = new Comment(2, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        insertComment3 = new Comment(3, "CommentAuthor2", "BlaBlaBla", LocalDate.now());
        insertComment4 = new Comment(4, "CommentAuthor1", "BlaBlaBla", LocalDate.now());
        testComment = new Comment(null, "TestCommentAuthor", "TestBlaBlaBla", LocalDate.now());
        commentList = List.of(insertComment1, insertComment2, insertComment3, insertComment4);
    }

    @Test
    void author__checkAndCreateAuthorForBook__returnNewAuthor() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor2.getName();

        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(Collections.emptyList());
        testAuthor.setId(authorList.size() + 1);
        testAuthor.setName(inputAuthorSurname);
        testAuthor.setName(inputAuthorName);
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName))
                .thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = libraryService.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__checkAndCreateAuthorForBook_ErrorCreate__returnNull() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor2.getName();

        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(Collections.emptyList());
        testAuthor.setId(authorList.size() + 1);
        testAuthor.setName(inputAuthorSurname);
        testAuthor.setName(inputAuthorName);
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName));
    }

    @Test
    void author__checkAndCreateAuthorForBook__returnOneExistAuthor() {
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = insertAuthor1.getName();

        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(List.of(insertAuthor1));

        Author expected = insertAuthor1;
        Author actual = libraryService.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__checkAndCreateAuthorForBook_ManyAuthors__returnNull() {
        testAuthor.setId(3);
        testAuthor.setSurname(insertAuthor1.getSurname());
        String inputAuthorSurname = insertAuthor1.getSurname();
        String inputAuthorName = "-";

        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName))
                .thenReturn(List.of(insertAuthor1, testAuthor));

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.checkAndCreateAuthorForBook(inputAuthorSurname, inputAuthorName));
    }

    @Test
    void genre__checkAndCreateGenreForBook__returnNewGenre() {
        List<String> inputGenreNameList = Stream.of(testGenre1, testGenre2).map(Genre::getName).toList();

        Mockito.when(genreService.readByName(testGenre1.getName())).thenReturn(Collections.emptyList());
        Mockito.when(genreService.readByName(testGenre2.getName())).thenReturn(Collections.emptyList());
        Mockito.when(genreService.create(testGenre1.getName(), null)).thenReturn(testGenre1);
        Mockito.when(genreService.create(testGenre2.getName(), null)).thenReturn(testGenre2);

        List<Genre> expected = List.of(testGenre1, testGenre2);
        List<Genre> actual = libraryService.checkAndCreateGenreForBook(inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__checkAndCreateGenreForBook_ErrorCreate__returnEmptyList() {
        List<String> inputGenreNameList = Stream.of(testGenre1, testGenre2).map(Genre::getName).toList();

        Mockito.when(genreService.readByName(testGenre1.getName())).thenReturn(null);
        Mockito.when(genreService.readByName(testGenre2.getName())).thenReturn(null);
        Mockito.when(genreService.create(testGenre1.getName(), null)).thenReturn(null);
        Mockito.when(genreService.create(testGenre2.getName(), null)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.checkAndCreateGenreForBook(inputGenreNameList));
    }

    @Test
    void genre__checkAndCreateGenreForBook__returnExistGenre() {
        List<String> inputGenreNameList = genreList.stream().map(Genre::getName).toList();
        Mockito.when(genreService.readByName(genreList.get(0).getName())).thenReturn(List.of(genreList.get(0)));
        Mockito.when(genreService.readByName(genreList.get(1).getName())).thenReturn(List.of(genreList.get(1)));

        List<Genre> expected = genreList;
        List<Genre> actual = libraryService.checkAndCreateGenreForBook(inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__checkAndCreateCommentForBook__returnNew() {
        String inputCommentAuthor = testComment.getAuthor();
        String inputCommentText = insertComment1.getText();

        Mockito.when(commentService.readByAuthorOfComment(inputCommentAuthor))
                .thenReturn(Collections.emptyList());
        testComment.setId(commentList.size() + 1);
        testComment.setText(inputCommentText);
        Mockito.when(commentService.create(inputCommentAuthor, inputCommentText))
                .thenReturn(testComment);

        Comment expected = testComment;
        Comment actual = libraryService.checkAndCreateCommentForBook(inputCommentAuthor, inputCommentText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__checkAndCreateCommentForBook_ErrorCreate__returnNull() {
        String inputCommentAuthor = testComment.getAuthor();
        String inputCommentText = insertComment1.getText();

        Mockito.when(commentService.readByAuthorOfComment(inputCommentAuthor))
                .thenReturn(Collections.emptyList());
        testComment.setId(commentList.size() + 1);
        testComment.setText(inputCommentText);
        Mockito.when(commentService.create(inputCommentAuthor, inputCommentText))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.checkAndCreateCommentForBook(inputCommentAuthor, inputCommentText));
    }

    @Test
    void comment__checkAndCreateCommentForBook__returnOneExist() {
        String inputCommentAuthor = insertComment3.getAuthor();
        String inputCommentText = insertComment3.getText();

        Mockito.when(commentService.readByAuthorOfComment(inputCommentAuthor))
                .thenReturn(List.of(insertComment3));

        Comment expected = insertComment3;
        Comment actual = libraryService.checkAndCreateCommentForBook(inputCommentAuthor, inputCommentText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void comment__checkAndCreateCommentForBook__ManyComments__returnNull() {
        String inputCommentAuthor = insertComment1.getAuthor();
        String inputCommentText = insertComment1.getText();

        Mockito.when(commentService.readByAuthorOfComment(inputCommentAuthor))
                .thenReturn(commentList);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.checkAndCreateCommentForBook(inputCommentAuthor, inputCommentText));
    }
}
