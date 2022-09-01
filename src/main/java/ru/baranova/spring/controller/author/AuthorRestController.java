package ru.baranova.spring.controller.author;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.data.author.AuthorService;

import java.util.List;

@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorRestController {
    private static BusinessConstants.ShellEntityServiceLog bc;
    private final AuthorService authorService;

    @PutMapping("/ac")
    public Author create(@RequestParam("surname") String surname, @RequestParam("name") String name) {
        return authorService.create(surname, name);
    }

    @GetMapping("/ar-id")
    public Author readById(@RequestParam("id") Integer id) {
        return authorService.readById(id);
    }

    @GetMapping("/ar")
    public List<Author> readBySurnameAndName(@RequestParam("surname") String surname, @RequestParam("name") String name) {
        return authorService.readBySurnameAndName(surname, name);
    }

    @GetMapping("/ar-all")
    public List<Author> readAll() {
        return authorService.readAll();
    }

    @PatchMapping("/au/{id}")
    public Author update(@PathVariable("id") Integer id, @RequestParam("surname") String surname, @RequestParam("name") String name) {
        return authorService.update(id, surname, name);
    }

    @DeleteMapping("/ad")
    public void delete(@RequestParam("id") Integer id) {
        authorService.delete(id);
    }
}
