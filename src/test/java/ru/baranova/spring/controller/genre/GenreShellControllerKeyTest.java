package ru.baranova.spring.controller.genre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.genre.GenreService;

@SpringBootTest(classes = {GenreShellControllerTestConfig.class, StopSearchConfig.class})
class GenreShellControllerKeyTest {
    @Autowired
    private Shell shell;
    @Autowired
    private GenreService genreService;
    @Autowired
    private GenreShellControllerTestConfig config;
    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = new Genre(7, "name", "description");
    }

    @Test
    void create_correctKey() {
        shell.evaluate(() -> config.getCreate() + " " + genre.getName() + " " + genre.getDescription());
        Mockito.verify(genreService).create(genre.getName(), genre.getDescription());
    }

    @Test
    void create_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + genre.getName() + " " + genre.getDescription());
        Mockito.verify(genreService, Mockito.never()).create(Mockito.any(), Mockito.any());
    }

    @Test
    void readById_correctKey() {
        Integer id = genre.getId();
        shell.evaluate(() -> config.getReadById() + " " + id);
        Mockito.verify(genreService).readById(id);
    }

    @Test
    void readById_incorrectKey() {
        Integer id = genre.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(genreService, Mockito.never()).readById(Mockito.any());
    }

    @Test
    void readBySurnameAndName_correctKey() {
        shell.evaluate(() -> config.getReadByName() + " " + genre.getName());
        Mockito.verify(genreService).readByName(genre.getName());
    }

    @Test
    void readBySurnameAndName_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + genre.getName());
        Mockito.verify(genreService, Mockito.never()).readByName(Mockito.any());
    }

    @Test
    void readAll_correctKey() {
        shell.evaluate(() -> config.getReadAll());
        Mockito.verify(genreService).readAll();
    }

    @Test
    void readAll_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(genreService, Mockito.never()).readAll();
    }

    @Test
    void update_correctKey() {
        shell.evaluate(() -> config.getUpdate() + " " + genre.getId() + " " + genre.getName() + " " + genre.getDescription());
        Mockito.verify(genreService).update(genre.getId(), genre.getName(), genre.getDescription());
    }

    @Test
    void update_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + genre.getId() + " " + genre.getName() + " " + genre.getDescription());
        Mockito.verify(genreService, Mockito.never()).update(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void delete_correctKey() {
        Integer id = genre.getId();
        shell.evaluate(() -> config.getDelete() + " " + id);
        Mockito.verify(genreService).delete(id);
    }

    @Test
    void delete_incorrectKey() {
        Integer id = genre.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(genreService, Mockito.never()).delete(Mockito.any());
    }
}