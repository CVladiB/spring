package ru.baranova.spring.service.data.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplUpdateTest {
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private CheckService checkService;
    @Autowired
    private AuthorService authorService;
    private int minInput;
    private int maxInputSurname;
    private int maxInputName;
    private Author insertAuthor1;
    private Author testAuthor;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);
        minInput = 3;
        maxInputSurname = 20;
        maxInputName = 15;
    }

    @Test
    void author__update__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorDao.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setId(inputId);
        Mockito.when(authorDao.update(inputId, inputSurname, inputName)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_Exception__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorDao.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setId(inputId);
        Mockito.when(authorDao.update(inputId, inputSurname, inputName)).thenReturn(null);

        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }

    @Test
    void author__update_IncorrectSurname__correctReturnObject() {
        String inputSurname = "smth";
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorDao.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(insertAuthor1.getSurname());
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setSurname(insertAuthor1.getSurname());
        testAuthor.setId(inputId);
        Mockito.when(authorDao.update(inputId, insertAuthor1.getSurname(), inputName)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_IncorrectName__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = "smth";
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorDao.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(insertAuthor1.getName());
        testAuthor.setName(insertAuthor1.getName());
        testAuthor.setId(inputId);
        Mockito.when(authorDao.update(inputId, inputSurname, insertAuthor1.getName())).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = authorList.size() + 1;
        Mockito.when(authorDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }

    @Test
    void author__update_ExistNonexistentId__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorDao.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(authorDao.update(inputId, inputSurname, inputName)).thenReturn(null);

        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }
}