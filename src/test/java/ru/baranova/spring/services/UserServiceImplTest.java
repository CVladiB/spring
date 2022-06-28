package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test class UserServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:testApplication.properties")
@ContextConfiguration(classes = {UserServiceImpl.class, UserServiceImplTestConfig.class})
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImplTest userServiceImplTest;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private UserDao userDaoImpl;
    @Captor
    ArgumentCaptor<String> consoleArgumentCaptor;
    @Captor
    ArgumentCaptor<String> consoleArgumentCaptor2;

    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, create new User")
    void shouldHaveNewUser() {
        Mockito.when(inputDaoReader.inputLine())
                .thenReturn("name")
                .thenReturn("surname");
        Mockito.when(userDaoImpl.getUser("name", "surname")).thenReturn(new User("name", "surname"));
        User expected = new User("name", "surname");
        User actual = userServiceImpl.createUser();
        Assertions.assertEquals(expected, actual);
    }

    @Disabled
    // todo Пофиксить, непонятная ошибка
    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, message before creating new User")
    void shouldHaveMessageBeforeInputUser() {
        userServiceImpl.createUser();
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor.capture());
        Mockito.when(inputDaoReader.inputLine()).thenReturn("name\n");
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor2.capture());
        Mockito.when(inputDaoReader.inputLine()).thenReturn("surname\n");
        Assertions.assertAll(
                () -> assertEquals("Input your name ", consoleArgumentCaptor.getValue()),
                () -> assertEquals("Input your surname ", consoleArgumentCaptor2.getValue())
        );
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, return false after wrong number")
    void shouldHaveFalseInputUser_WrongSymbol() {
        String text = "nam0e";
        boolean expected = userServiceImpl.isCorrectInput(text);
        boolean actual = true;
        if (text == null || text.isEmpty()) {
            actual = false;
        }
        char[] strArr = text.toCharArray();
        for (int i = 0; i < strArr.length; i++) {
            if (!Character.isLetter(strArr[i])) {
                actual = false;
            }
        }
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class UserServiceImpl, method createUser, return false after null")
    void shouldHaveFalseInputUser_Null() {
        String text = "";
        boolean expected = userServiceImpl.isCorrectInput(text);
        boolean actual = true;
        if (text == null || text.isEmpty()) {
            actual = false;
        }
        char[] strArr = text.toCharArray();
        for (int i = 0; i < strArr.length; i++) {
            if (!Character.isLetter(strArr[i])) {
                actual = false;
            }
        }
        Assertions.assertEquals(expected, actual);
    }
}

@TestConfiguration
class UserServiceImplTestConfig {
    @Bean
    public InputDao getInputDaoReader() {
        return Mockito.mock(InputDao.class);
    }
    @Bean
    public OutputDao getOutputDaoConsole() {
        return Mockito.mock(OutputDao.class);
    }
    @Bean
    public UserDao getUserDaoImpl() {
        return Mockito.mock(UserDao.class);
    }
}