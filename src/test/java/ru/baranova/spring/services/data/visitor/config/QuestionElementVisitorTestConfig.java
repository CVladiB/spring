package ru.baranova.spring.services.data.visitor.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

@Slf4j
@Getter
@Setter
@TestConfiguration
@ComponentScan("ru.baranova.spring.services.data.visitor")
@ConfigurationProperties(prefix = "question-visitor-impl")
public class QuestionElementVisitorTestConfig {
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;
    private int minAnswerSymbol;

    @MockBean
    private InputDao inputDaoReader;
    @MockBean
    private OutputService outputServiceConsole;
    @MockBean
    private CheckService checkServiceImpl;

}
