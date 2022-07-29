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
    private final AuthorService authorServiceImpl;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Author", value = "Create author", key = {"ac"})
    public String create(String surname, String name) {
        Author author = authorServiceImpl.create(surname, name);
        if (author != null) {
            return String.format(BusinessConstants.ShellEntityServiceLog.COMPLETE_CREATE, author.getId());
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read author by Id", key = {"ar-id"})
    public String readById(Integer id) {
        Author author = authorServiceImpl.readById(id);
        if (author != null) {
            printer.print(author);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read author by surname and name", key = {"ar"})
    public String readBySurnameAndName(String surname, String name) {
        List<Author> authorList = authorServiceImpl.readBySurnameAndName(surname, name);
        if (authorList != null && !authorList.isEmpty()) {
            authorList.forEach(printer::print);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read all authors", key = {"ar-all"})
    public String readAll() {
        List<Author> authorList = authorServiceImpl.readAll();
        if (!authorList.isEmpty()) {
            authorList.forEach(printer::print);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Update author", key = {"au"})
    public String update(Integer id, String surname, String name) {
        Author author = authorServiceImpl.update(id, surname, name);
        if (author != null) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_UPDATE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Delete author", key = {"ad"})
    public String delete(Integer id) {
        if (authorServiceImpl.delete(id)) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_DELETE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }
}
