package ru.baranova.spring.dao.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@Component
public class ReaderDaoFile implements ReaderDao {

    private final ApplicationContext ctx;

    @Nullable
    @Override
    public InputStream getResource(String path) {
        try {
            log.info(path);
            return ctx.getResource(path).getInputStream();
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return null;
        }
    }
}
