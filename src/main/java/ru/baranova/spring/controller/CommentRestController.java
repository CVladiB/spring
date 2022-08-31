package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @PutMapping("/cc")
    public Comment create(@RequestParam("author") String author, @RequestParam("text") String text) {
        return commentService.create(author, text);
    }

    @GetMapping("/cr-id")
    public Comment readById(@RequestParam("id") Integer id) {
        return commentService.readById(id);
    }

    @GetMapping("/cr")
    public List<Comment> readByAuthor(@RequestParam("author") String author) {
        return commentService.readByAuthorOfComment(author);
    }

    @GetMapping("/cr-all")
    public List<Comment> readAll() {
        return commentService.readAll();
    }

    @PatchMapping("/cu")
    public Comment update(@RequestParam("id") Integer id, @RequestParam("text") String text) {
        return commentService.update(id, text);
    }

    @DeleteMapping("/cd")
    public void delete(@RequestParam("id") Integer id) {
        commentService.delete(id);
    }
}
