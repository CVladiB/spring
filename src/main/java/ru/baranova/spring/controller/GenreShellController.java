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

    private static BusinessConstants.ShellEntityServiceLog bc;
    private final GenreService genreService;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Genre", value = "Create genre", key = {"gc"})
    public String create(String name, String description) {
        Genre genre = genreService.create(name, description);
        return genre == null ? bc.WARNING : String.format(bc.COMPLETE_CREATE, genre.getId());
    }

    @ShellMethod(group = "Genre", value = "Read genre by Id", key = {"gr-id"})
    public String readById(Integer id) {
        Genre genre = genreService.readById(id);
        printer.print(genre);
        return genre == null ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Genre", value = "Read genre by name", key = {"gr"})
    public String readByName(String name) {
        Genre genre = genreService.readByName(name);
        printer.print(genre);
        return genre == null ? bc.WARNING : bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Genre", value = "Read all genres", key = {"gr-all"})
    public String readAll() {
        List<Genre> genreList = genreService.readAll();
        genreList.forEach(printer::print);
        return genreList.isEmpty() ? bc.WARNING : bc.COMPLETE_OUTPUT;

    }

    @ShellMethod(group = "Genre", value = "Update genre", key = {"gu"})
    public String update(Integer id, String name, String description) {
        return genreService.update(id, name, description) == null ? bc.WARNING : bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Genre", value = "Delete genre", key = {"gd"})
    public String delete(Integer id) {
        return genreService.delete(id) ? bc.COMPLETE_DELETE : bc.WARNING;
    }
}
