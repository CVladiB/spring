package ru.baranova.spring.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import ru.baranova.spring.domain.User;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test class UserDaoImpl")
@ContextConfiguration(classes = {UserDaoImpl.class, UserDaoImplTest.class})
public class UserDaoImplTest {
    @Autowired
    private UserDaoImpl userDaoImpl;
    // todo
    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return null")
    void shouldHaveCorrectNameObject () {
        Exception thrown = Assertions.assertThrows(NullPointerException.class, () -> userDaoImpl.getUser(null,null));
        Assertions.assertEquals("Cannot invoke \"ru.baranova.spring.dao.UserDaoImpl.getUser(String, String)\" because \"this.userDaoImpl\" is null", thrown.getMessage());
        //Assertions.assertTrue(thrown.getMessage().contains("NullPointerException"));
    }

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct object")
    void shouldHaveCorrectObject () {
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        User user = userDaoImpl.getUser("Ekaterina", "Baranova");
        assertEquals(new User("Ekaterina", "Baranova"), user);

    }

    @Test
    @DisplayName("Test class UserDaoImpl, method getUser, return correct name/surname")
    void shouldHaveCorrectNameAndSurnameObject () {
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
