package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;

import java.util.Collections;

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

    @DisplayName("Test class UserServiceImpl create new User")
    @Test
    void createNewUser() {
        Mockito.when(inputDaoReader.inputLine())
                .thenReturn("name")
                .thenReturn("surname");
        Mockito.when(userDaoImpl.getUser("name", "surname")).thenReturn(new User("name", "surname"));
        User expected = new User("name", "surname");
        User actual = userServiceImpl.createUser();
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Test class UserServiceImpl output info")
    @Test
    void createUserMessage() {
        ArgumentCaptor<String> consoleArgumentCaptor = ArgumentCaptor.forClass(String.class);
        userServiceImpl.createUser();
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor.capture());
        Mockito.when(inputDaoReader.inputLine()).thenReturn("name");
        //Mockito.when(outputDaoConsole).then();
        Mockito.when(inputDaoReader.inputLine()).thenReturn("surname");
        assertEquals("Hi", consoleArgumentCaptor.getValue());
    }
}