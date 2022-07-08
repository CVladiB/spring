package ru.baranova.spring.services.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.services.LocaleService;

@SpringBootTest(classes = {OutputServiceConsoleTestConfig.class, OutputServiceConsole.class})
class OutputServiceConsoleTest {
    private final String expected = "This is the source of my output stream";
    @Autowired
    private OutputServiceConsole OutputServiceConsole;
    @Autowired
    private OutputDao outputDaoConsole;
    @Autowired
    private LocaleService localeServiceImpl;

    @Autowired
    private OutputServiceConsoleTestConfig config;

    @Test
    void getMessage() {
        Mockito.when(localeServiceImpl.getMessage("str")).thenReturn(expected);

        Mockito.doAnswer(invocation -> {
            config.getWriter().println(expected);
            return null;
        }).when(outputDaoConsole).outputLine(expected);

        String actual = config.getOut().toString();
        Assertions.assertEquals(expected, actual);
    }

}