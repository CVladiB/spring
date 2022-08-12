package ru.baranova.spring.service.app.config;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.service.app.CheckService;
import ru.baranova.spring.service.app.CheckServiceImpl;

@Getter
@TestConfiguration
public class CheckServiceImplTestConfig {
    private String NOTHING_INPUT = "Ничего не введено";
    private String SHORT_INPUT = "Слишком короткий ввод, минимум %d символов";
    private String LONG_INPUT = "Слишком длинный ввод, максимум %d символов";
    private String CHAR_OR_NUMBER_INPUT = "Недопустимый ввод, исключите символы";
    private String SHOULD_EXIST_INPUT = "Нужнно ввести существующее значение";
    private String WARNING_EXIST = "Указанное поле уже существует";

    @Bean
    public CheckService CheckServiceImpl() {
        NOTHING_INPUT = "Ничего не введено";
        SHORT_INPUT = "Слишком короткий ввод, минимум %d символов";
        LONG_INPUT = "Слишком длинный ввод, максимум %d символов";
        CHAR_OR_NUMBER_INPUT = "Недопустимый ввод, исключите символы";
        SHOULD_EXIST_INPUT = "Нужнно ввести существующее значение";
        WARNING_EXIST = "Указанное поле уже существует";

        return new CheckServiceImpl();
    }
}
