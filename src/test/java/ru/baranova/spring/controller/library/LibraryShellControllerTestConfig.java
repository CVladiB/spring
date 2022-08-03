package ru.baranova.spring.controller.library;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.LibraryShellController;
import ru.baranova.spring.service.app.ParseService;
import ru.baranova.spring.service.data.LibraryService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Getter
@TestConfiguration
public class LibraryShellControllerTestConfig {
    @MockBean
    private LibraryService libraryServiceImpl;
    @MockBean
    private ParseService parseServiceImpl;
    @MockBean
    private EntityPrintVisitor printer;
    private String create;
    private String createById;
    private String readById;
    private String readByTitle;
    private String readAll;
    private String update;
    private String updateById;
    private String delete;
    private String COMPLETE_CREATE;
    private String COMPLETE_OUTPUT;
    private String COMPLETE_UPDATE;
    private String COMPLETE_DELETE;
    private String WARNING;
    private String WARNING_BOOK_NULL;

    @Bean
    public LibraryShellController libraryShellController(LibraryService libraryServiceImpl, ParseService parseServiceImpl, EntityPrintVisitor printer) {
        create = "bc";
        createById = "bc-id";
        readById = "br-id";
        readByTitle = "br";
        readAll = "br-all";
        update = "bu";
        updateById = "bu-id";
        delete = "bd";

        COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
        COMPLETE_OUTPUT = "Корерктный вывод";
        COMPLETE_UPDATE = "Поле изменено";
        COMPLETE_DELETE = "Поле удалено";
        WARNING = "Ошибка";
        WARNING_BOOK_NULL = "Ошибка печати книги, поля не заполнены";

        return new LibraryShellController(libraryServiceImpl, parseServiceImpl, printer);
    }

}
