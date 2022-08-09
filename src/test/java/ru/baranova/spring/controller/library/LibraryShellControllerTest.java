package ru.baranova.spring.controller.library;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.LibraryShellController;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {LibraryShellControllerTestConfig.class, StopSearchConfig.class})
class LibraryShellControllerTest {
    @Autowired
    private LibraryService libraryServiceImpl;
    @Autowired
    private ParseService parseServiceImpl;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private LibraryShellController libraryShellController;
    @Autowired
    private LibraryShellControllerTestConfig config;
    private Genre genre1;
    private Genre genre2;
    private BookDTO book;

    @BeforeEach
    void setUp() {
        Author author = new Author(7, "surname", "name");
        genre1 = new Genre(7, "name1", "description");
        genre2 = new Genre(8, "name2", "description");
        book = new BookDTO(7, "title", author, List.of(genre1, genre2));
    }

    @Test
    void create_correct() {
        String inputGenreNames = "name1,name2";
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryServiceImpl.create(book.getTitle()
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryServiceImpl.create(book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(null);
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId().toString(), genre2.getId().toString()));
        Mockito.when(parseServiceImpl.parseStringToInt(genre1.getId().toString()))
                .thenReturn(genre1.getId());
        Mockito.when(parseServiceImpl.parseStringToInt(genre2.getId().toString()))
                .thenReturn(genre2.getId());
        Mockito.when(libraryServiceImpl.create(book.getTitle()
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId().toString(), genre2.getId().toString()));
        Mockito.when(parseServiceImpl.parseStringToInt(genre1.getId().toString()))
                .thenReturn(genre1.getId());
        Mockito.when(parseServiceImpl.parseStringToInt(genre2.getId().toString()))
                .thenReturn(genre2.getId());
        Mockito.when(libraryServiceImpl.create(book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(null);
        String expected = config.getWARNING();
        String actual = libraryShellController.createById(book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Integer id = book.getId();
        Mockito.when(libraryServiceImpl.readById(id)).thenReturn(book);
        Mockito.doNothing().when(printer).print(book);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrectRead() {
        Integer id = book.getId();
        Mockito.when(libraryServiceImpl.readById(id)).thenReturn(book);
        Mockito.doThrow(NullPointerException.class).when(printer).print(book);
        String expected = config.getWARNING();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = book.getId();
        Mockito.when(libraryServiceImpl.readById(id)).thenReturn(null);
        String expected = config.getWARNING();
        String actual = libraryShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_correct() {
        BookDTO book1 = book;
        BookDTO book2 = new BookDTO(8, "title", new Author(8, "surname1", "name1"), List.of(genre1));
        String inputTitle = book.getTitle();
        Mockito.when(libraryServiceImpl.readByTitle(inputTitle)).thenReturn(List.of(book1, book2));
        Mockito.doNothing().when(printer).print((BookDTO) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_incorrectRead() {
        BookDTO book1 = book;
        BookDTO book2 = new BookDTO(8, "title", new Author(8, "surname1", "name1"), List.of(genre1));
        String inputTitle = book.getTitle();
        Mockito.when(libraryServiceImpl.readByTitle(inputTitle)).thenReturn(List.of(book1, book2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((BookDTO) Mockito.any());
        String expected = config.getWARNING();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByTitle_incorrect() {
        String inputTitle = book.getTitle();
        Mockito.when(libraryServiceImpl.readByTitle(inputTitle)).thenReturn(new ArrayList<>());
        String expected = config.getWARNING();
        String actual = libraryShellController.readByTitle(inputTitle);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        BookDTO book1 = book;
        BookDTO book2 = new BookDTO(8, "title", new Author(8, "surname1", "name1"), List.of(genre1));
        Mockito.when(libraryServiceImpl.readAll()).thenReturn(List.of(book1, book2));
        Mockito.doNothing().when(printer).print((BookDTO) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrectRead() {
        BookDTO book1 = book;
        BookDTO book2 = new BookDTO(8, "title", new Author(8, "surname1", "name1"), List.of(genre1));
        Mockito.when(libraryServiceImpl.readAll()).thenReturn(List.of(book1, book2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((BookDTO) Mockito.any());
        String expected = config.getWARNING();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(libraryServiceImpl.readAll()).thenReturn(new ArrayList<>());
        String expected = config.getWARNING();
        String actual = libraryShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        String inputGenreNames = "name1,name2";
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryServiceImpl.update(book.getId()
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreNames))
                .thenReturn(List.of(genre1.getName(), genre2.getName()));
        Mockito.when(libraryServiceImpl.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getSurname()
                        , book.getAuthor().getName()
                        , List.of(genre1.getName(), genre2.getName())))
                .thenReturn(null);
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId().toString(), genre2.getId().toString()));
        Mockito.when(parseServiceImpl.parseStringToInt(genre1.getId().toString()))
                .thenReturn(genre1.getId());
        Mockito.when(parseServiceImpl.parseStringToInt(genre2.getId().toString()))
                .thenReturn(genre2.getId());
        Mockito.when(libraryServiceImpl.update(book.getId()
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
        Mockito.when(parseServiceImpl.parseLinesToListByComma(inputGenreIds))
                .thenReturn(List.of(genre1.getId().toString(), genre2.getId().toString()));
        Mockito.when(parseServiceImpl.parseStringToInt(genre1.getId().toString()))
                .thenReturn(genre1.getId());
        Mockito.when(parseServiceImpl.parseStringToInt(genre2.getId().toString()))
                .thenReturn(genre2.getId());
        Mockito.when(libraryServiceImpl.update(book.getId()
                        , book.getTitle()
                        , book.getAuthor().getId()
                        , List.of(genre1.getId(), genre2.getId())))
                .thenReturn(null);
        String expected = config.getWARNING();
        String actual = libraryShellController.updateById(book.getId()
                , book.getTitle()
                , book.getAuthor().getId()
                , inputGenreIds);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = book.getId();
        Mockito.when(libraryServiceImpl.delete(id)).thenReturn(true);
        String expected = config.getCOMPLETE_DELETE();
        String actual = libraryShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = book.getId();
        Mockito.when(libraryServiceImpl.delete(id)).thenReturn(false);
        String expected = config.getWARNING();
        String actual = libraryShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

}