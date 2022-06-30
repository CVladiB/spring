package ru.baranova.spring.dao;

import org.springframework.lang.NonNull;
import ru.baranova.spring.domain.User;

public interface UserDao {

    User getUser(@NonNull String name, @NonNull String surname);
}
