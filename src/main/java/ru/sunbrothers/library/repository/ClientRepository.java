package ru.sunbrothers.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sunbrothers.library.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getClientByTelephoneNumber(Long number);
    Client getClientByPassportNumber(Long number);

}
