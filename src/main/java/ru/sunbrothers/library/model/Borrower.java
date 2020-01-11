package ru.sunbrothers.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "borrower")
@Data
@NoArgsConstructor
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
