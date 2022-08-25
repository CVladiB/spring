package ru.baranova.spring.service.data.comment;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.repository.entity.CommentRepository;
import ru.baranova.spring.service.app.CheckService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.service.comment-service")
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
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

    @Transactional
    @Nullable
    @Override
    public Comment create(String author, String text) {
        Comment comment = null;
        if (checkService.doCheck(author, authorMinMaxFn) && checkService.doCheck(text, textMinMaxFn)) {
            comment = commentRepository.save(Comment.builder().author(author).text(text).build());
        }
        return comment;
    }

    @Transactional
    @Nullable
    @Override
    public Comment readById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Transactional
    @Nullable
    @Override
    public List<Comment> readByAuthorOfComment(String author) {
        return commentRepository.getByAuthorOfComment(author);
    }

    @Transactional
    @Nullable
    @Override
    public List<Comment> readAll() {
        return commentRepository.findAll();
    }

    @Transactional
    @Nullable
    @Override
    public Comment update(Integer id, String text) {
        Comment comment = null;
        Optional<Comment> commentById = commentRepository.findById(id);
        if (commentById.isPresent() && checkService.doCheck(text, textMinMaxFn)) {
            commentById.get().setText(text);
            comment = commentRepository.save(commentById.get());
        }
        return comment;
    }

    @Transactional
    @Override
    public Boolean delete(Integer id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        boolean isDelete = false;
        if (commentById.isPresent()) {
            commentRepository.delete(commentById.get());
            isDelete = true;
        }
        return isDelete;
    }
}
