package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;
import ru.baranova.spring.domain.Genre;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.print.PrintService;

@RequiredArgsConstructor
@Controller
@ShellComponent
public class GenreShellController {

    private final GenreService genreServiceImpl;
    private final PrintService printServiceImpl;


    @ShellMethod(group = "Genre", value = "Create genre", key = {"create-genre"})
    public String create(String surname, String name) {
//        return genreServiceImpl.create(surname, name);
        return null;
    }

    @ShellMethod(group = "Genre", value = "Read genre", key = {"read-genre"})
    public void read(Integer id) {
        Genre genre = genreServiceImpl.read(id);
        printServiceImpl.printEntity(genre);
    }

    @ShellMethod(group = "Genre", value = "Read all genre", key = {"read-all-genre"})
    public String readAll() {
//        return genreServiceImpl.readAll();
        return null;
    }

    @ShellMethod(group = "Genre", value = "Update genre", key = {"update-genre"})
    public String update(Integer id, String surname, String name) {
//        return genreServiceImpl.update(id, surname, name);
        return null;
    }

    @ShellMethod(group = "Genre", value = "Delete genre", key = {"delete-genre"})
    public String delete(Integer id) {
//        return genreServiceImpl.delete(id);
        return null;
    }
}
