package ru.baranova.spring.dao.io;

public interface OutputDao {

    void someMethod(String a, String b, String c, String d);

    void outputLine(String line);

    void outputFormatLine(String formatLine, Object... args);
}
