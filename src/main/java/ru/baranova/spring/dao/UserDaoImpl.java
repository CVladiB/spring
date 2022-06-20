package ru.baranova.spring.dao;

import lombok.Data;
import ru.baranova.spring.domain.User;

@Data
public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(String name, String surname) {
        return new User(name, surname);
    }
}
