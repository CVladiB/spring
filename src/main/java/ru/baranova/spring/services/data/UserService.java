package ru.baranova.spring.services.data;

import ru.baranova.spring.domain.User;

public interface UserService {
    int getMinNameSurnameSymbol();

    void setMinNameSurnameSymbol(int min);

    User createUser();

    boolean isCorrectInput(String str);
}
