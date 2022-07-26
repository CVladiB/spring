package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Genre implements Entity {
    private Integer id;
    private String name;
    private String description;

    @Override
    public void print(EntityPrintVisitor visitor) {
        visitor.print(this);
    }
}

