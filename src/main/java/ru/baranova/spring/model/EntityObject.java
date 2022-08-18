package ru.baranova.spring.model;

import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

public interface EntityObject {
    void print(EntityPrintVisitor visitor);
}
