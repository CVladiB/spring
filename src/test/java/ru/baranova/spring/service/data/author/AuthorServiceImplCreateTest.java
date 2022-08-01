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
class AuthorServiceImplCreateTest {
    @Autowired
    private AuthorDao authorDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
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
}