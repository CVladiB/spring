package ru.baranova.spring.services.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.User;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
@ConfigurationProperties(prefix = "app.services.user-service-impl")
public class UserServiceImpl implements UserService {
    private final InputDao inputDaoReader;
    private final OutputService outputServiceConsole;
    private final UserDao userDaoImpl;
    private final CheckService checkServiceImpl;
    @Autowired
    private UserService self;
    @Setter
    @Getter
    private int minNameSurnameSymbol;

    @Override
    public User createUser() {
        String name;
        String surname;

        do {
            outputServiceConsole.getMessage("message.user-service-message.input-name");
            name = inputDaoReader.inputLine();
        } while (!self.isCorrectInput(name));
        do {
            outputServiceConsole.getMessage("message.user-service-message.input-surname");
            surname = inputDaoReader.inputLine();
        } while (!self.isCorrectInput(surname));

        return userDaoImpl.getUser(name, surname);
    }

    @Override
    public boolean isCorrectInput(String str) {
        boolean flag = false;
        if (checkServiceImpl.checkCorrectInputStr(str, minNameSurnameSymbol)) {
            Pattern pattern = Pattern.compile("[a-zA-Zа-яА-ЯёЁ\\-]+");
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        }
        return flag;
    }
}
