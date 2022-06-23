package ru.baranova.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test class TestServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@PropertySource("classpath:testApplication.properties")
class TestServiceImplTest {
    private TestServiceImpl testServiceImpl;
    @Mock
    private InputDao inputDaoReader;
    @Mock
    private OutputDao outputDaoConsole;
    @Mock
    private QuestionDao questionDaoCsv;
    @Mock
    private UserService userServiceImpl;
    @Mock
    private QuestionService questionServiceImpl;

    @BeforeEach
    void setUp() {
        testServiceImpl = new TestServiceImpl(inputDaoReader, outputDaoConsole, questionDaoCsv, userServiceImpl, questionServiceImpl);
    }

    @Test
    void test() {

    }
}