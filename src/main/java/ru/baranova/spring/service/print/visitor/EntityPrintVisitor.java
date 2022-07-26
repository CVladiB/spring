package ru.baranova.spring.service.print.visitor;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

public interface EntityPrintVisitor {
    void print(Author author);

    void print(Book book);

    void print(Genre genre);
}
