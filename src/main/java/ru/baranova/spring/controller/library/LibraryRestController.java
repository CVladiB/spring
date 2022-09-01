package ru.baranova.spring.controller.library;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.service.data.LibraryService;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class LibraryRestController {
    private final LibraryService libraryService;

    @PutMapping("/bc")
    public Book create(@RequestParam("title") String title
            , @RequestParam("authorSurname") String authorSurname
            , @RequestParam("authorName") String authorName
            , @RequestParam("genreNames") List<String> genreNames) {
        return libraryService.create(title, authorSurname, authorName, genreNames);
    }

    @PutMapping("/bc-id")
    public Book createById(@RequestParam("title") String title
            , @RequestParam("authorId") Integer authorId
            , @RequestParam("genreIds") List<Integer> genreIds) {
        return libraryService.create(title, authorId, genreIds);
    }

    @GetMapping("/br-id")
    public Book readById(@RequestParam("id") Integer id) {
        return libraryService.readById(id);
    }

    @GetMapping("/br")
    public List<Book> readByTitle(@RequestParam("title") String title) {
        return libraryService.readByTitle(title);
    }

    @GetMapping("/br-all")
    public List<Book> readAll() {
        return libraryService.readAll();
    }

    @PatchMapping("/bu")
    public Book update(@RequestParam("id") Integer id
            , @RequestParam("title") String title
            , @RequestParam("authorSurname") String authorSurname
            , @RequestParam("authorName") String authorName
            , @RequestParam("genreNames") List<String> genreNames) {
        return libraryService.update(id, title, authorSurname, authorName, genreNames);
    }

    @PatchMapping("/bu-id")
    public Book updateById(@RequestParam("id") Integer id
            , @RequestParam("title") String title
            , @RequestParam("authorId") Integer authorId
            , @RequestParam("genreIds") List<Integer> genreIds) {
        return libraryService.update(id, title, authorId, genreIds);
    }

    @PatchMapping("/bu-id-cc")
    public Book updateByIdAddComment(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentAuthor") String commentAuthor
            , @RequestParam("commentText") String commentText) {
        return libraryService.updateAddCommentToBook(bookId, commentAuthor, commentText);
    }

    @PatchMapping("/bu-id-cc-id")
    public Book updateByIdAddCommentById(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId) {
        return libraryService.updateAddCommentByIdToBook(bookId, commentId);
    }

    @PatchMapping("/bu-id-cu")
    public Book updateByIdUpdateComment(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId
            , @RequestParam("commentText") String commentText) {
        return libraryService.updateUpdateCommentToBook(bookId, commentId, commentText);
    }

    @DeleteMapping("/bd")
    public void delete(@RequestParam("id") Integer id) {
        libraryService.delete(id);
    }
}
