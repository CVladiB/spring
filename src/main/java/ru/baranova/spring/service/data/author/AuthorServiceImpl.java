package ru.baranova.spring.service.data.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.domain.Author;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDaoJdbc;

    @Override
    public Author create(String surname, String name) {
        Author author = new Author();
        author.setSurname(surname);
        author.setName(name);
        Integer id = authorDaoJdbc.create(author);
        author.setId(id);
        return author;
    }

    @Override
    public Author read(Integer id) {
        return authorDaoJdbc.getById(id);
    }

    @Override
    public List<Author> getBySurnameAndName(String surname, String name) {
        return authorDaoJdbc.getBySurnameAndName(surname, name);
    }

    @Override
    public List<Author> readAll() {
        return authorDaoJdbc.getAll();
    }

    @Override
    public Author update(Integer id, String surname, String name) {
        Author author = authorDaoJdbc.getById(id);
        author.setId(id);
        author.setSurname(surname);
        author.setName(name);
        authorDaoJdbc.update(author);
        return author;
    }

    @Override
    public void delete(Integer id) {
        authorDaoJdbc.delete(id);
    }

}
