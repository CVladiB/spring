package ru.baranova.spring.service.print.visitor;

import lombok.RequiredArgsConstructor;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

//@Component
@RequiredArgsConstructor
public class EntitySmilePrintVisitor implements EntityPrintVisitor {
    private final OutputDao outputDao;

    @Override
    public void print(Author author) {
        outputDao.output(":)");
    }

    @Override
    public void print(Book book) {

    }

    @Override
    public void print(Genre genre) {

    }
}
