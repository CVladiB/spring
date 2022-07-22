package ru.baranova.spring.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Controller;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.PrintService;

@RequiredArgsConstructor
@Controller
@ShellComponent
public class AuthorShellController {

    //todo think about it
    private final AuthorService authorServiceImpl;
    private final PrintService printServiceImpl;

    @ShellMethod(group = "Author", value = "Create author", key = {"create-author"})
    public String create(String surname, String name) {
//        return authorServiceImpl.create(surname, name);
        return null;
    }

    @ShellMethod(group = "Author", value = "Read author by ID", key = {"read-author-id"})
    public void read(Integer id) {
        //authorServiceImpl.
    }

    @ShellMethod(group = "Author", value = "Read author by surname and name", key = {"read-author"})
    public String readByNameAndSurname(String surname, String name) {
//        return authorServiceImpl.read(surname, name);
        return null;
    }

    @ShellMethod(group = "Author", value = "Read all authors", key = {"read-all-author"})
    public String readAll() {
//        return authorServiceImpl.readAll();
        return null;
    }

    @ShellMethod(group = "Author", value = "Update author", key = {"update-author"})
    public String update(Integer id, String surname, String name) {
//        return authorServiceImpl.update(id, surname, name);
        return null;
    }

    @ShellMethod(group = "Author", value = "Delete author", key = {"delete-author"})
    public String delete(Integer id) {
//        return authorServiceImpl.delete(id);
        return null;
    }
}
