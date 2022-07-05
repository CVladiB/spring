package ru.baranova.spring.dao.io;

public interface OutputDao {

    void outputLine(String line);

    void outputFormatLine(String formatLine, Object... args);
}
