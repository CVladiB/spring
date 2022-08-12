package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellController {
    private static BusinessConstants.ShellEntityServiceLog bc;
    private final AuthorService authorServiceImpl;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Author", value = "Create author", key = {"ac"})
    public String create(String surname, String name) {
        Author author = authorServiceImpl.create(surname, name);
        return author == null ? bc.WARNING : String.format(bc.COMPLETE_CREATE, author.getId());
    }

    @ShellMethod(group = "Author", value = "Read author by Id", key = {"ar-id"})
    public String readById(Integer id) {
        Author author = authorServiceImpl.readById(id);
        printer.print(author);
        return author == null ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Read author by surname and name", key = {"ar"})
    public String readBySurnameAndName(String surname, String name) {
        List<Author> authorList = authorServiceImpl.readBySurnameAndName(surname, name);
        authorList.forEach(printer::print);
        return authorList.isEmpty() ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Read all authors", key = {"ar-all"})
    public String readAll() {
        List<Author> authorList = authorServiceImpl.readAll();
        authorList.forEach(printer::print);
        return authorList.isEmpty() ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Author", value = "Update author", key = {"au"})
    public String update(Integer id, String surname, String name) {
        return authorServiceImpl.update(id, surname, name) == null ? bc.WARNING : bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Author", value = "Delete author", key = {"ad"})
    public String delete(Integer id) {
        return authorServiceImpl.delete(id) ? bc.COMPLETE_DELETE : bc.WARNING;
    }
}
