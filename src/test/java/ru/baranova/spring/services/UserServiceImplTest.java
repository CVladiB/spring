package ru.baranova.spring.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDaoReader;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.domain.User;

@DisplayName("Test class UserServiceImpl")
//@Import(UserServiceTestConfig.class) //~ =@ContextConfiguration(classes = {UserServiceTestConfig.class})
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
class UserServiceImplTest {

    @Mock
    private InputDaoReader inputDaoReader;
    @Mock
    private UserDao userDaoImpl;
    @Mock
    private OutputDaoConsole outputDaoConsole;

    private User user;

    private UserService userServiceImpl;

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl(inputDaoReader,outputDaoConsole,userDaoImpl);
        user = new User("name", "surname");
    }

    @Test
   // todo fix new User (now return null)
    void createUser() {
        Mockito.when(inputDaoReader.inputLine()).thenReturn("name");
        Mockito.when(inputDaoReader.inputLine()).thenReturn("surname");
        OutputDaoConsole mock = Mockito.mock(OutputDaoConsole.class);
        Mockito.lenient().when(userDaoImpl.getUser("name", "surname")).thenReturn(user);
        User expected = userServiceImpl.createUser();
        User actual = new User("name", "surname");
        Assertions.assertEquals(expected, actual);
    }
}