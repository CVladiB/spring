package ru.baranova.spring.controller.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.genre.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreRestController {
    private final GenreService genreService;

    @PutMapping("/gc")
    public Genre create(@RequestParam("name") String name, @RequestParam("description") String description) {
        return genreService.create(name, description);
    }

    @GetMapping("/gr-id")
    public Genre readById(@RequestParam("id") Integer id) {
        return genreService.readById(id);
    }

    @GetMapping("/gr")
    public List<Genre> readByName(@RequestParam("name") String name) {
        return genreService.readByName(name);
    }

    @GetMapping("/gr-all")
    public List<Genre> readAll() {
        return genreService.readAll();
    }

    @PatchMapping("/gu")
    public Genre update(@RequestParam("id") Integer id, @RequestParam("name") String name, @RequestParam("description") String description) {
        return genreService.update(id, name, description);
    }

    @DeleteMapping("/gd")
    public void delete(@RequestParam("id") Integer id) {
        genreService.delete(id);
    }
}
