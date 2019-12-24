package ru.sunbrothers.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sunbrothers.library.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
