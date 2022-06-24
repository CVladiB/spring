package ru.baranova.spring.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test class TestServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:testApplication.properties")
@ContextConfiguration(classes = {TestServiceImpl.class, TestServiceImplTestConfig.class})
class TestServiceImplTest {
    @Autowired
    private TestService testServiceImpl;

    @Autowired
    private InputDao inputDaoReader;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private QuestionDao questionDaoCsv;
    @Autowired
    private UserService userServiceImpl;
    @Autowired
    private QuestionService questionServiceImpl;


    @Test
    void testMethod() {
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(Collections.emptyList());
        ArgumentCaptor<String> consoleArgumentCaptor = ArgumentCaptor.forClass(String.class);
        testServiceImpl.test();
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor.capture());
        assertEquals("Hi", consoleArgumentCaptor.getValue());
    }
}