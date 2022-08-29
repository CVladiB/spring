package ru.baranova.spring.service.app;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
}
