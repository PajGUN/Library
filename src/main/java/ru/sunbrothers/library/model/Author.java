package ru.sunbrothers.library.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "books")
@ToString(exclude = "books")
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authors", cascade = CascadeType.ALL)
    @Column(name = "books")
    private Set<Book> books;

    public void addBook(Book book){
        books.add(book);
    }
}
