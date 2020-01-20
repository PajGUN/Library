package ru.sunbrothers.library.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrower")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "client_id", nullable = false)
    Long clientId;

    @Column(name = "book_id", nullable = false)
    Long bookId;

    @Column(name = "loan_date", nullable = false)
    LocalDate loanDate;

    public Borrower(Long bookId, Long clientId) {
        this.bookId = bookId;
        this.clientId = clientId;
        this.loanDate = LocalDate.now();
    }
}
