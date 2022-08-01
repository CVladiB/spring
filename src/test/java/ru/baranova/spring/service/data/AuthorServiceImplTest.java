package ru.baranova.spring.service.data;

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
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.data.config.AuthorServiceImplTestConfig;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplTest {
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
    private Author insertAuthor2;
    private Author testAuthor;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);
        minInput = 3;
        maxInputSurname = 20;
        maxInputName = 15;
    }

    @Test
    void author__create__correctReturnNewObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer newId = authorList.size() + 1;

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.create(Mockito.any())).thenReturn(newId);

        testAuthor.setId(newId);
        Author expected = testAuthor;
        Author actual = authorServiceImpl.create(inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__create_ExistSurname__returnNull() {
        String inputSurname = insertAuthor1.getSurname();
        String inputName = testAuthor.getName();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_ExistName__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = insertAuthor1.getName();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);


        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_IncorrectSurname__returnNull() {
        String inputSurname = "smth";
        String inputName = testAuthor.getName();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_IncorrectName__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = "smth";

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__readById__correctReturnObject() {
        Integer inputId = authorList.size();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.doReturn(authorList.get(inputId - 1)).when(authorDaoJdbc).getById(inputId);

        Author expected = authorList.get(inputId - 1);
        Author actual = authorServiceImpl.readById(inputId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readById_NonexistentId__returnNull() {
        Integer inputId = authorList.size() + 1;

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertNull(authorServiceImpl.readById(inputId));
    }

    @Test
    void author__readBySurnameAndName__correctReturnListObject() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = authorList.get(0).getName();

        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.getBySurnameAndName(inputSurname, inputName))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorServiceImpl.readBySurnameAndName(inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullSurname__correctReturnListObject() {
        String inputSurname = "-";
        String inputName = authorList.get(0).getName();

        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(null);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.getBySurnameAndName(null, inputName))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorServiceImpl.readBySurnameAndName(inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullName__correctReturnListObject() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = "-";

        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(null);

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.getBySurnameAndName(inputSurname, null))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorServiceImpl.readBySurnameAndName(inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullSurnameName__returnNull() {
        String inputSurname = "-";
        String inputName = "-";

        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(null);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(null);

        Assertions.assertNull(authorServiceImpl.readBySurnameAndName(inputSurname, inputName));
    }

    @Test
    void author__readBySurnameAndName_DifferentSurnameAndName__returnNull() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = authorList.get(1).getName();

        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);

        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputSurname), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputName), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);

        Mockito.when(authorDaoJdbc.getBySurnameAndName(inputSurname, inputName))
                .thenReturn(new ArrayList<>());

        List<Author> expected = new ArrayList<>();
        List<Author> actual = authorServiceImpl.readBySurnameAndName(inputSurname, inputName);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    void author__readAll__correctReturnListObject() {
        Mockito.doReturn(authorList).when(authorDaoJdbc).getAll();

        List<Author> expected = authorList;
        List<Author> actual = authorServiceImpl.readAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update__correctReturnObject() {
        int countAffectedRows = 1;
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = 1;

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
        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.FALSE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.TRUE);

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
        Mockito.when(parseServiceImpl.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseServiceImpl.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputSurname, minInput, maxInputSurname))
                .thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.isCorrectSymbolsInInputString(inputName, minInput, maxInputName))
                .thenReturn(Boolean.FALSE);

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

    @Test
    void author__delete__true() {
        int countAffectedRows = 1;
        Integer inputId = authorList.size();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertTrue(authorServiceImpl.delete(inputId));
    }

    @Test
    void author__delete_NonexistentId__false() {
        Integer inputId = authorList.size() + 1;

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.FALSE);

        Assertions.assertFalse(authorServiceImpl.delete(inputId));
    }

    @Test
    void author__delete_ExistNonexistentId__false() {
        int countAffectedRows = 0;
        Integer inputId = authorList.size();

        Mockito.doReturn(authorList).when(authorServiceImpl).readAll();
        Mockito.when(checkServiceImpl.isInputExist(Mockito.eq(inputId), Mockito.any(), Mockito.any()))
                .thenReturn(Boolean.TRUE);
        Mockito.when(authorDaoJdbc.delete(inputId)).thenReturn(countAffectedRows);

        Assertions.assertFalse(authorServiceImpl.delete(inputId));
    }

}