package ru.baranova.spring.domain;

public abstract class BusinessConstants {
    public interface ShellEntityService {
        String COMPLETE_CREATE = "Новое поле добавлено";
        String COMPLETE_UPDATE = "Поле %s изменено на %s";
        String COMPLETE_DELETE = "Поле %s удалено";
        String WARNING = "Ошибка";
        String WARNING_EXIST = "Ошибка, указанное поле уже существует %s";
    }
}
