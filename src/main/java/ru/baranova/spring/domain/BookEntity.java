package ru.baranova.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class BookEntity implements Entity {
    private Integer id;
    private String title;
    private Integer authorId;
    private List<Integer> genreListId;

    @Override
    public void print(EntityPrintVisitor visitor) {
        visitor.print(this);
    }
}
