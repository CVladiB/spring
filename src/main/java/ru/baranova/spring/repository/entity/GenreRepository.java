package ru.baranova.spring.repository.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import ru.baranova.spring.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Override
    <S extends Genre> S save(S entity);

    @Override
    Optional<Genre> findById(Integer integer);

    @Query("select g from Genre g where g.name = :name")
    List<Genre> findByName(@Param("name") @NonNull String name);

    @Override
    List<Genre> findAll();

    @Override
    void delete(Genre entity);

}
