package ru.baranova.spring.services.data;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.baranova.spring.config.ComponentScanStopConfig;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.services.data.config.QuestionServiceImplTestConfig;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

@Slf4j
@DisplayName("Test class QuestionServiceImpl")
@ActiveProfiles("question-service-impl")
@SpringBootTest(classes = {QuestionServiceImplTestConfig.class, ComponentScanStopConfig.class})
class QuestionServiceImplQuestionOneAnswerTest {
    @Autowired
    private QuestionServiceImplTestConfig config;
    @Autowired
    private QuestionService questionServiceImplString;
    @Autowired
    private QuestionElementPrintQuestionVisitor questionElementPrintQuestionVisitorMock;
    @Autowired
    private QuestionElementSetAndGetAnswerVisitor questionElementSetAndGetAnswerVisitorMock;
    @Autowired
    private QuestionElementCheckCorrectAnswerVisitor questionElementCheckCorrectAnswerVisitorMock;

    private Question questionOneAnswer;

}

