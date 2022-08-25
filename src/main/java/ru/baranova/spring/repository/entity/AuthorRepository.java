package ru.baranova.spring.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.baranova.spring.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Override
    <S extends Author> S save(S entity);

    @Override
    Optional<Author> findById(Integer integer);

    @Query("select a from Author a " +
            "where  ( 1 = 1 " +
            "and :surname is not null and :name is not null and a.surname = :surname and a.name = :name) " +
            "or (1 = 1 and ( :surname is null or :name is null) and (a.surname = :surname or a.name = :name))")
    List<Author> findBySurnameAndName(@Param("surname") String surname, @Param("name") String name);

    @Override
    List<Author> findAll();

    @Override
    void delete(Author entity);

}
