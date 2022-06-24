package ru.baranova.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.QuestionDaoCsv;
import ru.baranova.spring.dao.UserDao;
import ru.baranova.spring.dao.UserDaoImpl;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.InputDaoReader;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.dao.reader.ReaderDaoFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfiguration
class TestServiceImplTestContext {
/*    @Autowired
    public InputStream systemInputStream;
    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    public OutputStream systemOutputStream;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private ReaderDao readerDaoFile;
    @Autowired
    private QuestionDao questionDaoCsv;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private QuestionService questionServiceImpl;
    @Autowired
    private UserDao userDaoImpl;
    @Autowired
    private TestService testServiceImpl;*/

    @Bean
    public InputStream systemInputStream () {
        return System.in;
    }
    @Bean
    public InputDao inputDaoReader(InputStream systemInputStream) {
        return new InputDaoReader(systemInputStream);
    }
    @Bean
    public OutputStream systemOutputStream(){
        return System.out;
    }
    @Bean
    public OutputDao outputDaoConsole(OutputStream systemOutputStream) {
        return new OutputDaoConsole(systemOutputStream);
    }
    // todo
    @Bean
    public ReaderDao readerDaoFile(ApplicationContext ac) {
        return new ReaderDaoFile(ac);
    }
    @Bean
    public QuestionDao questionDaoCsv(ReaderDao readerDaoFile) {
        return new QuestionDaoCsv(readerDaoFile);
    }
    @Bean
    public QuestionService questionServiceImpl(InputDao inputDaoReader, OutputDao outputDaoConsole) {
        return new QuestionServiceImpl(inputDaoReader, outputDaoConsole);
    }
    @Bean
    public UserDao userDaoImpl () {
        return new UserDaoImpl();
    }

    @Bean
    public UserService userServiceImpl(InputDao inputDaoReader, OutputDao outputDaoConsole, UserDao userDaoImpl) {
        return new UserServiceImpl(inputDaoReader, outputDaoConsole, userDaoImpl);
    }
    @Bean
    public TestService testServiceImpl(InputDao inputDaoReader, OutputDao outputDaoConsole, QuestionDao questionDaoCsv,
                                       UserService userServiceImpl, QuestionService questionServiceImpl) {
        return new TestServiceImpl(inputDaoReader, outputDaoConsole, questionDaoCsv, userServiceImpl, questionServiceImpl);
    }
}