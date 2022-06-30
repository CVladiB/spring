package ru.baranova.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test class UserDaoImpl")
@ExtendWith(value = {SpringExtension.class})
@ContextConfiguration(classes = {UserDaoImpl.class})
public class UserDaoImplTest {
    // Проверка на null требуется не в DAO, а в сервисе.
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
