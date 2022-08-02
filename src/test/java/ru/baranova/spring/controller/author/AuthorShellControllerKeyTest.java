package ru.baranova.spring.controller.author;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.Shell;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@SpringBootTest(classes = {AuthorShellControllerTestConfig.class, StopSearchConfig.class})
class AuthorShellControllerKeyTest {
    @Autowired
    private Shell shell;
    @Autowired
    private AuthorService authorServiceImpl;
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
    void create_correctKey() {
        shell.evaluate(() -> config.getCreate() + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl).create(author.getSurname(), author.getName());
    }

    @Test
    void create_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl, Mockito.never()).create(Mockito.any(), Mockito.any());
    }

    @Test
    void readById_correctKey() {
        Integer id = author.getId();
        shell.evaluate(() -> config.getReadById() + " " + id);
        Mockito.verify(authorServiceImpl).readById(id);
    }

    @Test
    void readById_incorrectKey() {
        Integer id = author.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(authorServiceImpl, Mockito.never()).readById(Mockito.any());
    }

    @Test
    void readBySurnameAndName_correctKey() {
        shell.evaluate(() -> config.getReadBySurnameAndName() + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl).readBySurnameAndName(author.getSurname(), author.getName());
    }

    @Test
    void readBySurnameAndName_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl, Mockito.never()).readBySurnameAndName(Mockito.any(), Mockito.any());
    }

    @Test
    void readAll_correctKey() {
        shell.evaluate(() -> config.getReadAll());
        Mockito.verify(authorServiceImpl).readAll();
    }

    @Test
    void readAll_incorrectKey() {
        shell.evaluate(() -> "smthWrong");
        Mockito.verify(authorServiceImpl, Mockito.never()).readAll();
    }

    @Test
    void update_correctKey() {
        shell.evaluate(() -> config.getUpdate() + " " + author.getId() + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl).update(author.getId(), author.getSurname(), author.getName());
    }

    @Test
    void update_incorrectKey() {
        shell.evaluate(() -> "smthWrong" + " " + author.getId() + " " + author.getSurname() + " " + author.getName());
        Mockito.verify(authorServiceImpl, Mockito.never()).update(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void delete_correctKey() {
        Integer id = author.getId();
        shell.evaluate(() -> config.getDelete() + " " + id);
        Mockito.verify(authorServiceImpl).delete(id);
    }

    @Test
    void delete_incorrectKey() {
        Integer id = author.getId();
        shell.evaluate(() -> "smthWrong" + " " + id);
        Mockito.verify(authorServiceImpl, Mockito.never()).delete(Mockito.any());
    }
}