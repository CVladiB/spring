package ru.baranova.spring.services;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.OutputDao;

@TestConfiguration
class TestServiceImplTestConfig {
    @Bean
    public OutputDao getOutputDaoConsole() {
        return Mockito.mock(OutputDao.class);
    }

    @Bean
    public QuestionDao getQuestionDaoCsv() {
        return Mockito.mock(QuestionDao.class);
    }

    @Bean
    public UserService getUserServiceImpl() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public QuestionService getQuestionServiceImpl() {
        return Mockito.mock(QuestionService.class);
    }
}