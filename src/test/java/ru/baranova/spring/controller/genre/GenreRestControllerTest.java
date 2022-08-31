package ru.baranova.spring.controller.genre;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = {StopSearchConfig.class, GenreRestController.class})
class GenreRestControllerTest {
    @MockBean
    private GenreService genreService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Genre genre;
    private Genre insertGenre1;
    private Genre insertGenre2;
    private Genre testGenre;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);
        genre = new Genre(7, "name", "description");
    }

    @Test
    void create_correct() throws Exception {
        Mockito.when(genreService.create(genre.getName(), genre.getDescription())).thenReturn(genre);
        mvc.perform(put("/genre/gc")
                        .param("name", genre.getName())
                        .param("description", genre.getDescription()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genre)));
    }

    @Test
    void readById_correct() throws Exception {
        Mockito.when(genreService.readById(insertGenre1.getId())).thenReturn(insertGenre1);
        mvc.perform(get("/genre/gr-id").param("id", insertGenre1.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(insertGenre1)));
    }

    @Test
    void readByName_correct() throws Exception {
        Mockito.when(genreService.readByName(insertGenre1.getName())).thenReturn(List.of(insertGenre1));
        mvc.perform(get("/genre/gr")
                        .param("name", insertGenre1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(insertGenre1))));
    }


    @Test
    void readAll_correct() throws Exception {
        Mockito.when(genreService.readAll()).thenReturn(genreList);
        mvc.perform(get("/genre/gr-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreList)));
    }

    @Test
    void update_correct() throws Exception {
        genre.setId(insertGenre1.getId());
        Mockito.when(genreService.update(genre.getId(), genre.getName(), genre.getDescription())).thenReturn(genre);
        mvc.perform(patch("/genre/gu")
                        .param("id", genre.getId().toString())
                        .param("name", genre.getName())
                        .param("description", genre.getDescription()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genre)));
    }

    @Test
    void delete_correct() throws Exception {
        mvc.perform(delete("/genre/gd").param("id", insertGenre1.getId().toString()))
                .andExpect(status().isOk());
        Mockito.verify(genreService, Mockito.times(1)).delete(insertGenre1.getId());
    }
}