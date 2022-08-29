package ru.baranova.spring.service.print.visitor;

import lombok.Getter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.dao.output.OutputDaoConsole;
import ru.baranova.spring.service.app.CheckService;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@TestConfiguration
public class EntityPrintVisitorImplTestConfig {
    @Getter
    private ByteArrayOutputStream out;
    @Getter
    private PrintWriter writer;
    @MockBean
    private CheckService checkService;
    @SpyBean
    private EntityPrintVisitor entityPrintVisitor;

    @Bean
    public OutputDao outputDao() {
        out = new ByteArrayOutputStream();
        writer = new PrintWriter(out, true);
        return new OutputDaoConsole(out);
    }

    @Bean
    public EntityPrintVisitor printer(OutputDao outputDao, CheckService checkService) {
        return new EntityPrintVisitorImpl(outputDao, checkService);
    }
}
