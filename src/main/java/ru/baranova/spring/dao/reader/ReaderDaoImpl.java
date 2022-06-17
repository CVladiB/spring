package ru.baranova.spring.dao.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

import java.io.InputStream;

/**
 * 2я реализация работы с ресурсами (метод Spring)
 */
@Slf4j
public class ReaderDaoImpl implements ReaderDao {
    @Nullable
    @Override
    public InputStream getResource(String path) {
        try {
            return new ClassPathResource(path).getInputStream();
        } catch (Exception e) {
            log.error("The file was missing");
            return null;
        }
    }
}
