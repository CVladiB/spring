package ru.baranova.spring.controller.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {AuthorShellControllerTestConfig.class, StopSearchConfig.class})
class AuthorShellControllerTest {
    @Autowired
    private AuthorService authorServiceImpl;
    @Autowired
    private EntityPrintVisitor printer;
    @Autowired
    private AuthorShellController authorShellController;
    @Autowired
    private AuthorShellControllerTestConfig config;

    @Test
    void create_correct() {
        Author author = new Author(7, "surname", "name");
        Mockito.when(authorServiceImpl.create(author.getSurname(), author.getName())).thenReturn(author);
        String expected = String.format(config.getCOMPLETE_CREATE(), author.getId());
        String actual = authorShellController.create(author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void create_incorrect() {
        Author author = new Author(7, "surname", "name");
        Mockito.when(authorServiceImpl.create(author.getSurname(), author.getName())).thenReturn(null);
        String expected = config.getWARNING();
        String actual = authorShellController.create(author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_correct() {
        Author author = new Author(7, "surname", "name");
        Integer id = author.getId();
        Mockito.when(authorServiceImpl.readById(id)).thenReturn(author);
        Mockito.doNothing().when(printer).print(author);
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readById_incorrect() {
        Integer id = 7;
        Mockito.when(authorServiceImpl.readById(id)).thenReturn(null);
        String expected = config.getWARNING();
        String actual = authorShellController.readById(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readBySurnameAndName_correct() {
        Author author1 = new Author(7, "surname", "name1");
        Author author2 = new Author(8, "surname", "name2");
        String inputSurname = "surname";
        String inputName = "-";
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName))
                .thenReturn(List.of(author1, author2));
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readBySurnameAndName_incorrect() {
        String inputSurname = "surname";
        String inputName = "-";
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenReturn(new ArrayList<>());
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getWARNING();
        String actual = authorShellController.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_correct() {
        Author author1 = new Author(7, "surname", "name1");
        Author author2 = new Author(8, "surname", "name2");
        Mockito.when(authorServiceImpl.readAll())
                .thenReturn(List.of(author1, author2));
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getCOMPLETE_OUTPUT();
        String actual = authorShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readAll_incorrect() {
        Mockito.when(authorServiceImpl.readAll()).thenReturn(new ArrayList<>());
        Mockito.doNothing().when(printer).print((Author) Mockito.any());
        String expected = config.getWARNING();
        String actual = authorShellController.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_correct() {
        Author author = new Author(7, "surname", "name");
        Mockito.when(authorServiceImpl.update(author.getId(), author.getSurname(), author.getName())).thenReturn(author);
        String expected = config.getCOMPLETE_UPDATE();
        String actual = authorShellController.update(author.getId(), author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void update_incorrect() {
        Author author = new Author(7, "surname", "name");
        Mockito.when(authorServiceImpl.update(author.getId(), author.getSurname(), author.getName())).thenReturn(null);
        String expected = config.getWARNING();
        String actual = authorShellController.update(author.getId(), author.getSurname(), author.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_correct() {
        Integer id = 7;
        Mockito.when(authorServiceImpl.delete(id)).thenReturn(true);
        String expected = config.getCOMPLETE_DELETE();
        String actual = authorShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void delete_incorrect() {
        Integer id = 7;
        Mockito.when(authorServiceImpl.delete(id)).thenReturn(false);
        String expected = config.getWARNING();
        String actual = authorShellController.delete(id);
        Assertions.assertEquals(expected, actual);
    }
}