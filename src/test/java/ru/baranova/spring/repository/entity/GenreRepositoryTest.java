package ru.baranova.spring.repository.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.baranova.spring.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class GenreRepositoryTest {
    @Autowired
    private GenreRepository genreRepository;
    private Genre insertGenre1;
    private Genre insertGenre2;
    private Genre testGenre;
    private List<Genre> genreList;

    @BeforeEach
    void setUp() {
        insertGenre1 = new Genre(1, "Name1", "Description1");
        insertGenre2 = new Genre(2, "Name2", "Description2");
        testGenre = new Genre(null, "NameTest", "DescriptionTest");
        genreList = List.of(insertGenre1, insertGenre2);
    }

    @Test
    void genre__create__correctReturnNewGenre() {
        List<Integer> listExistId = genreRepository.findAll()
                .stream()
                .map(Genre::getId)
                .toList();

        Genre expected = testGenre;
        Genre actual = genreRepository.save(testGenre);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__create_NullName__incorrectException() {
        testGenre.setName(null);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreRepository.save(testGenre));
    }

    @Test
    void genre__create_DuplicateName__incorrectException() {
        insertGenre1.setId(null);
        Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> genreRepository.save(insertGenre1));
    }

    @Test
    void genre__create_NullDescription_correctReturnNewGenre() {
        List<Integer> listExistId = genreRepository.findAll().stream().map(Genre::getId).toList();

        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreRepository.save(testGenre);

        Assertions.assertFalse(listExistId.contains(actual.getId()));
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void genre__findById__correctReturnGenreById() {
        Integer id = insertGenre1.getId();
        Genre expected = insertGenre1;
        Genre actual = genreRepository.findById(id).get();
        Assertions.assertAll(
                () -> Assertions.assertEquals(expected.getName(), actual.getName()),
                () -> Assertions.assertEquals(expected.getDescription(), actual.getDescription()),
                () -> Assertions.assertEquals(expected.getId(), actual.getId())
        );
    }

    @Test
    void genre__findById_NonexistentId__incorrectException() {
        Integer nonexistentId = 100;
        Assertions.assertEquals(Optional.empty(), genreRepository.findById(nonexistentId));
    }

    @Test
    void genre__getByName__correctReturnGenreByName() {
        List<Genre> expected = List.of(insertGenre1);
        List<Genre> actual = genreRepository.findByName(insertGenre1.getName());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getByName_NonexistentName__incorrectException() {
        String nonexistentName = "Name25";
        Assertions.assertEquals(Collections.emptyList(), genreRepository.findByName(nonexistentName));
    }

    @Test
    void genre__getByName_NullName__emptyListResult() {
        Assertions.assertEquals(Collections.emptyList(), genreRepository.findByName(null));
    }

    @Test
    void genre__getAll__returnListGenres() {
        List<Genre> expected = genreList;
        List<Genre> actual = genreRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__getAll_Empty_emptyListResultException() {
        genreRepository.deleteAll(genreList);
        Assertions.assertEquals(Collections.emptyList(), genreRepository.findAll());
    }

    @Test
    void genre__update__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        Genre expected = testGenre;
        Genre actual = genreRepository.save(testGenre);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NonexistentId__incorrectException() {
        testGenre.setId(100);
        Genre actual = genreRepository.save(testGenre);

        testGenre.setId(genreList.size() + 1);
        Genre expected = testGenre;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NullName__incorrectException() {
        testGenre.setId(insertGenre1.getId());
        testGenre.setName(null);
        Genre expected = testGenre;
        Genre actual = genreRepository.save(testGenre);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__update_NullDescription__correctChangeAllFieldGenreById() {
        Integer id = insertGenre1.getId();
        testGenre.setId(id);
        testGenre.setDescription(null);
        Genre expected = testGenre;
        Genre actual = genreRepository.save(testGenre);
        Assertions.assertEquals(expected, actual);
    }


    @Test
    void genre__delete__correctDelete() {
        List<Genre> actualBeforeDelete = genreRepository.findAll();
        Assertions.assertNotNull(actualBeforeDelete);
        genreRepository.delete(insertGenre1);
        genreRepository.delete(insertGenre2);

        List<Genre> expected = Collections.emptyList();
        List<Genre> actual = genreRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void genre__delete_NonexistentId__notDelete() {
        List<Genre> actualBeforeDelete = genreRepository.findAll();
        Assertions.assertNotNull(actualBeforeDelete);
        genreRepository.delete(testGenre);


        List<Genre> expected = actualBeforeDelete;
        List<Genre> actual = genreRepository.findAll();
        Assertions.assertEquals(expected, actual);
    }
}
