package ru.baranova.spring.service.print.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.app.CheckService;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntityPrintVisitorImpl implements EntityPrintVisitor {
    private final OutputDao outputDao;
    private final CheckService checkService;

    public void print(@NonNull Author author) {
        if (checkService.doCheck(author, checkService::checkAllFieldsAreNotNull)) {
            String str = String.format("%d. %s %s", author.getId(), author.getSurname(), author.getName());
            outputDao.output(str);
        } else {
            throw new NullPointerException(BusinessConstants.PrintService.WARNING_AUTHOR_NULL);
        }
    }

    @Override
    public void print(@NonNull Book book) {
        if (book.getId() != null && book.getTitle() != null) {
            StringBuilder sb = new StringBuilder();

            String title = String.format("%d. \"%s\"", book.getId(), book.getTitle());
            sb.append(title);

            if (book.getAuthor() != null) {
                if (checkService.doCheck(book.getAuthor(), checkService::checkAllFieldsAreNotNull)) {
                    String author = String.format(", %s %s."
                            , book.getAuthor().getSurname()
                            , book.getAuthor().getName().charAt(0));
                    sb.append(author);
                }
            }

            if (!book.getGenreList().isEmpty()) {
                String genre = book.getGenreList()
                        .stream()
                        .map(Genre::getName)
                        .collect(Collectors.joining(", "));
                sb.append(", жанр: ").append(genre);
            }

            outputDao.output(sb.toString());

            if (!book.getCommentList().isEmpty()) {
                outputDao.output("Комментарии:");
                book.getCommentList().forEach(this::print);
            }
        } else {
            throw new NullPointerException(BusinessConstants.PrintService.WARNING_BOOK_NULL);
        }
    }

    @Override
    public void print(@NonNull Genre genre) {
        genre.setDescription(genre.getDescription() == null ?
                BusinessConstants.PrintService.GENRE_DESCRIPTION_NULL : genre.getDescription());

        if (checkService.doCheck(genre, checkService::checkAllFieldsAreNotNull)) {
            String str = String.format("%d. %s - %s", genre.getId(), genre.getName(), genre.getDescription());
            outputDao.output(str);
        } else {
            throw new NullPointerException(BusinessConstants.PrintService.WARNING_GENRE_NULL);
        }
    }

    @Override
    public void print(@NonNull Comment comment) {
        if (checkService.doCheck(comment, checkService::checkAllFieldsAreNotNull)) {
            String str = String.format("%s. %s - %s", comment.getDate(), comment.getAuthor(), comment.getText());
            outputDao.output(str);
        } else {
            throw new NullPointerException(BusinessConstants.PrintService.WARNING_COMMENT_NULL);
        }
    }
}
