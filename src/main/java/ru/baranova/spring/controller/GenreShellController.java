package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.domain.BusinessConstants;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
public class GenreShellController {

    private final GenreService genreServiceImpl;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Genre", value = "Create genre", key = {"gc"})
    public String create(String name, String description) {
        Genre genre = genreServiceImpl.create(name, description);
        if (genre != null) {
            return String.format(BusinessConstants.ShellEntityServiceLog.COMPLETE_CREATE, genre.getId());
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Genre", value = "Read genre by Id", key = {"gr-id"})
    public String readById(Integer id) {
        Genre genre = genreServiceImpl.readById(id);
        if (genre != null) {
            printer.print(genre);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Genre", value = "Read genre by name", key = {"gr"})
    public String readByName(String name) {
        Genre genre = genreServiceImpl.readByName(name);
        if (genre != null) {
            printer.print(genre);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Genre", value = "Read all genres", key = {"gr-all"})
    public String readAll() {
        List<Genre> genreList = genreServiceImpl.readAll();
        if (!genreList.isEmpty()) {
            genreList.forEach(printer::print);
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_OUTPUT;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Genre", value = "Update genre", key = {"gu"})
    public String update(Integer id, String name, String description) {
        Genre genre = genreServiceImpl.update(id, name, description);
        if (genre != null) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_UPDATE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }

    @ShellMethod(group = "Genre", value = "Delete genre", key = {"gd"})
    public String delete(Integer id) {
        if (genreServiceImpl.delete(id)) {
            return BusinessConstants.ShellEntityServiceLog.COMPLETE_DELETE;
        } else {
            return BusinessConstants.ShellEntityServiceLog.WARNING;
        }
    }
}
