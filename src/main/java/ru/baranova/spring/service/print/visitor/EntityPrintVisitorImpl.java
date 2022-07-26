package ru.baranova.spring.service.print.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

@Component
@RequiredArgsConstructor
public class EntityPrintVisitorImpl implements EntityPrintVisitor {
    private final OutputDao outputDaoConsole;

    public void print(Author author) {
        String str = String.format("%d. %s %s.", author.getId(), author.getSurname(), author.getName());
        outputDaoConsole.output(str);
    }

    @Override
    public void print(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append(book.getId()).append(". \"").append(book.getTitle()).append("\", ")
                .append(book.getAuthor().getSurname()).append(book.getAuthor().getName().charAt(0))
                .append(", жанр: ");
        outputDaoConsole.output(sb.toString());
        book.getGenre().forEach(this::print);
    }

    @Override
    public void print(Genre genre) {
        String str = String.format("%d. %s - %s", genre.getId(), genre.getName(), genre.getDescription());
        outputDaoConsole.output(str);
    }
}
