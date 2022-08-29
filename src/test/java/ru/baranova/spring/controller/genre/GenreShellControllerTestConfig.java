package ru.baranova.spring.controller.genre;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.controller.GenreShellController;
import ru.baranova.spring.service.data.genre.GenreService;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Getter
@TestConfiguration
public class GenreShellControllerTestConfig {
    @MockBean
    private GenreService genreService;
    @MockBean
    private EntityPrintVisitor printer;
    private String create;
    private String readById;
    private String readByName;
    private String readAll;
    private String update;
    private String delete;
    private String COMPLETE_CREATE;
    private String COMPLETE_OUTPUT;
    private String COMPLETE_UPDATE;
    private String COMPLETE_DELETE;
    private String WARNING;
    private String WARNING_GENRE_NULL;


    @Bean
    public GenreShellController genreShellController(GenreService genreService, EntityPrintVisitor printer) {
        create = "gc";
        readById = "gr-id";
        readByName = "gr";
        readAll = "gr-all";
        update = "gu";
        delete = "gd";

        COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
        COMPLETE_OUTPUT = "Корерктный вывод";
        COMPLETE_UPDATE = "Поле изменено";
        COMPLETE_DELETE = "Поле удалено";
        WARNING = "Ошибка";
        WARNING_GENRE_NULL = "Ошибка печати жанра, поля не заполнены";


        return new GenreShellController(genreService, printer);
    }

}
