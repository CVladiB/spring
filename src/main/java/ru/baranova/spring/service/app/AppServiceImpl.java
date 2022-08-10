package ru.baranova.spring.service.app;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.BusinessConstants;

import java.util.function.Supplier;

@Slf4j
@Service
public class AppServiceImpl implements AppService {
    @Setter
    @Autowired
    private ApplicationContext context;

    @Override
    public int stopApplication() {
        return SpringApplication.exit(context);
    }

    @Override
    @Nullable
    public <T> T evaluate(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (DataAccessException e) {
            log.info(BusinessConstants.EntityServiceLog.WARNING_NEED_ADMINISTRATOR);
            return null;
        }
    }
}
