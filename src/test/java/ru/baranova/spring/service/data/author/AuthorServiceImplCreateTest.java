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
    private CheckService checkServiceImpl;
    @Autowired
    private AuthorService authorServiceImpl;
    @Autowired
    private AuthorDao authorDao;
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

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        testAuthor.setId(newId);
        Mockito.when(authorDao.create(inputSurname, inputName)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorServiceImpl.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__create_Exception__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(authorDao.create(inputSurname, inputName)).thenReturn(null);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_ExistNameSurnameException__returnNull() {
        String inputSurname = insertAuthor1.getSurname();
        String inputName = insertAuthor1.getName();
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.FALSE);
        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_ExistSurname__correctReturnNewObject() {
        testAuthor.setSurname(insertAuthor1.getSurname());
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer newId = authorList.size() + 1;

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        testAuthor.setId(newId);
        Mockito.when(authorDao.create(inputSurname, inputName)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorServiceImpl.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__create_ExistName__correctReturnNewObject() {
        String inputSurname = testAuthor.getSurname();
        testAuthor.setName(insertAuthor1.getName());
        String inputName = testAuthor.getName();
        Integer newId = authorList.size() + 1;

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);
        testAuthor.setId(newId);
        Mockito.when(authorDao.create(inputSurname, inputName)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorServiceImpl.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__create_IncorrectSurname__returnNull() {
        String inputSurname = "smth";
        String inputName = testAuthor.getName();

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }

    @Test
    void author__create_IncorrectName__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = "smth";

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertNull(authorServiceImpl.create(inputSurname, inputName));
    }
}