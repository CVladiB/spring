package ru.baranova.spring.service.data.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.repository.entity.GenreRepository;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {GenreServiceImplTestConfig.class, StopSearchConfig.class})
class GenreServiceImplDeleteTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreService genreService;
    @Autowired
    private CheckService checkService;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        Genre insertGenre1 = new Genre(1, "Name1", "Description1");
        Genre insertGenre2 = new Genre(2, "Name2", "Description2");
        genreList = List.of(insertGenre1, insertGenre2);
    }

    @Test
    void genre__delete__true() {
        Integer inputId = genreList.size();
        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.of(genreList.get(1)));
        Mockito.doNothing().when(genreRepository).delete(genreList.get(1));
        Assertions.assertTrue(genreService.delete(inputId));
    }

    @Test
    void genre__delete_NonexistentId__false() {
        Integer inputId = genreList.size() + 1;
        Mockito.when(genreRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertFalse(genreService.delete(inputId));
    }
}
