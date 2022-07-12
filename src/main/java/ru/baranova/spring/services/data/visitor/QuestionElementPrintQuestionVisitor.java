package ru.baranova.spring.services.data.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.Option;
import ru.baranova.spring.domain.QuestionOneAnswer;
import ru.baranova.spring.domain.QuestionWithOptionAnswers;
import ru.baranova.spring.domain.QuestionWithoutAnswer;


@Component
@RequiredArgsConstructor
public class QuestionElementPrintQuestionVisitor implements QuestionElementVisitor<String> {

    @Override
    public String visit(QuestionWithOptionAnswers question) {
        StringBuilder sb = new StringBuilder();
        sb.append(question.getQuestion()).append("\n");
        int count = 0;
        for (Option option : question.getOptionAnswers()) {
            sb.append(++count).append(") ").append(option.getOption()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String visit(QuestionOneAnswer question) {
        StringBuilder sb = new StringBuilder();
        sb.append(question.getQuestion()).append("\n");
        return sb.toString();
    }

    @Override
    public String visit(QuestionWithoutAnswer question) {
        StringBuilder sb = new StringBuilder();
        sb.append(question.getQuestion()).append("\n");
        return sb.toString();
    }
}
