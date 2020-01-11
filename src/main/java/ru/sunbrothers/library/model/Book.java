package ru.sunbrothers.library.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"authors"})
@ToString(exclude = {"authors"})
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Отсутствует название книги")
    @Column(name = "book_name", nullable = false)
    private String bookName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Column(name = "authors")
    private Set<Author> authors;

    @Column(name = "publishing_house")
    private String publishingHouse;

    //Количество книг
    @NotNull(message = "Общее количество книг не указано")
    @Digits(integer = 3, fraction = 0)
    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    //Количество книг на текущий момент
    @NotNull(message = "Количество книг в наличии не указано")
    @Digits(integer = 3, fraction = 0)
    @Column(name = "current_count", nullable = false)
    private Integer currentCount;

    public void addAuthor(Author author){
        authors.add(author);
    }


}
