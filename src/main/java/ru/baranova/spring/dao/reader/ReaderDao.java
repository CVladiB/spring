package ru.baranova.spring.dao.reader;

import java.io.IOException;
import java.io.InputStream;

public interface ReaderDao {
/**
 * Преобразование входных данных в поток,
 * */
    InputStream getResource(String path);
}
