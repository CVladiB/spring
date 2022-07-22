package ru.baranova.spring.domain;

import ru.baranova.spring.service.print.visitor.EntityToStringVisitor;

public interface Entity {
    String accept(EntityToStringVisitor visitor);
}
