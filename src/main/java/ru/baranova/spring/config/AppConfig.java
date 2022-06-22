package ru.baranova.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.OutputStream;

@Configuration
public class AppConfig {

    @Bean
    public InputStream systemInputStream(){
        return System.in;
    }

    @Bean
    public OutputStream systemOutputStream(){
        return System.out;
    }
}
