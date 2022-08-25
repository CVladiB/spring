package ru.baranova.spring.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.baranova.spring.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Override
    <S extends Comment> S save(S entity);

    @Override
    Optional<Comment> findById(Integer integer);

    @Query("select c from Comment c where c.author = :author")
    List<Comment> getByAuthorOfComment(@Param("author") @NonNull String author);

    @Override
    List<Comment> findAll();

    @Override
    void delete(Comment entity);
}
