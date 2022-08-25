package ru.baranova.spring.service.data.author;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.repository.entity.AuthorRepository;
import ru.baranova.spring.service.app.CheckService;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = {AuthorServiceImplTestConfig.class, StopSearchConfig.class})
class AuthorServiceImplUpdateTest {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CheckService checkService;
    @Autowired
    private AuthorService authorService;
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
    void author__update__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.of(insertAuthor1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setId(inputId);
        Mockito.when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_Exception__returnNull() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.of(insertAuthor1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setId(inputId);
        Mockito.when(authorRepository.save(testAuthor)).thenReturn(null);

        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }

    @Test
    void author__update_IncorrectSurname__correctReturnObject() {
        String inputSurname = "smth";
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.of(insertAuthor1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(insertAuthor1.getSurname());
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputName);
        testAuthor.setSurname(insertAuthor1.getSurname());
        testAuthor.setId(inputId);
        Mockito.when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_IncorrectName__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = "smth";
        Integer inputId = insertAuthor1.getId();

        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.of(insertAuthor1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(insertAuthor1.getName());
        testAuthor.setName(insertAuthor1.getName());
        testAuthor.setId(inputId);
        Mockito.when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        Author expected = testAuthor;
        Author actual = authorService.update(inputId, inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__update_NonexistentId__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = authorList.size() + 1;
        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.empty());
        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }

    @Test
    void author__update_ExistNonexistentId__correctReturnObject() {
        String inputSurname = testAuthor.getSurname();
        String inputName = testAuthor.getName();
        Integer inputId = insertAuthor1.getId();
        testAuthor.setId(inputId);

        Mockito.when(authorRepository.findById(inputId)).thenReturn(Optional.of(insertAuthor1));
        Mockito.when(checkService.doCheck(Mockito.eq(insertAuthor1), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputSurname), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(checkService.correctOrDefault(Mockito.eq(inputName), Mockito.any(), Mockito.any())).thenReturn(inputSurname);
        Mockito.when(authorRepository.save(testAuthor)).thenReturn(null);

        Assertions.assertNull(authorService.update(inputId, inputSurname, inputName));
    }
}
