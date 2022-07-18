package ru.baranova.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.baranova.spring.domain.Book;

@SpringBootApplication
public class Library {

    public static void main(String[] args) {
        SpringApplication.run(Library.class);
    }
}
