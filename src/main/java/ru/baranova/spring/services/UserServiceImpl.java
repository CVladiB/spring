package ru.baranova.spring.services;

import lombok.RequiredArgsConstructor;
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
        outputDaoConsole.outputLine(inputName);
        String name = inputDaoReader.inputLine();
        outputDaoConsole.outputLine(inputSurname);
        String surname = inputDaoReader.inputLine();

        return userDaoImpl.getUser(name, surname);
    }
}
