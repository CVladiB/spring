package ru.baranova.spring.controller.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.LibraryShellController;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {LibraryShellControllerTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class LibraryShellControllerTest {
    @Autowired
    private LibraryService libraryService;
    @Autowired
    private ParseService parseService;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private LibraryShellController libraryShellController;
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
    void create_correct() {
        String inputGenreNames = "name1,name2";
        Mockito.when(parseService.parseLinesToListStrByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(book);

        String expected = String.format(config.getCOMPLETE_CREATE(), book.getId());
        String actual = libraryShellController.create(book.getTitle()
                , book.getAuthor().getSurname()
                , book.getAuthor().getName()
                , inputGenreNames);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrect() {
        String inputGenreNames = "name1,name2";
        Mockito.when(parseService.parseLinesToListStrByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.create(book.getTitle()
                , book.getAuthor().getSurname()
                , book.getAuthor().getName()
                , inputGenreNames);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createById_correct() {
        String inputGenreIds = "7,8";
        Mockito.when(parseService.parseLinesToListIntByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId(), genre2.getId()));
        Mockito.when(libraryService.create(book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(book);

        String expected = String.format(config.getCOMPLETE_CREATE(), book.getId());
        String actual = libraryShellController.createById(book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createById_incorrect() {
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
        String actual = libraryShellController.createById(book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Integer id = book.getId();
        Mockito.when(libraryService.readById(id)).thenReturn(book);
        Mockito.doNothing().when(printer).print(book);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrectRead() {
        Integer id = book.getId();
        Mockito.when(libraryService.readById(id)).thenReturn(book);
        Mockito.doThrow(NullPointerException.class).when(printer).print(book);
        String expected = config.getWARNING();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = book.getId();
        Mockito.when(libraryService.readById(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_correct() {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        String inputTitle = book.getTitle();
        Mockito.when(libraryService.readByTitle(inputTitle)).thenReturn(List.of(book1, book2));
        Mockito.doNothing().when(printer).print((Book) Mockito.any());

        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_incorrectRead() {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        String inputTitle = book.getTitle();
        Mockito.when(libraryService.readByTitle(inputTitle)).thenReturn(List.of(book1, book2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Book) Mockito.any());

        String expected = config.getWARNING();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_incorrect() {
        String inputTitle = book.getTitle();
        Mockito.when(libraryService.readByTitle(inputTitle)).thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        Mockito.when(libraryService.readAll()).thenReturn(List.of(book1, book2));
        Mockito.doNothing().when(printer).print((Book) Mockito.any());

        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrectRead() {
        Book book1 = book;
        Book book2 = new Book(8, "title", new Author(8, "surname1", "name1"), List.of(genre1), Collections.emptyList());
        Mockito.when(libraryService.readAll()).thenReturn(List.of(book1, book2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Book) Mockito.any());

        String expected = config.getWARNING();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(libraryService.readAll()).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        String inputGenreNames = "name1,name2";
        Mockito.when(parseService.parseLinesToListStrByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(book);

        String expected = config.getCOMPLETE_UPDATE();
        String actual = libraryShellController.update(book.getId()
                , book.getTitle()
                , book.getAuthor().getSurname()
                , book.getAuthor().getName()
                , inputGenreNames);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrect() {
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
        String actual = libraryShellController.update(book.getId()
                , book.getTitle()
                , book.getAuthor().getSurname()
                , book.getAuthor().getName()
                , inputGenreNames);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateById_correct() {
        String inputGenreIds = "7,8";
        Mockito.when(parseService.parseLinesToListIntByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId(), genre2.getId()));
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(book);

        String expected = config.getCOMPLETE_UPDATE();
        String actual = libraryShellController.updateById(book.getId()
                , book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateById_incorrect() {
        String inputGenreIds = "7,8";
        Mockito.when(parseService.parseLinesToListIntByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId(), genre2.getId()));
        Mockito.when(libraryService.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.updateById(book.getId()
                , book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddComment_correct() {
        Mockito.when(libraryService.updateAddCommentToBook(book.getId()
                        , book.getCommentList().get(0).getAuthor()
                        , book.getCommentList().get(0).getText()))
                .thenReturn(book);

        String expected = config.getCOMPLETE_UPDATE();
        String actual = libraryShellController.updateByIdAddComment(book.getId()
                , book.getCommentList().get(0).getAuthor()
                , book.getCommentList().get(0).getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddComment_incorrect() {
        Mockito.when(libraryService.updateAddCommentToBook(book.getId()
                        , book.getCommentList().get(0).getAuthor()
                        , book.getCommentList().get(0).getText()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.updateByIdAddComment(book.getId()
                , book.getCommentList().get(0).getAuthor()
                , book.getCommentList().get(0).getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddCommentById_correct() {
        Mockito.when(libraryService.updateAddCommentByIdToBook(book.getId()
                        , book.getCommentList().get(0).getId()))
                .thenReturn(book);

        String expected = config.getCOMPLETE_UPDATE();
        String actual = libraryShellController.updateByIdAddCommentById(book.getId()
                , book.getCommentList().get(0).getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdAddCommentById_incorrect() {
        Mockito.when(libraryService.updateAddCommentByIdToBook(book.getId()
                        , book.getCommentList().get(0).getId()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.updateByIdAddCommentById(book.getId()
                , book.getCommentList().get(0).getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdUpdateComment_correct() {
        Mockito.when(libraryService.updateUpdateCommentToBook(book.getId()
                        , book.getCommentList().get(0).getId()
                        , book.getCommentList().get(0).getText()))
                .thenReturn(book);

        String expected = config.getCOMPLETE_UPDATE();
        String actual = libraryShellController.updateByIdUpdateComment(book.getId()
                , book.getCommentList().get(0).getId()
                , book.getCommentList().get(0).getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateByIdUpdateComment_incorrect() {
        Mockito.when(libraryService.updateUpdateCommentToBook(book.getId()
                        , book.getCommentList().get(0).getId()
                        , book.getCommentList().get(0).getText()))
                .thenThrow(NullPointerException.class);

        String expected = config.getWARNING();
        String actual = libraryShellController.updateByIdUpdateComment(book.getId()
                , book.getCommentList().get(0).getId()
                , book.getCommentList().get(0).getText());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = book.getId();
        Mockito.when(libraryService.delete(id)).thenReturn(true);
        String expected = config.getCOMPLETE_DELETE();
        String actual = libraryShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = book.getId();
        Mockito.when(libraryService.delete(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = libraryShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

}
