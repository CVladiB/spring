package ru.baranova.spring.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;

import java.util.List;

@Controller
@RequestMapping("/list/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/cc")
    public String createList(Model model) {
        model.addAttribute("comment", new Comment());
        return "comment/createComment";
    }

    @PostMapping
    public String create(@ModelAttribute("comment") Comment comment) {
        commentService.create(comment.getAuthor(), comment.getText());
        return "redirect:/list/comment/cr-all";
    }

    @GetMapping("/cr-id")
    public String readById(@RequestParam("id") Integer id, Model model) {
        Comment comment = commentService.readById(id);
        model.addAttribute("comment", comment);
        return "comment/editComment";
    }

    @GetMapping("/cr")
    public String readByAuthorList(Model model) {
        model.addAttribute("comment", new Comment());
        return "comment/findComment";
    }

    @GetMapping("/cr-by-author")
    public String readByAuthor(@ModelAttribute("comment") Comment comment, Model model) {
        List<Comment> comments = commentService.readByAuthorOfComment(comment.getAuthor());
        model.addAttribute("comments", comments);
        return "comment/listComment";
    }

    @GetMapping("/cr-all")
    public String readAll(Model model) {
        List<Comment> comments = commentService.readAll();
        model.addAttribute("comments", comments);
        return "comment/listComment";
    }

    @GetMapping("/cu")
    public String updateList(Model model, @RequestParam("id") Integer id) {
        Comment comment = commentService.readById(id);
        model.addAttribute("comment", comment);
        return "comment/editComment";
    }

    @PostMapping("/cu")
    public String update(@ModelAttribute("comment") Comment comment, @RequestParam("id") Integer id) {
        commentService.update(comment.getId(), comment.getText());
        return "redirect:/list/comment/cr-all";
    }

    @PostMapping("/cd")
    public String delete(@RequestParam("id") Integer id) {
        commentService.delete(id);
        return "redirect:/list/comment/cr-all";
    }
}
