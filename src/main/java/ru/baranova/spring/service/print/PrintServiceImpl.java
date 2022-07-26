package ru.baranova.spring.service.print;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.domain.Entity;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Service
@RequiredArgsConstructor
public class PrintServiceImpl implements PrintService {
    private final EntityPrintVisitor entityPrintVisitorImpl;


    @Override
    public void printEntity(Entity entity) {
        entity.print(entityPrintVisitorImpl);
    }
}
