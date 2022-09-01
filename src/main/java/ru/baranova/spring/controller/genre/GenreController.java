package ru.baranova.spring.controller.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;

@Controller
@RequestMapping("/list/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/gc")
    public String createList(Model model) {
        model.addAttribute("genre", new Genre());
        return "genre/createGenre";
    }

    @PostMapping
    public String create(@ModelAttribute("genre") Genre genre) {
        genreService.create(genre.getName(), genre.getDescription());
        return "redirect:/list/genre/gr-all";
    }

    @GetMapping("/gr-id")
    public String readById(@RequestParam("id") Integer id, Model model) {
        Genre genre = genreService.readById(id);
        model.addAttribute("genre", genre);
        return "genre/editGenre";
    }

    @GetMapping("/gr")
    public String readByName(@RequestParam("name") String name, Model model) {
        List<Genre> genres = genreService.readByName(name);
        model.addAttribute("genres", genres);
        return "genre/listGenre";
    }

    @GetMapping("/gr-all")
    public String readAll(Model model) {
        List<Genre> genres = genreService.readAll();
        model.addAttribute("genres", genres);
        return "genre/listGenre";
    }

    @GetMapping("/gu")
    public String updateList(Model model, @RequestParam("id") Integer id) {
        Genre genre = genreService.readById(id);
        model.addAttribute("genre", genre);
        return "genre/editGenre";
    }

    @PostMapping("/gu")
    public String update(@ModelAttribute("genre") Genre genre, @RequestParam("id") Integer id) {
        genreService.update(genre.getId(), genre.getName(), genre.getDescription());
        return "redirect:/list/genre/gr-all";
    }

    @PostMapping("/gd")
    public String delete(@RequestParam("id") Integer id) {
        genreService.delete(id);
        return "redirect:/list/genre/gr-all";
    }
}
