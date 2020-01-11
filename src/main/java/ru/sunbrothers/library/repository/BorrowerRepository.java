package ru.sunbrothers.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sunbrothers.library.model.Borrower;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    @Query("select b.bookId from Borrower b where b.clientId = ?1")
    List<Long> findBookIdsByClientId(Long clientId);


    @Query("select b.bookId from Borrower b where b.clientId = ?1 and b.loanDate < ?2")
    List<Long> findBookIdsByClientIdAndLoanDateBefore(Long clientId, LocalDate localDate);

    Borrower findBorrowerByClientIdAndBookId(Long clientId, Long bookId);

    @Query("select count(distinct b.clientId) from Borrower b")
    Long getCountClientsWithBook();

    List<Borrower> findBorrowersByClientId(Long clientId);

    @Query("select distinct b.clientId from Borrower b where b.loanDate < ?1")
    List<Long> findAllClientIdByLoanDateBefore(LocalDate localDate);
}
