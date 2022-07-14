package ru.baranova.spring.services.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.User;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

@DisplayName("Test class UserServiceImpl")
@SpringBootTest(classes = {UserServiceImpl.class})
class UserServiceImplTest {
    int minNameSurnameSymbol;
    @Autowired
    private UserService userServiceImpl;
    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private OutputService outputServiceConsole;
    @MockBean
    private UserDao userDaoImpl;
    @MockBean
    private CheckService checkServiceImpl;

    @BeforeEach
    void setUp() {
        userServiceImpl.setMinNameSurnameSymbol(3);
        minNameSurnameSymbol = userServiceImpl.getMinNameSurnameSymbol();
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, create new User")
    void shouldHaveNewUser() {
        Mockito.lenient().when(inputDaoReader.inputLine()).thenReturn("name").thenReturn("surname");
        Mockito.when(checkServiceImpl.checkCorrectInputStr("name", minNameSurnameSymbol)).thenReturn(true);
        Mockito.when(checkServiceImpl.checkCorrectInputStr("surname", minNameSurnameSymbol)).thenReturn(true);
        Mockito.when(userDaoImpl.getUser("name", "surname")).thenReturn(new User("name", "surname"));
        User expected = new User("name", "surname");
        User actual = userServiceImpl.createUser();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after null")
    void shouldHaveFalseInput_Null() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr(null, minNameSurnameSymbol)).thenReturn(false);
        boolean expected = userServiceImpl.isCorrectInput(null);
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after empty string")
    void shouldHaveFalseInput_EmptyString() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr("", minNameSurnameSymbol)).thenReturn(false);
        boolean expected = userServiceImpl.isCorrectInput("");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after short input")
    void shouldHaveFalseInput_ShortInput() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr("na", minNameSurnameSymbol)).thenReturn(false);
        boolean expected = userServiceImpl.isCorrectInput("na");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return true after short input")
    void shouldHaveTrueInput_ShortInput() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr("nam", minNameSurnameSymbol)).thenReturn(true);
        boolean expected = userServiceImpl.isCorrectInput("nam");
        Assertions.assertTrue(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return false after wrong symbol")
    void shouldHaveFalseInput_WrongSymbol() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr("nam/", minNameSurnameSymbol)).thenReturn(true);
        boolean expected = userServiceImpl.isCorrectInput("nam/");
        Assertions.assertFalse(expected);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method isCorrectInput, return true after correct symbol")
    void shouldHaveTrueInput_WrongSymbol() {
        Mockito.when(checkServiceImpl.checkCorrectInputStr("a-zA-Zа-яА-ЯёЁ", minNameSurnameSymbol)).thenReturn(true);
        boolean expected = userServiceImpl.isCorrectInput("a-zA-Zа-яА-ЯёЁ");
        Assertions.assertTrue(expected);
    }
}