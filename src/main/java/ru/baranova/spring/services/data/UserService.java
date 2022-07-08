package ru.baranova.spring.services.data;

import ru.baranova.spring.domain.User;

public interface UserService {
    User createUser();

    boolean isCorrectInput(String str);
}
