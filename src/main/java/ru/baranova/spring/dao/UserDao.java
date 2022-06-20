package ru.baranova.spring.dao;

import ru.baranova.spring.domain.User;

public interface UserDao {

    User getUser(String name, String surname);
}
