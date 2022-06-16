package ru.baranova.spring.dao.reader;

import java.io.InputStream;

public interface ReaderDao {

    InputStream getResource(String path);
}
