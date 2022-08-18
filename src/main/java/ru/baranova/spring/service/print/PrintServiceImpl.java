package ru.baranova.spring.service.print;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.baranova.spring.model.EntityObject;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Service
@RequiredArgsConstructor
public class PrintServiceImpl implements PrintService {
    private final EntityPrintVisitor entityPrintVisitor;

    @Override
    public void printEntity(EntityObject entity) {
        entity.print(entityPrintVisitor);
    }
}
