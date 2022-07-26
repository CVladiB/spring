package ru.baranova.spring.domain;

public abstract class BusinessConstants {
    public interface ShellEntityService {
        String COMPLETE_CREATE = "Новое поле добавлено";
        String COMPLETE_OUTPUT = "Корерктный вывод";
        String COMPLETE_UPDATE = "Поле изменено";
        String COMPLETE_DELETE = "Поле удалено";
        String WARNING = "Ошибка";
    }
    public interface CheckConstant {
        String NOTHING_INPUT = "Ничего не введено";
        String SHOULD_EXIST_INPUT = "Нужнно ввести существующиее значение";
        String WARNING_EXIST = "Ошибка, указанное поле уже существует";
        String CHAR_OR_NUMBER_INPUT = "Недопустимый ввод, исключите символы";
        String SHORT_INPUT = "Слишком короткий ввод, минимум %d символов";
        String LONG_INPUT = "Слишком длинный ввод, максимум %d символов";

    }
}
