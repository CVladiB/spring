package ru.baranova.spring.service.print.visitor;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BookEntity;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.app.CheckService;

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
    public void print(@NonNull BookEntity bookEntity) {
        if (bookEntity.getId() != null && bookEntity.getTitle() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(bookEntity.getId()).append(". \"")
                    .append(bookEntity.getTitle()).append("\"");
            if (bookEntity.getAuthorId() != null) {
                sb.append(", authorId: ").append(bookEntity.getAuthorId());
            }
            if (!bookEntity.getGenreListId().isEmpty()) {
                sb.append(", genreId: ");
                bookEntity.getGenreListId().forEach(g -> sb.append(g).append(", "));
            }
            outputDao.output(sb.toString());
        } else {
            throw new NullPointerException(BusinessConstants.PrintService.WARNING_BOOK_NULL);
        }
    }

    @Override
    public void print(@NonNull BookDTO book) {
        if (book.getId() != null && book.getTitle() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(book.getId()).append(". \"").append(book.getTitle()).append("\"");

            if (book.getAuthor() != null) {
                if (checkService.doCheck(book.getAuthor(), checkService::checkAllFieldsAreNotNull)) {
                    sb.append(", ").append(book.getAuthor().getSurname())
                            .append(" ").append(book.getAuthor().getName().charAt(0)).append(".");
                }
            }

            outputDao.output(sb.toString());
            if (book.getGenreList() != null && !book.getGenreList().isEmpty()) {
                outputDao.output(", жанр: ");
                book.getGenreList().forEach(this::print);
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
}
