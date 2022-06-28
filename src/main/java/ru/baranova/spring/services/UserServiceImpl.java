package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private final UserDao userDaoImpl;
    @Value("${app.bean.userServiceImpl.name}")
    private String inputName;
    @Value("${app.bean.userServiceImpl.surname}")
    private String inputSurname;

    public User createUser() {
        String name;
        String surname;
        boolean flag = true;
        do {
            outputDaoConsole.outputLine(inputName);
            name = inputDaoReader.inputLine();
            flag = isCorrectInput(name);
        } while (!flag);
        do {
            outputDaoConsole.outputLine(inputSurname);
            surname = inputDaoReader.inputLine();
            flag = isCorrectInput(surname);
        } while (!flag);

        return userDaoImpl.getUser(name, surname);
    }

    public boolean isCorrectInput(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        char[] strArr = str.toCharArray();
        for (int i = 0; i < strArr.length; i++) {
            if (!Character.isLetter(strArr[i])) {
                return false;
            }
        }
        return true;
    }
}
