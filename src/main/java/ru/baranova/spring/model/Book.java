package ru.baranova.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.baranova.spring.service.print.visitor.EntityPrintVisitor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "book", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"book_title", "author_id"})})
public class Book implements EntityObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "book_title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Author.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre"
            , joinColumns = @JoinColumn(name = "book_id")
            , inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genreList;

    @OneToMany(targetEntity = Comment.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private List<Comment> commentList;

    @Override
    public void print(EntityPrintVisitor visitor) {
        visitor.print(this);
    }
}
