package ru.baranova.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "comment")
public class Comment implements EntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "comment_author", updatable = false, nullable = false)
    private String author;

    @Column(name = "comment_text", nullable = false)
    private String text;

    @Column(name = "comment_date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Override
    public void print(EntityPrintVisitor visitor) {
        visitor.print(this);
    }
}
