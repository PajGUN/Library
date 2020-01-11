package ru.sunbrothers.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sunbrothers.library.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthors_FirstName(String firstName);

    List<Book> findByAuthors_FirstNameAndAuthors_LastName(String firstName, String lastName);

    @Query("select sum(b.totalCount) from Book b")
    Long getSumTotalBook();

    @Query("select sum(b.currentCount) from Book b")
    Long getSumCurrentBook();
}
