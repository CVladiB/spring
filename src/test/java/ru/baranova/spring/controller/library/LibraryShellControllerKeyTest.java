package ru.baranova.spring.controller.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = {LibraryShellControllerTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryShellControllerKeyTest {
    @Autowired
    private Shell shell;
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private ParseService parseService;
    @Autowired
    private LibraryShellControllerTestConfig config;
    private Genre genre1;
    private Genre genre2;
    private Book book;

    @BeforeEach
    void setUp() {
        Author author = new Author(7, "surname", "name");
        genre1 = new Genre(7, "name1", "description");
        genre2 = new Genre(8, "name2", "description");
        Comment comment = new Comment(7, "CommentAuthor", "BlaBlaBla", LocalDate.now());
        book = new Book(7, "title", author, List.of(genre1, genre2), List.of(comment));
    }

    @Test
    void create_correctKey() {
        String inputGenreNames = "name1,name2";
        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getCreate()
                + " " + book.getTitle()
                + " " + book.getAuthor().getSurname()
                + " " + book.getAuthor().getName()
                + " " + inputGenreNames).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getTitle()
                + " " + book.getAuthor().getSurname()
                + " " + book.getAuthor().getName()
                + " " + book.getGenreList().toString());
        Mockito.verify(libraryService, Mockito.never()).create(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void createById_correctKey() {
        String inputGenreIds = "7,8";

        Mockito.when(parseService.parseLinesToListStrByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId().toString(), genre2.getId().toString()));
        Mockito.when(parseService.parseStringToInt(genre1.getId().toString()))
                .thenReturn(genre1.getId());
        Mockito.when(parseService.parseStringToInt(genre2.getId().toString()))
                .thenReturn(genre2.getId());
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getCreateById()
                + " " + book.getTitle()
                + " " + book.getAuthor().getId()
                + " " + inputGenreIds).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createById_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getTitle()
                + " " + book.getAuthor().getId()
                + " " + book.getGenreList().toString());
        Mockito.verify(libraryService, Mockito.never()).create(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void readById_correctKey() {
        Integer id = book.getId();
        shell.evaluate(() -> config.getReadById() + " " + id);
        Mockito.verify(libraryService).readById(id);
    }

    @Test
    void readById_incorrectKey() {
        Integer id = book.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(libraryService, Mockito.never()).readById(Mockito.any());
    }

    @Test
    void readByTitle_correctKey() {
        shell.evaluate(() -> config.getReadByTitle() + " " + book.getTitle());
        Mockito.verify(libraryService).readByTitle(book.getTitle());
    }

    @Test
    void readByTitle_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + book.getTitle());
        Mockito.verify(libraryService, Mockito.never()).readByTitle(Mockito.any());
    }

    @Test
    void readAll_correctKey() {
        shell.evaluate(() -> config.getReadAll());
        Mockito.verify(libraryService).readAll();
    }

    @Test
    void readAll_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(libraryService, Mockito.never()).readAll();
    }

    @Test
    void update_correctKey() {
        String inputGenreNames = "name1,name2";

        Mockito.when(parseService.parseLinesToListStrByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getUpdate()
                + " " + book.getId()
                + " " + book.getTitle()
                + " " + book.getAuthor().getSurname()
                + " " + book.getAuthor().getName()
                + " " + inputGenreNames).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getId()
                + " " + book.getTitle()
                + " " + book.getAuthor().getSurname()
                + " " + book.getAuthor().getName()
                + " " + book.getGenreList().toString());
        Mockito.verify(libraryService, Mockito.never()).update(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void updateById_correctKey() {
        String inputGenreIds = "7,8";

        Mockito.when(parseService.parseLinesToListIntByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId(), genre2.getId()));
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getUpdateById()
                + " " + book.getId()
                + " " + book.getTitle()
                + " " + book.getAuthor().getId()
                + " " + inputGenreIds).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateById_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getId()
                + " " + book.getTitle()
                + " " + book.getAuthor().getId()
                + " " + book.getGenreList().toString());
        Mockito.verify(libraryService, Mockito.never()).update(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void updateByIdAddComment_correctKey() {
        Mockito.when(libraryService.updateAddCommentToBook(book.getId()
                        , book.getCommentList().get(0).getAuthor()
                        , book.getCommentList().get(0).getText()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getUpdateByIdAddComment()
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getAuthor()
                + " " + book.getCommentList().get(0).getText()).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddComment_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getAuthor()
                + " " + book.getCommentList().get(0).getText());
        Mockito.verify(libraryService, Mockito.never()).updateAddCommentToBook(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void updateByIdAddCommentById_correctKey() {
        Mockito.when(libraryService.updateAddCommentByIdToBook(book.getId()
                        , book.getCommentList().get(0).getId()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getUpdateByIdAddCommentById()
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getId()).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddCommentById_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getId());
        Mockito.verify(libraryService, Mockito.never()).updateAddCommentByIdToBook(Mockito.any(), Mockito.any());
    }

    @Test
    void updateByIdUpdateComment_correctKey() {
        Mockito.when(libraryService.updateUpdateCommentToBook(book.getId()
                        , book.getCommentList().get(0).getId()
                        , book.getCommentList().get(0).getText()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = shell.evaluate(() -> config.getUpdateByIdUpdateComment()
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getId()
                + " " + book.getCommentList().get(0).getText()).toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdUpdateComment_incorrectKey() {
        shell.evaluate(() -> "smthWrong"
                + " " + book.getId()
                + " " + book.getCommentList().get(0).getAuthor()
                + " " + book.getCommentList().get(0).getText());
        Mockito.verify(libraryService, Mockito.never()).updateUpdateCommentToBook(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void delete_correctKey() {
        Integer id = book.getId();
        shell.evaluate(() -> config.getDelete() + " " + id);
        Mockito.verify(libraryService).delete(id);
    }

    @Test
    void delete_incorrectKey() {
        Integer id = book.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(libraryService, Mockito.never()).delete(Mockito.any());
    }
}
