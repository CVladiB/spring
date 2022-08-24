package ru.baranova.spring.service.data.comment;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.dao.entity.comment.CommentDao;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.comment-service")
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final CheckService checkService;
    @Setter
    private int minInput;
    @Setter
    private int maxInputAuthor;
    @Setter
    private int maxInputText;
    private Function<String, List<String>> authorMinMaxFn;
    private Function<String, List<String>> textMinMaxFn;

    @PostConstruct
    private void initFunction() {
        BiFunction<Integer, Integer, Function<String, List<String>>> correctInputStrFn
                = (minValue, maxValue) -> str -> checkService.checkCorrectInputStrLength(str, minValue, maxValue);
        authorMinMaxFn = correctInputStrFn.apply(minInput, maxInputAuthor);
        textMinMaxFn = correctInputStrFn.apply(minInput, maxInputText);
    }

    @Nullable
    @Override
    public Comment create(String author, String text) {
        Comment comment = null;
        if (checkService.doCheck(author, authorMinMaxFn) && checkService.doCheck(text, textMinMaxFn)) {
            comment = commentDao.create(author, text);
        }
        return comment;
    }

    @Nullable
    @Override
    public Comment readById(Integer id) {
        return commentDao.getById(id);
    }

    @Nullable
    @Override
    public List<Comment> readByAuthorOfComment(String author) {
        return commentDao.getByAuthorOfComment(author);
    }

    @Nullable
    @Override
    public List<Comment> readAll() {
        return commentDao.getAll();
    }

    @Nullable
    @Override
    public Comment update(Integer id, String text) {
        Comment comment = null;
        if (checkService.doCheck(text, textMinMaxFn)) {
            comment = commentDao.update(id, text);
        }
        return comment;
    }

    @Override
    public Boolean delete(Integer id) {
        return checkService.doCheck(commentDao.getById(id), checkService::checkExist) && commentDao.delete(id);
    }
}
