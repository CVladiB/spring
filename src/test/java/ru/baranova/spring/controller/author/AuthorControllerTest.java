package ru.baranova.spring.controller.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.data.author.AuthorService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(value = {StopSearchConfig.class, AuthorController.class})
class AuthorControllerTest {
    @MockBean
    private AuthorService authorService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    private Author author;
    private Author insertAuthor1;
    private Author insertAuthor2;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        authorList = List.of(insertAuthor1, insertAuthor2);
        author = new Author(7, "surname", "name");
    }

    @Test
    void create_correct() throws Exception {
        Mockito.when(authorService.create(author.getSurname(), author.getName())).thenReturn(author);
        mvc.perform(put("/list/author/ac").sessionAttr("author", author))
                .andExpect(status().isOk())
                .andExpect(view().name("list/author/ac"))
                .andExpect(model().attribute("author", hasProperty("surname", equalTo(insertAuthor1.getSurname()))))
                .andExpect(model().attribute("author", hasProperty("name", equalTo(insertAuthor1.getName()))));
    }

    @Test
    void readById_correct() throws Exception {
        Mockito.when(authorService.readById(insertAuthor1.getId())).thenReturn(insertAuthor1);
        mvc.perform(get("/author/ar-id").param("id", insertAuthor1.getId().toString()))
                // .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(insertAuthor1)));
    }

    @Test
    void readBySurnameAndName_correct() throws Exception {
        Mockito.when(authorService.readBySurnameAndName(insertAuthor1.getSurname(), insertAuthor1.getName())).thenReturn(List.of(insertAuthor1));
        mvc.perform(get("/author/ar")
                        .param("surname", insertAuthor1.getSurname())
                        .param("name", insertAuthor1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(insertAuthor1))));
    }


    @Test
    void readAll_correct() throws Exception {
        Mockito.when(authorService.readAll()).thenReturn(authorList);
        mvc.perform(get("/author/ar-all"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorList)));
    }

    @Test
    void update_correct() throws Exception {
        author.setId(insertAuthor1.getId());
        Mockito.when(authorService.update(author.getId(), author.getSurname(), author.getName())).thenReturn(author);
        mvc.perform(patch("/author/au/1")
                        .param("surname", author.getSurname())
                        .param("name", author.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(author)));
    }

    @Test
    void delete_correct() throws Exception {
        mvc.perform(delete("/author/ad").param("id", insertAuthor1.getId().toString()))
                .andExpect(status().isOk());
        Mockito.verify(authorService, Mockito.times(1)).delete(insertAuthor1.getId());
    }
}
