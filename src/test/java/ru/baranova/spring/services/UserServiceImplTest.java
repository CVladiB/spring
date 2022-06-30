package ru.baranova.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;

@DisplayName("Test class UserServiceImpl")
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private UserService userServiceImpl;
    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private OutputDao outputDaoConsole;
    @MockBean
    private UserDao userDaoImpl;

    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, create new User")
    void shouldHaveNewUser() {
        Mockito.when(inputDaoReader.inputLine()).thenReturn("name").thenReturn("surname");
        Mockito.when(userDaoImpl.getUser("name", "surname")).thenReturn(new User("name", "surname"));
        User expected = new User("name", "surname");
        User actual = userServiceImpl.createUser();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after null")
    void shouldHaveFalseInput_Null() {
        boolean expected = userServiceImpl.isCorrectInput(null);
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after empty string")
    void shouldHaveFalseInput_EmptyString() {
        boolean expected = userServiceImpl.isCorrectInput("");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after short input")
    void shouldHaveFalseInput_ShortInput() {
        boolean expected = userServiceImpl.isCorrectInput("na");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return true after short input")
    void shouldHaveTrueInput_ShortInput() {
        boolean expected = userServiceImpl.isCorrectInput("nam");
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after wrong symbol")
    void shouldHaveFalseInput_WrongSymbol() {
        boolean expected = userServiceImpl.isCorrectInput("nam/");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return true after correct symbol")
    void shouldHaveTrueInput_WrongSymbol() {
        boolean expected = userServiceImpl.isCorrectInput("a-fzA-FZа-фяА-ФЯёЁ");
        Assertions.assertTrue(expected);
    }
}