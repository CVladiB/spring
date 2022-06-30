package ru.baranova.spring.dao;

import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.User;

@Component
public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(String name, String surname) {
        return new User(name, surname);
    }
}
