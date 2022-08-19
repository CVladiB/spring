package ru.baranova.spring.service.data.comment;

import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.BaseService;

import java.util.List;

public interface CommentService extends BaseService {

    Comment create(String author, String text);

    Comment readById(Integer id);

    List<Comment> readByAuthorOfComment(String author);

    List<Comment> readAll();

    Comment update(Integer id, String text);

    Boolean delete(Integer id);

}
