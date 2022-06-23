package ru.baranova.spring.dao;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.domain.User;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test class UserDaoImpl")
@ContextConfiguration(classes = UserDaoImplTest.class)
public class UserDaoImplTest {

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct name")
    void shouldHaveCorrectNameObject () {
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        User user = userDaoImpl.getUser("Ekaterina", "Baranova");

        assertEquals("Ekaterina", user.getName());
    }

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct surname")
    void shouldHaveCorrectSurnameObject () {
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        User user = userDaoImpl.getUser("Ekaterina", "Baranova");

        assertEquals("Baranova", user.getSurname());
    }

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct object")
    void shouldHaveCorrectObject () {
        String name = "Ekaterina";
        String surname = "Baranova";

        UserDaoImpl userDaoImpl = new UserDaoImpl();
        User user = userDaoImpl.getUser(name, surname);

        assertAll("user",
                () -> assertEquals(name, user.getName()),
                () -> assertEquals(surname, user.getSurname())
        );
    }
}
