package ru.baranova.spring.domain;

import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

public interface Entity {
    void print(EntityPrintVisitor visitor);
}
