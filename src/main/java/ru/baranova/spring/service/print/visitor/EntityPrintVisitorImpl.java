package ru.baranova.spring.service.print.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

@Component
@RequiredArgsConstructor
public class EntityPrintVisitorImpl implements EntityPrintVisitor {
    private final OutputDao outputDaoConsole;
    private final CheckService checkServiceImpl;

    public void print(@NonNull Author author) {
        if (checkServiceImpl.isAllFieldsNotNull(author)) {
            String str = String.format("%d. %s %s", author.getId(), author.getSurname(), author.getName());
            outputDaoConsole.output(str);
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void print(@NonNull Book book) {
        if (book.getId() != null && book.getTitle() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(book.getId()).append(". \"").append(book.getTitle()).append("\"");

            if (book.getAuthor() != null) {
                if (checkServiceImpl.isAllFieldsNotNull(book.getAuthor())) {
                    sb.append(", ").append(book.getAuthor().getSurname())
                            .append(" ").append(book.getAuthor().getName().charAt(0)).append(".");
                }
            }

            outputDaoConsole.output(sb.toString());
            if (book.getGenreList() != null && !book.getGenreList().isEmpty()) {
                outputDaoConsole.output(", жанр: ");
                book.getGenreList().forEach(this::print);
            }
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void print(@NonNull Genre genre) {
        genre.setDescription(genre.getDescription() == null ?
                BusinessConstants.PrintService.GENRE_DESCRIPTION_NULL : genre.getDescription());

        if (checkServiceImpl.isAllFieldsNotNull(genre)) {
            String str = String.format("%d. %s - %s", genre.getId(), genre.getName(), genre.getDescription());
            outputDaoConsole.output(str);
        } else {
            throw new NullPointerException();
        }
    }
}
