package ru.baranova.spring.controller.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.aspect.ThrowingAspect;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@SpringBootTest(classes = {AuthorShellControllerTestConfig.class, StopSearchConfig.class, ThrowingAspect.class})
class AuthorShellControllerTest {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private AuthorShellController authorShellController;
    @Autowired
    private AuthorShellControllerTestConfig config;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author(7, "surname", "name");
    }

    @Test
    void create_correct() {
        Mockito.when(authorService.create(author.getSurname(), author.getName())).thenReturn(author);
        String expected = String.format(config.getCOMPLETE_CREATE(), author.getId());
        String actual = authorShellController.create(author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrect() {
        Mockito.when(authorService.create(author.getSurname(), author.getName())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = authorShellController.create(author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Integer id = author.getId();
        Mockito.when(authorService.readById(id)).thenReturn(author);
        Mockito.doNothing().when(printer).print(author);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrectRead() {
        Integer id = author.getId();
        Mockito.when(authorService.readById(id)).thenReturn(author);
        Mockito.doThrow(NullPointerException.class).when(printer).print(author);
        String expected = config.getWARNING();
        String actual = authorShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = author.getId();
        Mockito.when(authorService.readById(id)).thenThrow(NullPointerException.class);
        Mockito.doNothing().when(printer).print(author);
        String expected = config.getWARNING();
        String actual = authorShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readBySurnameAndName_correct() {
        Author author1 = author;
        Author author2 = new Author(8, "surname", "name2");
        String inputSurname = author.getSurname();
        String inputName = "-";
        Mockito.when(authorService.readBySurnameAndName(inputSurname, inputName))
                .thenReturn(List.of(author1, author2));
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readBySurnameAndName_incorrectRead() {
        Author author1 = author;
        Author author2 = new Author(8, "surname", "name2");
        String inputSurname = author.getSurname();
        String inputName = "-";
        Mockito.when(authorService.readBySurnameAndName(inputSurname, inputName))
                .thenReturn(List.of(author1, author2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Author) Mockito.any());
        String expected = config.getWARNING();
        String actual = authorShellController.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readBySurnameAndName_incorrect() {
        String inputSurname = author.getSurname();
        String inputName = "-";
        Mockito.when(authorService.readBySurnameAndName(inputSurname, inputName)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = authorShellController.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        Author author1 = author;
        Author author2 = new Author(8, "surname", "name2");
        Mockito.when(authorService.readAll())
                .thenReturn(List.of(author1, author2));
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrectRead() {
        Author author1 = author;
        Author author2 = new Author(8, "surname", "name2");
        Mockito.when(authorService.readAll()).thenReturn(List.of(author1, author2));
        Mockito.doThrow(NullPointerException.class).when(printer).print((Author) Mockito.any());
        String expected = config.getWARNING();
        String actual = authorShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(authorService.readAll()).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = authorShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        Mockito.when(authorService.update(author.getId(), author.getSurname(), author.getName())).thenReturn(author);
        String expected = config.getCOMPLETE_UPDATE();
        String actual = authorShellController.update(author.getId(), author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrect() {
        Mockito.when(authorService.update(author.getId(), author.getSurname(), author.getName())).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = authorShellController.update(author.getId(), author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = author.getId();
        Mockito.when(authorService.delete(id)).thenReturn(Boolean.TRUE);
        String expected = config.getCOMPLETE_DELETE();
        String actual = authorShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = author.getId();
        Mockito.when(authorService.delete(id)).thenThrow(NullPointerException.class);
        String expected = config.getWARNING();
        String actual = authorShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }
}
