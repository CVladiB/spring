package ru.baranova.spring.service.print.visitor;

import org.springframework.stereotype.Component;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.Genre;

@Component
public class EntityToStringVisitorImpl implements EntityToStringVisitor {
    public String toString(Author author) {
        return String.format("%d. %s %s.", author.getId(), author.getSurname(), author.getName());
    }

    @Override
    public String toString(Book book) {
        StringBuilder sb = new StringBuilder();
        sb.append(book.getId()).append(". \"").append(book.getTitle()).append("\", ")
                .append(book.getAuthor().getSurname()).append(book.getAuthor().getName().charAt(0))
                .append(", жанр: ");
        for (Genre genre : book.getGenre()) {
            sb.append(genre.getName()).append(", ");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public String toString(Genre genre) {
        return String.format("%d. %s - %s", genre.getId(), genre.getName(), genre.getDescription());
    }
}
