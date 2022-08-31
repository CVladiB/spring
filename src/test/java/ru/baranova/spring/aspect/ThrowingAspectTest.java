package ru.baranova.spring.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.author.AuthorRestController;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.repository.entity.AuthorRepository;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.data.author.AuthorService;

@DataJpaTest
@Import(value = {ThrowingAspectTestConfig.class, StopSearchConfig.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
class ThrowingAspectTest {
    @Autowired
    public ThrowingAspect throwingAspect;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorRestController authorRestController;
    @Autowired
    private CheckService checkService;

    @Test
    void appDataAccessExceptionHandler__returnResult() {
        Integer id = 3;
        String inputSurname = "SurnameTest" + id;
        String inputName = "NameTest" + id;
        Author test = new Author(id, inputSurname, inputName);

        Author expected = test;
        Author actual = authorRepository.save(test);
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void appDataAccessExceptionHandler__catchExceptionReturnNull() {
        String input = "SmthsSmthsSmthsSmthsSmths";
        Assertions.assertNull(authorRepository.save(new Author(null, input, input)));
    }

    @Test
    void serviceNPEMaker__returnResult() {
        Integer id = 4;
        String inputSurname = "SurnameTest" + id;
        String inputName = "NameTest" + id;
        Author test = new Author(id, inputSurname, inputName);

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);

        Author expected = test;
        Author actual = authorService.create(inputSurname, inputName);
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void serviceNPEMaker__throwsNPEAfterNull() {
        String input = "SmthsSmthsSmthsSmthsSmths";
        Assertions.assertThrows(NullPointerException.class, () -> authorService.create(input, input));
    }

    @Test
    void serviceNPEMaker__throwsNPEAfterEmptyList() {
        authorRepository.deleteAll(authorRepository.findAll());
        Assertions.assertThrows(NullPointerException.class, () -> authorService.readAll());
    }

    @Test
    void controllersNPEHandler__catchNPEReturnErrorMessage() {
        String inputSurname = "-";
        String inputName = "-";

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.FALSE);

        Author expected = new Author();
        Author actual = authorRestController.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void controllersNPEHandler__returnMessage() {
        Integer id = 6;
        String inputSurname = "SurnameTest" + id;
        String inputName = "NameTest" + id;

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.TRUE);

        Author expected = new Author(id, inputSurname, inputName);
        Author actual = authorRestController.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }
}
