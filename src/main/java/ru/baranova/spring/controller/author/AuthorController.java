package ru.baranova.spring.controller.author;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.service.data.author.AuthorService;

import java.util.List;

@Controller
@RequestMapping("/list/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping("/ac")
    public String createList(Model model) {
        model.addAttribute("author", new Author());
        return "author/createAuthor";
    }

    @PostMapping
    public String create(@ModelAttribute("author") Author author) {
        authorService.create(author.getSurname(), author.getName());
        return "redirect:/list/author/ar-all";
    }

    @GetMapping("/ar-id")
    public String readById(@RequestParam("id") Integer id, Model model) {
        Author author = authorService.readById(id);
        model.addAttribute("author", author);
        return "author/editAuthor";
    }

    // todo добавить строку поиска
    @GetMapping("/ar")
    public String readBySurnameAndName(@RequestParam("surname") String surname, @RequestParam("name") String name, Model model) {
        List<Author> authors = authorService.readBySurnameAndName(surname, name);
        model.addAttribute("authors", authors);
        return "author/listAuthor";
    }

    @GetMapping("/ar-all")
    public String readAll(Model model) {
        List<Author> authors = authorService.readAll();
        model.addAttribute("authors", authors);
        return "author/listAuthor";
    }

    @GetMapping("/au")
    public String updateList(Model model, @RequestParam("id") Integer id) {
        Author author = authorService.readById(id);
        model.addAttribute("author", author);
        return "author/editAuthor";
    }

    @PostMapping("/au")
    public String update(@ModelAttribute("author") Author author, @RequestParam("id") Integer id) {
        authorService.update(author.getId(), author.getSurname(), author.getName());
        return "redirect:/list/author/ar-all";
    }

    @PostMapping("/ad")
    public String delete(@RequestParam("id") Integer id) {
        authorService.delete(id);
        return "redirect:/list/author/ar-all";
    }
}
