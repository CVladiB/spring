package ru.baranova.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test class UserDaoImpl")
@SpringBootTest(classes = {UserDaoImpl.class, ComponentScanStopConfig.class})
public class UserDaoImplTest {
    @Autowired
    private UserDaoImpl userDaoImpl;


    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct object")
    void shouldHaveCorrectObject() {
        User user = userDaoImpl.getUser("name", "surname");
        Assertions.assertEquals(new User("name", "surname"), user);

    }

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct name/surname")
    void shouldHaveCorrectNameAndSurnameObject() {
        User user = userDaoImpl.getUser("name", "surname");

        Assertions.assertAll("user",
                () -> assertEquals("name", user.getName()),
                () -> assertEquals("surname", user.getSurname())
        );
    }
}
