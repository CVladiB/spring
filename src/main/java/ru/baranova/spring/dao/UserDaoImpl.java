package ru.baranova.spring.dao;

import ru.baranova.spring.domain.User;

public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(String name, String surname) {
        return new User(name, surname);
    }
}
