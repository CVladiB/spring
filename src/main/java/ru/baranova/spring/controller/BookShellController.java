package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.print.PrintService;

@RequiredArgsConstructor
@Controller
@ShellComponent
public class BookShellController {

    private final GenreService bookServiceImpl;
    private final PrintService printServiceImpl;

    @ShellMethod(group = "Book", value = "Create book", key = {"create-book"})
    public String create(Integer id, String title, String authorSurname, String authorName, String... genreArg) {
//        return bookServiceImpl.create(id, title, authorSurname, authorName, genreArg);
        return null;
    }

    @ShellMethod(group = "Book", value = "Read book", key = {"read-book"})
    public String read(Integer id) {
//        return bookServiceImpl.read(id);
        return null;
    }

    @ShellMethod(group = "Book", value = "Read all book", key = {"read-all-book"})
    public String readAll() {
//        return bookServiceImpl.readAll();
        return null;
    }

    @ShellMethod(group = "Book", value = "Update book", key = {"update-book"})
    public String update(Integer id, String title, String authorSurname, String authorName, String... genreArg) {
//        return bookServiceImpl.update(id, title, authorSurname, authorName, genreArg);
        return null;
    }

    @ShellMethod(group = "Book", value = "Delete book", key = {"delete-book"})
    public String delete(Integer id) {
//        return bookServiceImpl.delete(id);
        return null;
    }
}
