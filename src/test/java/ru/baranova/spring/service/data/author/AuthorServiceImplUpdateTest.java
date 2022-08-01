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
import ru.baranova.spring.service.app.ParseService;

import java.util.List;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplUpdateTest {
    @Autowired
    private AuthorDao authorDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private ParseService parseServiceImpl;
    @Autowired
    private AuthorService authorServiceImpl;
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
        int countAffectedRows = 1;
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.doReturn(null).when(authorServiceImpl).readBySurnameAndName(inputSurname, inputName);

        Mockito.when(authorDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testAuthor.setId(inputId);
        Author expected = testAuthor;
        Author actual = authorServiceImpl.update(inputId, inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_IncorrectSurname__correctReturnObject() {
        int countAffectedRows = 1;
        String inputSurname = "smth";
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);
        Mockito.doReturn(null).when(authorServiceImpl).readBySurnameAndName(inputSurname, inputName);
        Mockito.when(authorDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testAuthor.setId(inputId);
        testAuthor.setSurname(insertAuthor1.getSurname());
        Author expected = testAuthor;
        Author actual = authorServiceImpl.update(inputId, inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_IncorrectName__correctReturnObject() {
        int countAffectedRows = 1;
        String inputSurname = testAuthor.getSurname();
        String inputName = "smth";
        Integer inputId = insertAuthor1.getId();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);
        Mockito.doReturn(null).when(authorServiceImpl).readBySurnameAndName(inputSurname, inputName);

        Mockito.when(authorDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        testAuthor.setId(inputId);
        testAuthor.setName(insertAuthor1.getName());
        Author expected = testAuthor;
        Author actual = authorServiceImpl.update(inputId, inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = authorList.size() + 1;

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(authorServiceImpl.update(inputId, inputSurname, inputName));
    }

    @Test
    void author__update_ExistNonexistentId__correctReturnObject() {
        int countAffectedRows = 0;
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.getById(inputId)).thenReturn(insertAuthor1);
        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.update(Mockito.any())).thenReturn(countAffectedRows);

        Assertions.assertNull(authorServiceImpl.update(inputId, inputSurname, inputName));
    }
}