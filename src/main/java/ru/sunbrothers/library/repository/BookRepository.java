package ru.sunbrothers.library.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sunbrothers.library.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
}
