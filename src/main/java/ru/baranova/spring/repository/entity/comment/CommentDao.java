package ru.baranova.spring.repository.entity.comment;

import ru.baranova.spring.model.Comment;

import java.util.List;

public interface CommentDao {
    Comment create(String author, String text);

    Comment getById(Integer id);

    List<Comment> getByAuthorOfComment(String author);

    List<Comment> getAll();

    Comment update(Integer id, String text);

    Boolean delete(Integer id);
}
