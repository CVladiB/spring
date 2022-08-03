package ru.baranova.spring.domain;

public abstract class BusinessConstants {
    public interface ShellEntityServiceLog {
        String COMPLETE_CREATE = "Новое поле добавлено, присвоен id - %d";
        String COMPLETE_OUTPUT = "Корерктный вывод";
        String COMPLETE_UPDATE = "Поле изменено";
        String COMPLETE_DELETE = "Поле удалено";
        String WARNING = "Ошибка";
        String WARNING_AUTHOR_NULL = "Ошибка печати автора, поля не заполнены";
        String WARNING_GENRE_NULL = "Ошибка печати жанра, поля не заполнены";
        String WARNING_BOOK_NULL = "Ошибка печати книги, поля не заполнены";

    }

    public interface CheckServiceLog {
        String NOTHING_INPUT = "Ничего не введено";
        String SHOULD_EXIST_INPUT = "Нужнно ввести существующее значение";
        String WARNING_EXIST = "Указанное поле уже существует";
        String CHAR_OR_NUMBER_INPUT = "Недопустимый ввод, исключите символы";
        String SHORT_INPUT = "Слишком короткий ввод, минимум %d символов";
        String LONG_INPUT = "Слишком длинный ввод, максимум %d символов";

    }

    public interface LibraryServiceLog {
        String WARNING_EXIST_MANY = "Существует несколько полей с указанными значениями, перепроверьте ввод или укажите Id";
        String WARNING_CREATE = "Ошибка добавления поля, перепроверьте ввод или добавьте автора и жанры отдельно";

    }

    public interface PrintService {
        String GENRE_DESCRIPTION_NULL = "описание жанра пока отсутствует";
    }
}
