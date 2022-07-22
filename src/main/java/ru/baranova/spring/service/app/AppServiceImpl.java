package ru.baranova.spring.service.app;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class AppServiceImpl implements AppService {
    @Setter
    @Autowired
    private ApplicationContext context;

    @Override
    public void stopApplication() {
        SpringApplication.exit(context);
    }
}
