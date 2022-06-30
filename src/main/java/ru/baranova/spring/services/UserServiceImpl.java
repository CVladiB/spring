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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        do {
            outputDaoConsole.outputFormatLine(inputName);
            name = inputDaoReader.inputLine();
        } while (!isCorrectInput(name));
        do {
            outputDaoConsole.outputFormatLine(inputSurname);
            surname = inputDaoReader.inputLine();
        } while (!isCorrectInput(surname));

        return userDaoImpl.getUser(name, surname);
    }

    @Override
    public boolean isCorrectInput(String str) {
        if (str == null || str.isEmpty()) {
            log.info("Упс! Ничего не введено");
            return false;
        }
        if (str.length() < 3) {
            log.info("Упс! Слишком короткий ввод");
            return false;
        }

        Pattern pattern = Pattern.compile("[a-zA-Zа-яА-ЯёЁ\\-]+");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
