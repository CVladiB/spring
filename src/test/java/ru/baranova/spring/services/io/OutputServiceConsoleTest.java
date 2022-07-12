package ru.baranova.spring.services.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.services.io.config.OutputServiceConsoleTestConfig;

@DisplayName("Test class OutputServiceConsole")
@SpringBootTest(classes = {OutputServiceConsoleTestConfig.class, ComponentScanStopConfig.class})
class OutputServiceConsoleTest {

    @Autowired
    private OutputServiceConsoleTestConfig config;
    @Autowired
    private OutputService outputServiceConsoleString;

    @AfterEach
    void tearDown() {
        config.getOut().reset();
    }

    @Test
    @DisplayName("Test class OutputServiceConsole, method getMessage(String keyMessage)")
    void shouldHaveCorrectMessage() {
        String expected = "Тестовое сообщение";

        outputServiceConsoleString.getMessage("message");
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected + "\r\n", actual);
    }

    @Test
    @DisplayName("Test class OutputServiceConsole, method getMessage(String keyMessage, Object... args)")
    void shouldHaveCorrectMessage2() {
        String expected = "Тестовое второе сообщение";

        outputServiceConsoleString.getMessage("message-two", "второе");
        String actual = config.getOut().toString();

        Assertions.assertEquals(expected + "\r\n", actual);
    }

}