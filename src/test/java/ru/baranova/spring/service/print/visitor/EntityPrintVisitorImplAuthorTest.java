package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.service.app.CheckService;


@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImplAuthorTest {
    @Autowired
    private EntityPrintVisitor entityPrintVisitorImpl;
    @Autowired
    private EntityPrintVisitorImplTestConfig config;
    @Autowired
    private CheckService checkServiceImpl;

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    void author__print__correctOutput() {
        Author testAuthor = new Author(1, "SurnameTest", "NameTest");
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testAuthor)).thenReturn(true);

        String expected = String.format("%d. %s %s\r\n", testAuthor.getId(), testAuthor.getSurname(), testAuthor.getName());
        entityPrintVisitorImpl.print(testAuthor);
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void author__print_NullId__incorrectNPE() {
        Author testAuthor = new Author(null, "SurnameTest", "NameTest");
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testAuthor)).thenReturn(false);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testAuthor));
    }

    @Test
    void author__print_NullSurname__incorrectNPE() {
        Author testAuthor = new Author(1, null, "NameTest");
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testAuthor)).thenReturn(false);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testAuthor));
    }

    @Test
    void author__print_NullName__incorrectNPE() {
        Author testAuthor = new Author(1, "SurnameTest", null);
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testAuthor)).thenReturn(false);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testAuthor));
    }

    @Test
    void author__print_NullAuthor__incorrectNPE() {
        Author testAuthor = null;
        Mockito.when(checkServiceImpl.isAllFieldsNotNull(testAuthor)).thenReturn(false);
        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testAuthor));
    }
}