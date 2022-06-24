package ru.baranova.spring.dao;

import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.User;

@Component
public class UserDaoImpl implements UserDao {

    @Override
    public User getUser(String name, String surname) {
        if (name == null && surname == null || name.isEmpty() && surname.isEmpty()) {
            throw new NullPointerException();
        }
        char[] nameArr = name.toCharArray();
        char[] surnameArr = surname.toCharArray();
        for (int i = 0; i < nameArr.length; i++) {
            if (!Character.isLetter(nameArr[i])) {
                throw new NullPointerException();
            }
        }
        for (int i = 0; i < surnameArr.length; i++) {
            if (!Character.isLetter(surnameArr[i])) {
                throw new NullPointerException();
            }
        }
        return new User(name, surname);
    }
}
