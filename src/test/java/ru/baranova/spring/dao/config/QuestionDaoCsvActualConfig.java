package ru.baranova.spring.dao.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.LocaleProvider;
import ru.baranova.spring.dao.QuestionDaoCsv;
import ru.baranova.spring.dao.reader.ReaderDao;
import ru.baranova.spring.services.message.LocaleService;

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "question-dao-csv")
public class QuestionDaoCsvActualConfig {
    private String initialStringQuestionWithOptionAnswers;
    private String initialStringQuestionOneAnswer;
    private String initialStringQuestionWithoutAnswer;
    private String question;
    private String rightAnswer;
    private String optionOne;
    private String optionTwo;

    @MockBean
    private LocaleService localeServiceImpl;

    @MockBean
    private ReaderDao readerDaoFile;
    @MockBean
    private LocaleProvider localeProviderImpl;

    @Bean
    public QuestionDaoCsv questionDaoCsv(LocaleService localeServiceImpl, ReaderDao readerDaoFile, LocaleProvider localeProviderImpl) {
        return new QuestionDaoCsv(readerDaoFile, localeProviderImpl, localeServiceImpl);
    }
}
