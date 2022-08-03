package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class Author implements Entity {
    private Integer id;
    private String surname;
    private String name;


    @Override
    public void print(EntityPrintVisitor visitor) {
        visitor.print(this);
    }
}
