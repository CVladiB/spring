package ru.baranova.spring.controller.comment;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.CommentShellController;
import ru.baranova.spring.service.data.comment.CommentService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@TestConfiguration
@Getter
public class CommentShellControllerTestConfig {
    @MockBean
    private CommentService commentService;
    @MockBean
    private EntityPrintVisitor printer;

    private String create;
    private String readById;
    private String readByAuthor;
    private String readAll;
    private String update;
    private String delete;
    private String COMPLETE_CREATE;
    private String COMPLETE_OUTPUT;
    private String COMPLETE_UPDATE;
    private String COMPLETE_DELETE;
    private String WARNING;
    private String WARNING_COMMENT_NULL;

    @Bean
    public CommentShellController commentShellController(CommentService commentService, EntityPrintVisitor printer) {
        create = "cc";
        readById = "cr-id";
        readByAuthor = "cr";
        readAll = "cr-all";
        update = "cu";
        delete = "cd";

        COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
        COMPLETE_OUTPUT = "Корерктный вывод";
        COMPLETE_UPDATE = "Поле изменено";
        COMPLETE_DELETE = "Поле удалено";
        WARNING = "Ошибка";
        WARNING_COMMENT_NULL = "Ошибка печати комментария, поля не заполнены";

        return new CommentShellController(commentService, printer);
    }
}
