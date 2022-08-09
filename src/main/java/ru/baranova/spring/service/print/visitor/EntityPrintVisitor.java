package ru.baranova.spring.service.print.visitor;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.Genre;

public interface EntityPrintVisitor {
    void print(Author author);

    void print(BookEntity bookEntity);

    void print(BookDTO book);

    void print(Genre genre);
}
