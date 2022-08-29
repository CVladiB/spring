package ru.baranova.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.baranova.spring.config.BusinessConstants;
import ru.baranova.spring.model.Comment;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
public class CommentShellController {
    private static BusinessConstants.ShellEntityServiceLog bc;
    private final CommentService commentService;
    private final EntityPrintVisitor printer;

    @ShellMethod(group = "Comment", value = "Create Comment", key = {"cc"})
    public String create(String name, String description) {
        Comment comment = commentService.create(name, description);
        return String.format(bc.COMPLETE_CREATE, comment.getId());
    }

    @ShellMethod(group = "Comment", value = "Read Comment by Id", key = {"cr-id"})
    public String readById(Integer id) {
        Comment comment = commentService.readById(id);
        printer.print(comment);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Comment", value = "Read Comment by author", key = {"cr"})
    public String readByName(String author) {
        List<Comment> commentList = commentService.readByAuthorOfComment(author);
        commentList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;
    }

    @ShellMethod(group = "Comment", value = "Read all Comments", key = {"cr-all"})
    public String readAll() {
        List<Comment> commentList = commentService.readAll();
        commentList.forEach(printer::print);
        return bc.COMPLETE_OUTPUT;

    }

    @ShellMethod(group = "Comment", value = "Update Comment", key = {"cu"})
    public String update(Integer id, String author) {
        commentService.update(id, author);
        return bc.COMPLETE_UPDATE;
    }

    @ShellMethod(group = "Comment", value = "Delete Comment", key = {"cd"})
    public String delete(Integer id) {
        commentService.delete(id);
        return bc.COMPLETE_DELETE;
    }
}
