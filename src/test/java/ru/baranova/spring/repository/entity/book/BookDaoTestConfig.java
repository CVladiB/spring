package ru.baranova.spring.repository.entity.book;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@TestConfiguration
public class BookDaoTestConfig {
    @Getter
    private Date dateWithoutTime;

    @Bean
    public BookDao bookDao(EntityManager em) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateWithoutTime = sdf.parse(sdf.format(new Date()));
        return new BookDaoJpa(em);
    }

}
