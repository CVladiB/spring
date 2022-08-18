package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellController {
    private static BusinessConstants.ShellEntityServiceLog bc;
    private final AuthorService authorService;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Author", value = "Create author", key = {"ac"})
    public String create(String surname, String name) {
        Author author = authorService.create(surname, name);
        return String.format(bc.COMPLETE_CREATE, author.getId());
    }

    @ShellMethod(group = "Author", value = "Read author by Id", key = {"ar-id"})
    public String readById(Integer id) {
        Author author = authorService.readById(id);
        printer.print(author);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Read author by surname and name", key = {"ar"})
    public String readBySurnameAndName(String surname, String name) {
        List<Author> authorList = authorService.readBySurnameAndName(surname, name);
        authorList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Read all authors", key = {"ar-all"})
    public String readAll() {
        List<Author> authorList = authorService.readAll();
        authorList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Update author", key = {"au"})
    public String update(Integer id, String surname, String name) {
        authorService.update(id, surname, name);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Author", value = "Delete author", key = {"ad"})
    public String delete(Integer id) {
        authorService.delete(id);
        return bc.COMPLETE_DELETE;
    }
}
