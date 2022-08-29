package ru.baranova.spring.service.print.visitor;

import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;

public interface EntityPrintVisitor {
    void print(Author author);

    void print(Book book);

    void print(Genre genre);

    void print(Comment comment);
}
