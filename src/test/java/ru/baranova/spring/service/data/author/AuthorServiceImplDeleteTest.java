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
class AuthorServiceImplDeleteTest {
    @Autowired
    private AuthorDao authorDaoJdbc;
    @Autowired
    private CheckService checkServiceImpl;
    @Autowired
    private AuthorService authorServiceImpl;

    private List<Author> authorList;

    @BeforeEach
    void setUp() {
        Author insertAuthor1 = new Author(1, "Surname1", "Name1");
        Author insertAuthor2 = new Author(2, "Surname2", "Name2");
        authorList = List.of(insertAuthor1, insertAuthor2);
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