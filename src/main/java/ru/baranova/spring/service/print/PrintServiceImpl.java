package ru.baranova.spring.service.print;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.dao.output.OutputDao;
import ru.baranova.spring.domain.Entity;
import ru.baranova.spring.service.print.visitor.EntityToStringVisitor;

@Service
@RequiredArgsConstructor
public class PrintServiceImpl implements PrintService {
    private final EntityToStringVisitor entityToStringVisitorImpl;
    private final OutputDao outputDaoConsole;

    @Override
    public void printEntity(Entity entity) {
        String str = entity.accept(entityToStringVisitorImpl);
        outputDaoConsole.output(str);
    }
}
