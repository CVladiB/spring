package ru.baranova.spring.controller.library;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.data.LibraryService;

import java.util.List;

@Controller
@RequestMapping("/list/book")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping("/bc")
    public String createList(Model model) {
        model.addAttribute("book", new Book());
        return "library/createBook";
    }

    @PostMapping
    public String create(@ModelAttribute("book") Book book) {
        libraryService.create(book.getTitle(), book.getAuthor().getSurname(), book.getAuthor().getName(), book.getGenreList().stream().map(Genre::getName).toList());
        return "redirect:/list/book/br-all";
    }

    @GetMapping("/bc-id")
    public String createByIdList(Model model) {
        model.addAttribute("book", new Book());
        return "library/createBookById";
    }

    @PostMapping("/bc-id")
    public String createById(@ModelAttribute("book") Book book) {
        libraryService.create(book.getTitle(), book.getAuthor().getId(), book.getGenreList().stream().map(Genre::getId).toList());
        return "redirect:/list/book/br-all";
    }

    @GetMapping("/br-id")
    public String readById(@RequestParam("id") Integer id, Model model) {
        Book book = libraryService.readById(id);
        model.addAttribute("book", book);
        return "library/editBook";
    }

    // todo добавить строку поиска
    @GetMapping("/br")
    public String readByTitle(@RequestParam("title") String title, Model model) {
        List<Book> books = libraryService.readByTitle(title);
        model.addAttribute("books", books);
        return "library/listBook";
    }

    @GetMapping("/br-all")
    public String readAll(Model model) {
        List<Book> books = libraryService.readAll();
        model.addAttribute("books", books);
        return "library/listBook";

    }

    @GetMapping("/bu")
    public String updateList(@RequestParam("id") Integer id, Model model) {
        Book book = libraryService.readById(id);
        model.addAttribute("book", book);
        return "library/editBook";
    }

    @PostMapping("/bu")
    public String update(@ModelAttribute("book") Book book, @RequestParam("id") Integer id) {
        libraryService.update(book.getId(), book.getTitle(), book.getAuthor().getSurname(), book.getAuthor().getName(), book.getGenreList().stream().map(Genre::getName).toList());
        return "redirect:/list/book/br-all";
    }

    @GetMapping("/bu-id")
    public String updateByIdList(@RequestParam("id") Integer id, Model model) {
        Book book = libraryService.readById(id);
        model.addAttribute("book", book);
        return "library/editBookById";
    }

    @PostMapping("/bu-id")
    public String updateById(@ModelAttribute("book") Book book, @RequestParam("id") Integer id) {
        libraryService.update(book.getId(), book.getTitle(), book.getAuthor().getId(), book.getGenreList().stream().map(Genre::getId).toList());
        return "redirect:/list/book/br-all";
    }

    // todo мб запрашивать на странице коммента?
    @GetMapping("/bu-id-cc")
    public String updateByIdAddCommentList(@RequestParam("bookId") Integer bookId, Model model) {
        Book book = libraryService.readById(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comment", new Comment());
        return "library/editBookByIdAddComment";
    }

    @PostMapping("/bu-id-cc")
    public String updateByIdAddComment(@ModelAttribute("book") Book book, @ModelAttribute("comment") Comment comment, @RequestParam("bookId") Integer bookId) {
        libraryService.updateAddCommentToBook(book.getId(), comment.getAuthor(), comment.getText());
        return "redirect:/list/book/br-all";
    }

    // todo сделать после реализации метода выше
/*    @GetMapping("/bu-id-cc-id")
    public String updateByIdAddCommentByIdList(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId) {
        return libraryService.updateAddCommentByIdToBook(bookId, commentId);
    }

    @PostMapping("/bu-id-cc-id")
    public String updateByIdAddCommentById(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId) {
        return libraryService.updateAddCommentByIdToBook(bookId, commentId);
    }

    @PatchMapping("/bu-id-cu")
    public String updateByIdUpdateCommentList(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId
            , @RequestParam("commentText") String commentText) {
        return libraryService.updateUpdateCommentToBook(bookId, commentId, commentText);
    }
    @PatchMapping("/bu-id-cu")
    public String updateByIdUpdateComment(@RequestParam("bookId") Integer bookId
            , @RequestParam("commentId") Integer commentId
            , @RequestParam("commentText") String commentText) {
        return libraryService.updateUpdateCommentToBook(bookId, commentId, commentText);
    }*/

    @PostMapping("/bd")
    public String delete(@RequestParam("id") Integer id) {
        libraryService.delete(id);
        return "redirect:/list/book/br-all";
    }
}
