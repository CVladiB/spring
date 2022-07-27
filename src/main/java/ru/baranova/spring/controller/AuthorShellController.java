package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.domain.Author;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorShellController {
    private final AuthorService authorServiceImpl;
    private final CheckService checkServiceImpl;
    private final EntityPrintVisitor printer;
    //Для использования нескольких способов печати (удалю после коммита)
    //private final List<EntityPrintVisitor> printers;

    @ShellMethod(group = "Author", value = "Create author", key = {"create-author"})
    public String create(String surname, String name) {
        Author author = authorServiceImpl.create(surname, name);
        // Проверка всех полей на наличие записей, достаточно вернуть null вместо объекта (удалю после коммита)
        if (checkServiceImpl.isAllFieldsNotNull(author)) {
            //Для использования нескольких способов печати (удалю после коммита)
            //printers.forEach(v -> v.print(author));
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_CREATE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read author by Id", key = {"read-author-id"})
    public String readById(Integer id) {
        Author author = authorServiceImpl.readById(id);
        if (author != null) {
            printer.print(author);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read author by surname and name", key = {"read-author"})
    public String readBySurnameAndName(String surname, String name) {
        List<Author> authorList = authorServiceImpl.readBySurnameAndName(surname, name);
        if (authorList != null && !authorList.isEmpty()) {
            authorList.forEach(printer::print);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Read all authors", key = {"read-all-author"})
    public String readAll() {
        List<Author> authorList = authorServiceImpl.readAll();
        if (!authorList.isEmpty()) {
            authorList.forEach(printer::print);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Update author", key = {"update-author"})
    public String update(Integer id, String surname, String name) {
        Author author = authorServiceImpl.update(id, surname, name);
        if (author != null) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_UPDATE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Author", value = "Delete author", key = {"delete-author"})
    public String delete(Integer id) {
        if (authorServiceImpl.delete(id)) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_DELETE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }
}
