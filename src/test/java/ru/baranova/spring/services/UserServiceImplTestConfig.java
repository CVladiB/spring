package ru.baranova.spring.services;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.User;

@TestConfiguration
class UserServiceImplTestConfig {
    @Bean
    public InputDao getInputDaoReader() {
        return Mockito.mock(InputDao.class);
    }
    @Bean
    public OutputDao getOutputDaoConsole() {
        return Mockito.mock(OutputDao.class);
    }
    @Bean
    public UserDao getUserDaoImpl() {
        return Mockito.mock(UserDao.class);
    }
}