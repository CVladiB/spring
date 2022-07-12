package ru.baranova.spring.services.data;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Question;
import ru.baranova.spring.services.data.visitor.QuestionElementCheckCorrectAnswerVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementPrintQuestionVisitor;
import ru.baranova.spring.services.data.visitor.QuestionElementSetAndGetAnswerVisitor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final OutputDao outputDaoConsole;
    @Autowired
    private ApplicationContext context;

    @Override
    public void printQuestion(@NonNull Question question) {
        QuestionElementPrintQuestionVisitor visitor = context.getBean(QuestionElementPrintQuestionVisitor.class);
        String result = question.accept(visitor);
        outputDaoConsole.outputLine(result);
    }

    @Override
    public String setAndGetAnswer(@NonNull Question question) {
        QuestionElementSetAndGetAnswerVisitor visitor = context.getBean(QuestionElementSetAndGetAnswerVisitor.class);
        return question.accept(visitor);
    }

    @Override
    public boolean checkCorrectAnswer(@NonNull Question question, @NonNull String inputAnswer) {
        QuestionElementCheckCorrectAnswerVisitor visitor = context.getBean(QuestionElementCheckCorrectAnswerVisitor.class);
        return question.accept(visitor, inputAnswer);
    }
}
