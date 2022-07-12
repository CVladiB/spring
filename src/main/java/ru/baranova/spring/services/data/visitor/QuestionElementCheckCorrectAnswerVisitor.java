package ru.baranova.spring.services.data.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;
import ru.baranova.spring.services.CheckService;

@Component
@RequiredArgsConstructor
public class QuestionElementCheckCorrectAnswerVisitor implements QuestionElementParameterVisitor<Boolean, String> {
    private final CheckService checkServiceImpl;

    @Override
    public Boolean visit(QuestionWithOptionAnswers question, String parameter) {
        boolean result = false;
        int min = 0;
        int max = question.getOptionAnswers().size();
        int indexInputAnswer = checkServiceImpl.checkCorrectInputNumber(parameter, min, max);
        if (indexInputAnswer != -1) {
            Option inputAnswerInOption = question.getOptionAnswers().get(indexInputAnswer - 1);
            result = inputAnswerInOption.getOption().equals(question.getRightAnswer().getAnswer());
        }
        return result;
    }

    @Override
    public Boolean visit(QuestionOneAnswer question, String parameter) {
        return parameter.equalsIgnoreCase(question.getRightAnswer().getAnswer());
    }

    @Override
    public Boolean visit(QuestionWithoutAnswer question, String parameter) {
        return true;
    }
}
