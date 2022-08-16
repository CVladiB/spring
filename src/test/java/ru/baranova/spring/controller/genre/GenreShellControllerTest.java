package ru.baranova.spring.controller.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.GenreShellController;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;


@SpringBootTest(classes = {GenreShellControllerTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class GenreShellControllerTest {
    @Autowired
    private GenreService genreService;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private GenreShellController genreShellController;
    @Autowired
    private GenreShellControllerTestConfig config;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = new Genre(7, "name", "description");
    }

    @Test
    void create_correct() {
        Mockito.when(genreService.create(genre.getName(), genre.getDescription())).thenReturn(genre);
        String expected = String.format(config.getCOMPLETE_CREATE(), genre.getId());
        String actual = genreShellController.create(genre.getName(), genre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrect() {
        Mockito.when(genreService.create(genre.getName(), genre.getDescription())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.create(genre.getName(), genre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Integer id = genre.getId();
        Mockito.when(genreService.readById(id)).thenReturn(genre);
        Mockito.doNothing().when(printer).print(genre);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = genreShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrectRead() {
        Integer id = genre.getId();
        Mockito.when(genreService.readById(id)).thenReturn(genre);
        Mockito.doThrow(NullPointerException.class).when(printer).print(genre);
        String expected = config.getWARNING();
        String actual = genreShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = genre.getId();
        Mockito.when(genreService.readById(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByName_correct() {
        String inputName = genre.getName();
        Mockito.when(genreService.readByName(inputName))
                .thenReturn(genre);
        Mockito.doNothing().when(printer).print((Genre) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = genreShellController.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByName_incorrectRead() {
        String inputName = genre.getName();
        Mockito.when(genreService.readByName(inputName)).thenReturn(genre);
        Mockito.doThrow(NullPointerException.class).when(printer).print((Genre) Mockito.any());
        String expected = config.getWARNING();
        String actual = genreShellController.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readByName_incorrect() {
        String inputName = genre.getName();
        Mockito.when(genreService.readByName(inputName)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.readByName(inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        Genre genre1 = genre;
        Genre genre2 = new Genre(8, "surname", "name2");
        Mockito.when(genreService.readAll())
                .thenReturn(List.of(genre1, genre2));
        Mockito.doNothing().when(printer).print((Genre) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = genreShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrectRead() {
        Genre genre1 = genre;
        Genre genre2 = new Genre(8, "surname", "name2");
        Mockito.when(genreService.readAll()).thenReturn(List.of(genre1, genre2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Genre) Mockito.any());
        String expected = config.getWARNING();
        String actual = genreShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(genreService.readAll()).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        Mockito.when(genreService.update(genre.getId(), genre.getName(), genre.getDescription())).thenReturn(genre);
        String expected = config.getCOMPLETE_UPDATE();
        String actual = genreShellController.update(genre.getId(), genre.getName(), genre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrect() {
        Mockito.when(genreService.update(genre.getId(), genre.getName(), genre.getDescription())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.update(genre.getId(), genre.getName(), genre.getDescription());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = genre.getId();
        Mockito.when(genreService.delete(id)).thenReturn(true);
        String expected = config.getCOMPLETE_DELETE();
        String actual = genreShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = genre.getId();
        Mockito.when(genreService.delete(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = genreShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }
}