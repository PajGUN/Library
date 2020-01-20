package ru.sunbrothers.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "books")
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "Имя не указано")
    @Size(min = 2, max = 20)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не указана")
    @Size(min = 2, max = 30)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors", cascade = CascadeType.ALL)
    @Column(name = "books")
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book){
        books.add(book);
    }
}
