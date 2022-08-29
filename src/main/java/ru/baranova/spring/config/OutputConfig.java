package ru.baranova.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.OutputStream;

@Configuration
public class OutputConfig {
    @Bean
    public OutputStream systemOutputStream() {
        return System.out;
    }
}
