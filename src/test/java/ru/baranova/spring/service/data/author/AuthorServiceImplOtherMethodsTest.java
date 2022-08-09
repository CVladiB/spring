package ru.baranova.spring.service.data.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplOtherMethodsTest {
    @Autowired
    private AuthorService authorServiceImpl;
    private Author insertAuthor1;
    private Author testAuthor;
    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        testAuthor = new Author(null, "SurnameTest", "NameTest");
        authorList = List.of(insertAuthor1, insertAuthor2);
    }

    @Test
    void checkExistId__true() {
        Integer inputId = 1;
        Mockito.when(authorServiceImpl.readById(inputId)).thenReturn(authorList.get(0));
        Assertions.assertTrue(authorServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId_Exception__false() {
        Integer inputId = 1;
        Mockito.when(authorServiceImpl.readById(inputId)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertFalse(authorServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistId__false() {
        Integer inputId = 100;
        Mockito.when(authorServiceImpl.readById(inputId)).thenReturn(null);
        Assertions.assertFalse(authorServiceImpl.checkExist(inputId));
    }

    @Test
    void checkExistNameSurname_Exception__true() {
        String inputSurname = testAuthor.getSurname();
        String inputName = insertAuthor1.getName();
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenThrow(DataIntegrityViolationException.class);
        Assertions.assertTrue(authorServiceImpl.checkIfNotExist(inputSurname, inputName));
    }

    @Test
    void checkExistNameSurname__true() {
        String inputSurname = insertAuthor1.getSurname();
        String inputName = insertAuthor1.getName();
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenReturn(List.of(insertAuthor1));
        Assertions.assertFalse(authorServiceImpl.checkIfNotExist(inputSurname, inputName));
    }

    @Test
    void checkExistNameSurname_NonExistSurname__false() {
        String inputSurname = testAuthor.getSurname();
        String inputName = insertAuthor1.getName();
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenReturn(new ArrayList<>());
        Assertions.assertTrue(authorServiceImpl.checkIfNotExist(inputSurname, inputName));
    }

    @Test
    void checkExistNameSurname_NonExistName__false() {
        String inputSurname = insertAuthor1.getSurname();
        String inputName = testAuthor.getName();
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenReturn(new ArrayList<>());
        Assertions.assertTrue(authorServiceImpl.checkIfNotExist(inputSurname, inputName));
    }

    @Test
    void checkExistNameSurname_NonExistSurnameAndName__false() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Mockito.when(authorServiceImpl.readBySurnameAndName(inputSurname, inputName)).thenReturn(new ArrayList<>());
        Assertions.assertTrue(authorServiceImpl.checkIfNotExist(inputSurname, inputName));
    }
}