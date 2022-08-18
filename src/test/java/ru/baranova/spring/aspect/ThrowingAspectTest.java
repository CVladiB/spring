package ru.baranova.spring.aspect;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.repository.author.AuthorDao;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.data.author.AuthorService;

@DataJpaTest
@Import(value = {ThrowingAspectTestConfig.class, StopSearchConfig.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
class ThrowingAspectTest {
    @Autowired
    public ThrowingAspect throwingAspect;
    @Autowired
    private AuthorDao authorDao;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorShellController authorShellController;
    @Autowired
    private CheckService checkService;

    @Test
    void appDataAccessExceptionHandler__returnResult() {
        Integer id = 3;
        String inputSurname = "SurnameTest" + id;
        String inputName = "NameTest" + id;
        Author test = new Author(id, inputSurname, inputName);

        Author expected = test;
        Author actual = authorDao.create(inputSurname, inputName);
        Assertions.assertEquals(expected.getSurname(), actual.getSurname());
        Assertions.assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void appDataAccessExceptionHandler__catchExceptionReturnNull() {
        String input = "SmthsSmthsSmthsSmthsSmths";
        Assertions.assertNull(authorDao.create(input, input));
    }

    @Test
    void appDataAccessExceptionHandler__catchExceptionReturnFalse() {
        Integer input = 100;
        Assertions.assertFalse(authorDao.delete(input));
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
    void serviceNPEMaker__throwsNPEAfterFalse() {
        Integer input = 100;
        Assertions.assertThrows(NullPointerException.class, () -> authorService.delete(input));
    }

    @Test
    void serviceNPEMaker__throwsNPEAfterEmptyList() {
        authorDao.getAll().stream().map(Author::getId).forEach(authorDao::delete);
        Assertions.assertThrows(NullPointerException.class, () -> authorService.readAll());
    }

    @Test
    void controllersNPEHandler__catchNPEReturnErrorMessage() {
        Integer id = 5;
        String inputSurname = "-";
        String inputName = "-";

        Mockito.when(checkService.doCheck(Mockito.eq(null), Mockito.any())).thenReturn(Boolean.TRUE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputSurname), Mockito.any())).thenReturn(Boolean.FALSE);
        Mockito.when(checkService.doCheck(Mockito.eq(inputName), Mockito.any())).thenReturn(Boolean.FALSE);

        String expected = "Ошибка";
        String actual = authorShellController.create(inputSurname, inputName);
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

        String expected = "Новое поле добавлено, присвоен id - " + id;
        String actual = authorShellController.create(inputSurname, inputName);
        Assertions.assertEquals(expected, actual);
    }
}