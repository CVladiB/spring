package ru.baranova.spring.dao.reader;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.io.InputStream;

@Data
@Slf4j
public class ReaderDaoFile implements ReaderDao {

    @Nullable
    @Override
    public InputStream getResource(String path) {
        try {
            return getClass().getClassLoader().getResourceAsStream(path);
        } catch (Exception e) {
            log.error("The file was missing");
            return null;
        }
    }

}
