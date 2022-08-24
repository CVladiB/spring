package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Book;
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
        Book book = libraryService.create(title, authorSurname, authorName, genreNameList);
        return String.format(bc.COMPLETE_CREATE, book.getId());
    }

    @ShellMethod(group = "Book", value = "Create book", key = {"bc-id"})
    public String createById(String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseService.parseLinesToListIntByComma(genreIds);
        Book book = libraryService.create(title, authorId, genreIdList);
        return String.format(bc.COMPLETE_CREATE, book.getId());
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"br-id"})
    public String readById(Integer id) {
        Book book = libraryService.readById(id);
        printer.print(book);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"br"})
    public String readByTitle(String title) {
        List<Book> bookList = libraryService.readByTitle(title);
        bookList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Read all book", key = {"br-all"})
    public String readAll() {
        List<Book> bookList = libraryService.readAll();
        bookList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu"})
    public String update(Integer id, String title, String authorSurname, String authorName, String genreNames) {
        List<String> genreNameList = parseService.parseLinesToListStrByComma(genreNames);
        libraryService.update(id, title, authorSurname, authorName, genreNameList);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id"})
    public String updateById(Integer id, String title, Integer authorId, String genreIds) {
        List<Integer> genreIdList = parseService.parseLinesToListIntByComma(genreIds);
        libraryService.update(id, title, authorId, genreIdList);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id-cc"})
    public String updateByIdAddComment(Integer bookId, String commentAuthor, String commentText) {
        libraryService.updateAddCommentToBook(bookId, commentAuthor, commentText);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id-cc-id"})
    public String updateByIdAddCommentById(Integer bookId, Integer commentId) {
        libraryService.updateAddCommentByIdToBook(bookId, commentId);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"bu-id-cu"})
    public String updateByIdUpdateComment(Integer bookId, Integer commentId, String commentText) {
        libraryService.updateUpdateCommentToBook(bookId, commentId, commentText);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Book", value = "Delete book", key = {"bd"})
    public String delete(Integer id) {
        libraryService.delete(id);
        return bc.COMPLETE_DELETE;
    }
}
