package ru.baranova.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.author.AuthorDao;
import ru.baranova.spring.dao.io.InputDao;
import ru.baranova.spring.dao.io.OutputDao;
import ru.baranova.spring.domain.Author;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDao authorDaoJdbc;
    private final InputDao inputDaoReader;
    private final OutputDao outputDaoConsole;


    public void create() {
        outputDaoConsole.output("Введите фамилию нового автора");
        String surname = inputDaoReader.input();
        outputDaoConsole.output("Введите имя нового автора");
        String name = inputDaoReader.input();
        Author author = new Author();
        author.setName(name);
        author.setSurname(surname);
        authorDaoJdbc.create(author);
    }

    @Override
    public void read() {

    }

    @Override
    public void readAll() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }


}
