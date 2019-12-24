package ru.sunbrothers.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@ToString(exclude = "authors")
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "book_name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "books")
    @Column(name = "authors", nullable = false)
    private Set<Author> authors;

    @Column(name = "publishing_house")
    private String publishingHouse;

    //Количество книг
    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    //Количество на текущий момент
    @Column(name = "current_count", nullable = false)
    private Integer current_count;


}
