package ru.baranova.spring.services.shell;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.StaticApplicationContext;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.services.setting.AppSettingService;
import ru.baranova.spring.services.shell.config.ShellServiceImplTestConfig;
import ru.baranova.spring.services.test.TestService;

@DisplayName("Test class ShellServiceImpl")
@SpringBootTest(classes = {ShellServiceImplTestConfig.class, ComponentScanStopConfig.class})
class ShellServiceImplTest {
    @Autowired
    private AppSettingService appSettingServiceImpl;
    @Autowired
    private TestService testServiceImpl;
    @Autowired
    private ShellService shellServiceImpl;
    @Autowired
    private ShellServiceImplTestConfig config;

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    @DisplayName("Test class ShellServiceImpl, method chooseLanguage, correct output")
    void shouldHaveCorrectChooseLanguage() {
        String expected = """
                Выберите язык:\r
                Введи 1 для продолжения на русском\r
                Input 2 to select English\r
                :>\s\r
                """;

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("""
                    Выберите язык:\r
                    Введи 1 для продолжения на русском\r
                    Input 2 to select English\r
                    :>\s""");
            return null;
        }).when(appSettingServiceImpl).chooseLanguage();

        shellServiceImpl.chooseLanguage();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class ShellServiceImpl, method test, correct output")
    void shouldHaveCorrectTest() {
        String expected = """
                Добро пожаловать на тестирвоание интеллекта!\r
                
                Question First\r
                
                Ты очень умен, ты ответил на 1 вопросов из 1\r
                
                
                name surname, надеюсь, ты не обижаешься на нас:)\r
                """;

        Mockito.doAnswer(invocation -> {
            config.getWriter().println("""
                    Добро пожаловать на тестирвоание интеллекта!\r
                    
                    Question First\r
                    
                    Ты очень умен, ты ответил на 1 вопросов из 1\r
                    
                    
                    name surname, надеюсь, ты не обижаешься на нас:)""");
            return null;
        }).when(testServiceImpl).test();

        shellServiceImpl.test();
        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class ShellServiceImpl, method test, correct return")
    void shouldHaveCorrectReturn() {
        String expected = "Smth input";
        String actual = shellServiceImpl.echo(expected);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test class ShellServiceImpl, method test, correct output")
    void shouldHaveCorrectExit() {
        shellServiceImpl.setContext(new StaticApplicationContext());
        int expected = 0;
        int actual = shellServiceImpl.stopApplication();
        Assertions.assertEquals(expected, actual);
    }
}