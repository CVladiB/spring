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
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.book.BookService;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.data.genre.GenreService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {LibraryServiceImplTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryServiceImplUpdateTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LibraryServiceImpl libraryService;
    private Genre testGenre1;
    private Genre testGenre2;
    private List<Genre> genreList;
    private Author testAuthor;
    private List<Author> authorList;
    private Comment insertComment1;
    private Comment insertComment2;
    private Comment insertComment3;
    private Comment insertComment4;
    private Comment testComment;
    private List<Comment> commentList;
    private Book insertBook1;
    private Book testBook;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
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
        List<Comment> commentList13 = new ArrayList<>();
        commentList13.add(insertComment1);
        commentList13.add(insertComment3);

        insertBook1 = new Book(1, "Title1", insertAuthor1, List.of(insertGenre1, insertGenre2), commentList13);
        testBook = new Book(null, "TitleTest", testAuthor, List.of(insertGenre1, insertGenre2), List.of(insertComment4));
    }

    @Test
    void book__update__correctReturnNewObject() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        testBook.setCommentList(Collections.emptyList());
        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(Collections.emptyList());
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(Collections.emptyList());
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);
        testBook.setId(inputId);
        Mockito.when(bookService.update(Mockito.eq(inputId), Mockito.eq(inputTitle), Mockito.any(), Mockito.any()))
                .thenReturn(testBook);

        Book expected = testBook;
        Book actual = libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__update_NullAuthor__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = "smth";
        String inputAuthorName = "smth";
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(null);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullGenre__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = List.of("smth");

        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullBookById__returnNull() {
        Integer inputId = 100;
        String inputTitle = testBook.getTitle();
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__update_NullBook__returnNull() {
        Integer inputId = insertBook1.getId();
        String inputTitle = "smth";
        String inputAuthorSurname = testBook.getAuthor().getSurname();
        String inputAuthorName = testBook.getAuthor().getName();
        List<String> inputGenreNameList = testBook.getGenreList().stream().map(Genre::getName).toList();

        testBook.getAuthor().setId(authorList.size() + 1);
        Mockito.when(authorService.readBySurnameAndName(inputAuthorSurname, inputAuthorName)).thenReturn(Collections.emptyList());
        Mockito.when(authorService.create(inputAuthorSurname, inputAuthorName)).thenReturn(testAuthor);

        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Mockito.when(genreService.readByName(inputGenreNameList.get(0))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(0), null)).thenReturn(testGenre1);
        Mockito.when(genreService.readByName(inputGenreNameList.get(1))).thenReturn(null);
        Mockito.when(genreService.create(inputGenreNameList.get(1), null)).thenReturn(testGenre2);

        Mockito.when(bookService.update(inputId, inputTitle, testBook.getAuthor(), testBook.getGenreList()))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorSurname, inputAuthorName, inputGenreNameList));
    }

    @Test
    void book__updateById__correctReturnNewObject() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        testBook.setId(inputId);
        Mockito.when(bookService.update(inputId, inputTitle, testBook.getAuthor(), testBook.getGenreList()))
                .thenReturn(testBook);

        Book expected = testBook;
        Book actual = libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__updateById_NullAuthor__returnNull() {
        testAuthor.setId(100);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullGenre__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(100);
        testGenre2.setId(101);
        Integer inputId = insertBook1.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(null);
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullBookById__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = testBook.getId();
        String inputTitle = testBook.getTitle();
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(null);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateById_NullBook__returnNull() {
        testAuthor.setId(authorList.size() + 1);
        testGenre1.setId(genreList.size() + 1);
        testGenre2.setId(genreList.size() + 2);
        Integer inputId = insertBook1.getId();
        String inputTitle = "smth";
        Integer inputAuthorId = testBook.getAuthor().getId();
        List<Integer> inputGenreIdList = testBook.getGenreList().stream().map(Genre::getId).toList();

        Mockito.when(bookService.readById(inputId)).thenReturn(insertBook1);
        Mockito.when(authorService.readById(inputAuthorId)).thenReturn(testBook.getAuthor());
        Mockito.when(genreService.readById(inputGenreIdList.get(0))).thenReturn(testBook.getGenreList().get(0));
        Mockito.when(genreService.readById(inputGenreIdList.get(1))).thenReturn(testBook.getGenreList().get(1));
        Mockito.when(bookService.update(inputId, inputTitle, testBook.getAuthor(), testBook.getGenreList()))
                .thenReturn(null);

        Assertions.assertThrows(NullPointerException.class
                , () -> libraryService.update(inputId, inputTitle, inputAuthorId, inputGenreIdList));
    }

    @Test
    void book__updateAddCommentToBook__correctReturnNewObject() {
        Integer inputId = insertBook1.getId();
        String inputCommentAuthor = insertComment2.getAuthor();
        String inputCommentText = insertComment2.getText();

        Mockito.when(commentService.readByAuthorOfComment(inputCommentAuthor))
                .thenReturn(Collections.emptyList());
        Mockito.when(commentService.create(inputCommentAuthor, inputCommentText))
                .thenReturn(insertComment2);
        insertBook1.getCommentList().add(insertComment2);
        Mockito.when(bookService.updateComment(Mockito.eq(inputId), Mockito.any()))
                .thenReturn(insertBook1);

        Book expected = insertBook1;
        Book actual = libraryService.updateAddCommentToBook(inputId, inputCommentAuthor, inputCommentText);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__updateAddCommentByIdToBook__correctReturnNewObject() {
        Integer inputIdBook = insertBook1.getId();
        Integer inputIdComment = insertComment2.getId();

        Mockito.when(commentService.readById(inputIdComment))
                .thenReturn(insertComment2);
        insertBook1.getCommentList().add(insertComment2);
        Mockito.when(bookService.updateComment(Mockito.eq(inputIdBook), Mockito.any()))
                .thenReturn(insertBook1);

        Book expected = insertBook1;
        Book actual = libraryService.updateAddCommentByIdToBook(inputIdBook, inputIdComment);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void book__updateUpdateCommentToBook__correctReturnNewObject() {
        Integer inputIdBook = insertBook1.getId();
        Integer inputIdComment = insertComment2.getId();
        String inputCommentText = insertComment2.getText();

        testComment.setId(inputIdComment);
        Mockito.when(commentService.update(inputIdComment, inputCommentText))
                .thenReturn(testComment);
        insertBook1.getCommentList().add(testComment);
        Mockito.when(bookService.updateComment(Mockito.eq(inputIdBook), Mockito.any()))
                .thenReturn(insertBook1);

        Book expected = insertBook1;
        Book actual = libraryService.updateUpdateCommentToBook(inputIdBook, inputIdComment, inputCommentText);
        Assertions.assertEquals(expected, actual);
    }

}
