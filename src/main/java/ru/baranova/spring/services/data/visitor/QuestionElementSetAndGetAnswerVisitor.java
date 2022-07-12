package ru.baranova.spring.services.data.visitor;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.CheckService;
import ru.baranova.spring.services.io.OutputService;

@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.services.question-service-impl")
public class QuestionElementSetAndGetAnswerVisitor implements QuestionElementVisitor<String> {
    private final InputDao inputDaoReader;
    private final OutputService outputServiceConsole;
    private final CheckService checkServiceImpl;
    @Setter
    private int minAnswerSymbol;

    @Override
    public String visit(QuestionWithOptionAnswers question) {
        String answer;
        int indexInputAnswer;
        do {
            outputServiceConsole.getMessage("message.question-service-message.input-number-answer");
            minAnswerSymbol = 0;
            int maxAnswerSymbol = question.getOptionAnswers().size();
            answer = inputDaoReader.inputLine();
            indexInputAnswer = checkServiceImpl.checkCorrectInputNumber(answer, minAnswerSymbol, maxAnswerSymbol);
        } while (indexInputAnswer == -1);
        return answer;
    }

    @Override
    public String visit(QuestionOneAnswer question) {
        String answer;
        boolean flag;
        do {
            outputServiceConsole.getMessage("message.question-service-message.input-answer");
            answer = inputDaoReader.inputLine();
            flag = checkServiceImpl.checkCorrectInputStr(answer, minAnswerSymbol);
        } while (!flag);
        return answer;
    }

    @Override
    public String visit(QuestionWithoutAnswer question) {
        String answer;
        boolean flag;
        do {
            outputServiceConsole.getMessage("message.question-service-message.input-answer");
            answer = inputDaoReader.inputLine();
            flag = checkServiceImpl.checkCorrectInputStr(answer, minAnswerSymbol);
        } while (!flag);
        return answer;
    }
}
