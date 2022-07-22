package ru.baranova.spring.service.print.visitor;

import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

public interface EntityToStringVisitor {
    String toString(Author author);

    String toString(Book book);

    String toString(Genre genre);
}
