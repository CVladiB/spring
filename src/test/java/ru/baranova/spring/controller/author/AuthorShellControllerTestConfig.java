package ru.baranova.spring.controller.author;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.AuthorShellController;
import ru.baranova.spring.service.data.author.AuthorService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Getter
@TestConfiguration
public class AuthorShellControllerTestConfig {
    @MockBean
    private AuthorService authorServiceImpl;
    @MockBean
    private EntityPrintVisitor printer;
    private String create;
    private String readById;
    private String readBySurnameAndName;
    private String readAll;
    private String update;
    private String delete;
    private String COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
    private String COMPLETE_OUTPUT = "Корерктный вывод";
    private String COMPLETE_UPDATE = "Поле изменено";
    private String COMPLETE_DELETE = "Поле удалено";
    private String WARNING = "Ошибка";

    @Bean
    public AuthorShellController authorShellController(AuthorService authorServiceImpl, EntityPrintVisitor printer) {
        create = "ac";
        readById = "ar-id";
        readBySurnameAndName = "ar";
        readAll = "ar-all";
        update = "au";
        delete = "ad";

        COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
        COMPLETE_OUTPUT = "Корерктный вывод";
        COMPLETE_UPDATE = "Поле изменено";
        COMPLETE_DELETE = "Поле удалено";
        WARNING = "Ошибка";

        return new AuthorShellController(authorServiceImpl, printer);
    }

}
