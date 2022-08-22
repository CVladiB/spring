package ru.baranova.spring.service.data.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.dao.entity.author.AuthorDao;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.app.ParseService;

import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplReadTest {
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private ParseService parseService;
    @Autowired
    private AuthorService authorService;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        authorList = List.of(insertAuthor1, insertAuthor2);
    }

    @Test
    void author__readById__correctReturnObject() {
        Integer inputId = authorList.size();

        Mockito.doReturn(authorList.get(inputId - 1)).when(authorDao).getById(inputId);

        Author expected = authorList.get(inputId - 1);
        Author actual = authorService.readById(inputId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readById_NonexistentId__returnNull() {
        Integer inputId = authorList.size() + 1;
        Assertions.assertNull(authorService.readById(inputId));
    }

    @Test
    void author__readById_Exception__returnNull() {
        Integer inputId = authorList.size();
        Mockito.when(authorDao.getById(inputId)).thenReturn(null);
        Assertions.assertNull(authorService.readById(inputId));
    }

    @Test
    void author__readBySurnameAndName__correctReturnListObject() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = authorList.get(0).getName();

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(authorDao.getBySurnameAndName(inputSurname, inputName))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullSurname__correctReturnListObject() {
        String inputSurname = "-";
        String inputName = authorList.get(0).getName();

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(null);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(authorDao.getBySurnameAndName(null, inputName))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullName__correctReturnListObject() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = "-";

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(null);
        Mockito.when(authorDao.getBySurnameAndName(inputSurname, null))
                .thenReturn(List.of(authorList.get(0)));

        List<Author> expected = List.of(authorList.get(0));
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_NullSurnameName__returnNull() {
        String inputSurname = "-";
        String inputName = "-";

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(null);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(null);

        List<Author> expected = Collections.emptyList();
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_DifferentSurnameAndName__returnNull() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = authorList.get(1).getName();

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(authorDao.getBySurnameAndName(inputSurname, inputName))
                .thenReturn(Collections.emptyList());

        List<Author> expected = Collections.emptyList();
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readBySurnameAndName_Exception__returnNull() {
        String inputSurname = authorList.get(0).getSurname();
        String inputName = authorList.get(1).getName();

        Mockito.when(parseService.parseDashToNull(inputSurname)).thenReturn(inputSurname);
        Mockito.when(parseService.parseDashToNull(inputName)).thenReturn(inputName);
        Mockito.when(authorDao.getBySurnameAndName(inputSurname, inputName))
                .thenReturn(Collections.emptyList());

        List<Author> expected = Collections.emptyList();
        List<Author> actual = authorService.readBySurnameAndName(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readAll__correctReturnListObject() {
        Mockito.doReturn(authorList).when(authorDao).getAll();
        List<Author> expected = authorList;
        List<Author> actual = authorService.readAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__readAll_Exception__EmptyList() {
        Mockito.when(authorDao.getAll()).thenReturn(Collections.emptyList());
        List<Author> expected = Collections.emptyList();
        List<Author> actual = authorService.readAll();
        Assertions.assertEquals(expected, actual);
    }
}
