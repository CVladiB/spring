package ru.baranova.spring.dao;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.User;

@Component
public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(@NonNull String name, @NonNull String surname) {
        return new User(name, surname);
    }
}
