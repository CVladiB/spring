package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;

@Slf4j
@Setter
@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.user-service-impl")
public class UserServiceImpl implements UserService {
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;
    private final UserDao userDaoImpl;

    private String inputName;

    private String inputSurname;

    @Override
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

    @Override
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
