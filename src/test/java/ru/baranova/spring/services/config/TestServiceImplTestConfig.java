package ru.baranova.spring.services.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.QuestionDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.dao.io.OutputDaoConsole;
import ru.baranova.spring.services.QuestionService;
import ru.baranova.spring.services.TestService;
import ru.baranova.spring.services.TestServiceImpl;
import ru.baranova.spring.services.UserService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "app.services.test-service-impl")
public class TestServiceImplTestConfig {
    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;

    @Bean
    public OutputDao outputDaoConsoleString() {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new OutputDaoConsole(out);
    }

    @Bean
    public TestService testServiceImplString(OutputDao outputDaoConsoleString
            , QuestionDao questionDaoCsv
            , UserService userServiceImpl
            , QuestionService questionServiceImpl) {
        return new TestServiceImpl(outputDaoConsoleString, questionDaoCsv, userServiceImpl, questionServiceImpl);
    }


//    @Bean
//    public QuestionService questionServiceImplString(InputDao inputDaoReader
//            , OutputDao outputDaoConsoleString){
//        return new QuestionServiceImpl(inputDaoReader,outputDaoConsoleString);
//    }

}
