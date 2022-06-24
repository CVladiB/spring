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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.util.ReflectionTestUtils;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test class TestServiceImpl")
@ExtendWith(value = {MockitoExtension.class, SpringExtension.class})
@TestPropertySource("classpath:testApplication.properties")
@TestConfiguration("TestServiceImplTestContext.class")
class TestServiceImplTest {
    @Autowired
    private TestService testServiceImpl;

    @Autowired
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
    }

    @Test
    void testMethod() {
        Mockito.when(questionDaoCsv.loadQuestion()).thenReturn(Collections.emptyList());
        ArgumentCaptor<String> consoleArgumentCaptor = ArgumentCaptor.forClass(String.class);
        testServiceImpl.test();
        Mockito.verify(outputDaoConsole).outputLine(consoleArgumentCaptor.capture());
        assertEquals("Hi", consoleArgumentCaptor.getValue());


    }

    @Configuration
    public static class ContextConfiguration {
    }
}