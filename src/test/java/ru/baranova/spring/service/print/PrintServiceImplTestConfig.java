package ru.baranova.spring.service.print;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.model.Author;
import ru.baranova.spring.model.Book;
import ru.baranova.spring.model.Genre;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@TestConfiguration
public class PrintServiceImplTestConfig {
    @MockBean
    private EntityPrintVisitor printer;
    @MockBean
    private Author testAuthor;
    @MockBean
    private Genre testGenre;
    @MockBean
    private Book testBook;


    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;
    @Getter
    private Date dateWithoutTime;

    @Bean
    public PrintService printServiceImpl(EntityPrintVisitor entityPrintVisitorImpl) throws ParseException {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateWithoutTime = sdf.parse(sdf.format(new Date()));
        return new PrintServiceImpl(entityPrintVisitorImpl);
    }


    public Author testAuthor() {
        return new Author(1, "SurnameTest", "NameTest");
    }
}
