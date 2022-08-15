package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.domain.BookDTO;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
public class LibraryShellController {

    private static BusinessConstants.ShellEntityServiceLog bc;
    private final LibraryService libraryService;
    private final ParseService parseService;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Book", value = "Create book", key = {"bc"})
    public String create(String title, String authorSurname, String authorName, String genreNames) {
        List<String> genreNameList = parseService.parseLinesToListStrByComma(genreNames);
        BookDTO book;
        return genreNameList.isEmpty() ? bc.WARNING
                : (book = libraryService.create(title, authorSurname, authorName, genreNameList)) == null ? bc.WARNING
                : String.format(bc.COMPLETE_CREATE, book.getId());
    }

    @ShellMethod(group = "Book", value = "Create book", key = {"bc-id"})
    public String createById(String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseService.parseLinesToListIntByComma(genreIds);
        BookDTO book;
        return genreIdList.isEmpty() ? bc.WARNING
                : (book = libraryService.create(title, authorId, genreIdList)) == null ? bc.WARNING
                : String.format(bc.COMPLETE_CREATE, book.getId());
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"br-id"})
    public String readById(Integer id) {
        BookDTO book = libraryService.readById(id);
        printer.print(book);
        return book == null ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"br"})
    public String readByTitle(String title) {
        List<BookDTO> bookList = libraryService.readByTitle(title);
        bookList.forEach(printer::print);
        return bookList.isEmpty() ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Read all book", key = {"br-all"})
    public String readAll() {
        List<BookDTO> bookList = libraryService.readAll();
        bookList.forEach(printer::print);
        return bookList.isEmpty() ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu"})
    public String update(Integer id, String title, String authorSurname, String authorName, String genreNames) {
        List<String> genreNameList = parseService.parseLinesToListStrByComma(genreNames);
        return genreNameList.isEmpty() ? bc.WARNING
                : libraryService.update(id, title, authorSurname, authorName, genreNameList) == null ? bc.WARNING
                : bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id"})
    public String updateById(Integer id, String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseService.parseLinesToListIntByComma(genreIds);
        return genreIdList.isEmpty() ?
                bc.WARNING
                : libraryService.update(id, title, authorId, genreIdList) == null ?
                bc.WARNING
                : bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Delete book", key = {"bd"})
    public String delete(Integer id) {
        return libraryService.delete(id) ? bc.COMPLETE_DELETE : bc.WARNING;
    }
}
