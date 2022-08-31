package ru.baranova.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "author", uniqueConstraints = {@UniqueConstraint(columnNames = {"author_surname", "author_name"})})
public class Author implements EntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "author_surname", nullable = false)
    private String surname;

    @Column(name = "author_name", nullable = false)
    private String name;
}
