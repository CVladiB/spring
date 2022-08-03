package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.domain.Book;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class LibraryShellController {

    private final LibraryService libraryServiceImpl;
    private final ParseService parseServiceImpl;

    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Book", value = "Create book", key = {"bc"})
    public String create(String title, String authorSurname, String authorName, String genreNames) {
        List<String> genreNameList = parseServiceImpl.parseLinesToListByComma(genreNames);
        Book book = null;
        if (!genreNameList.isEmpty()) {
            book = libraryServiceImpl.create(title, authorSurname, authorName, genreNameList);
        }
        if (genreNameList.isEmpty() || book == null) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
        return String.format(BusinessConstants.ShellEntityServiceLog.COMPLETE_CREATE, book.getId());
    }

    @ShellMethod(group = "Book", value = "Create book", key = {"bc-id"})
    public String createById(String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseServiceImpl.parseLinesToListByComma(genreIds)
                .stream()
                .map(parseServiceImpl::parseStringToInt)
                .collect(Collectors.toList());

        Book book = null;
        if (!genreIdList.isEmpty()) {
            book = libraryServiceImpl.create(title, authorId, genreIdList);

        }
        if (genreIdList.isEmpty() || book == null) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
        return String.format(BusinessConstants.ShellEntityServiceLog.COMPLETE_CREATE, book.getId());
    }


    @ShellMethod(group = "Book", value = "Read book", key = {"br-id"})
    public String readById(Integer id) {
        Book book = libraryServiceImpl.readById(id);
        if (book != null) {
            try {
                printer.print(book);
                return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
            } catch (NullPointerException e) {
                return BusinessConstants.ShellEntityServiceLog.WARNING_BOOK_NULL;
            }
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"br"})
    public String readByTitle(String title) {
        List<Book> bookList = libraryServiceImpl.readByTitle(title);
        if (!bookList.isEmpty()) {
            try {
                bookList.forEach(printer::print);
                return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
            } catch (NullPointerException e) {
                return BusinessConstants.ShellEntityServiceLog.WARNING_BOOK_NULL;
            }
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Book", value = "Read all book", key = {"br-all"})
    public String readAll() {
        List<Book> bookList = libraryServiceImpl.readAll();
        if (!bookList.isEmpty()) {
            try {
                bookList.forEach(printer::print);
                return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
            } catch (NullPointerException e) {
                return BusinessConstants.ShellEntityServiceLog.WARNING_BOOK_NULL;
            }
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu"})
    public String update(Integer id, String title, String authorSurname, String authorName, String genreNames) {
        List<String> genreNameList = parseServiceImpl.parseLinesToListByComma(genreNames);
        Book book = null;
        if (!genreNameList.isEmpty()) {
            book = libraryServiceImpl.update(id, title, authorSurname, authorName, genreNameList);
        }
        if (genreNameList.isEmpty() || book == null) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
        return BusinessConstants.ShellEntityServiceLog.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id"})
    public String updateById(Integer id, String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseServiceImpl.parseLinesToListByComma(genreIds)
                .stream()
                .map(parseServiceImpl::parseStringToInt)
                .collect(Collectors.toList());
        Book book = null;
        if (!genreIdList.isEmpty()) {
            book = libraryServiceImpl.update(id, title, authorId, genreIdList);

        }
        if (genreIdList.isEmpty() || book == null) {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
        return BusinessConstants.ShellEntityServiceLog.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Delete book", key = {"bd"})
    public String delete(Integer id) {
        if (libraryServiceImpl.delete(id)) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_DELETE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }
}
