package ru.baranova.spring.service.print.visitor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.StopSearchConfig;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

@SpringBootTest(classes = {EntityPrintVisitorImplTestConfig.class, StopSearchConfig.class})
class EntityPrintVisitorImplGenreTest {
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
    void genre__print__correctOutput() {
        Genre testGenre = new Genre(1, "NameTest", "DescriptionTest");

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(testGenre), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = String.format("%d. %s - %s\r\n", testGenre.getId(), testGenre.getName(), testGenre.getDescription());
        entityPrintVisitorImpl.print(testGenre);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__print_NullId__incorrectNPE() {
        Genre testGenre = new Genre(null, "NameTest", "DescriptionTest");
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(testGenre), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testGenre));
    }

    @Test
    void genre__print_NullName__incorrectNPE() {
        Genre testGenre = new Genre(1, null, "DescriptionTest");
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(testGenre), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testGenre));
    }

    @Test
    void genre__print_NullDescription__correctOutput() {
        Genre testGenre = new Genre(1, "NameTest", "описание жанра пока отсутствует");

        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(testGenre), Mockito.any())).thenReturn(Boolean.TRUE);

        String expected = String.format("%d. %s - %s\r\n", testGenre.getId(), testGenre.getName(), testGenre.getDescription());

        testGenre.setDescription(null);
        entityPrintVisitorImpl.print(testGenre);
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__print_NullGenre__incorrectNPE() {
        Genre testGenre = null;
        Mockito.when(checkServiceImpl.doCheck(Mockito.eq(testGenre), Mockito.any())).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(NullPointerException.class, () -> entityPrintVisitorImpl.print(testGenre));
    }
}