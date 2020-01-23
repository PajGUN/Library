package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.ClientDto;
import ru.sunbrothers.library.dto.MapperUtil;
import ru.sunbrothers.library.model.Borrower;
import ru.sunbrothers.library.model.Client;
import ru.sunbrothers.library.repository.BorrowerRepository;
import ru.sunbrothers.library.repository.ClientRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final BorrowerRepository borrowerRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, BorrowerRepository borrowerRepository) {
        this.clientRepository = clientRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Transactional
    public List<ClientDto> getAllClients(Pageable pageable) {
        List<ClientDto> clientDtos = new ArrayList<>();
        for (Client client : clientRepository.findAll(pageable)) {
            ClientDto clientDto = MapperUtil.mapToClientDto(client);
            clientDtos.add(clientDto);
        }
        return clientDtos;
    }

    @Transactional
    public ClientDto getClientById(Long id) {
        try {
            Client client = clientRepository.getOne(id);
            return MapperUtil.mapToClientDto(client);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public ClientDto getClientByPhonenumber(Long phoneNumber) {
        Client client = clientRepository.getClientByTelephoneNumber(phoneNumber);
        if (client == null) return null;
        return MapperUtil.mapToClientDto(client);
    }

    @Transactional
    public ClientDto getClientByPassportnumber(Long passportNumber) {
        Client client = clientRepository.getClientByPassportNumber(passportNumber);
        if (client == null) return null;
        return MapperUtil.mapToClientDto(client);
    }

    @Transactional
    public ClientDto save(Client client) {
        Client c = clientRepository.save(client);
        return MapperUtil.mapToClientDto(c);
    }

    @Transactional
    public ClientDto delete(Long id) {
        try {
            List<Borrower> borrowers = borrowerRepository.findBorrowersByClientId(id);
            if (!borrowers.isEmpty()) {
                log.info("У клиента с Id - {} имеются не сданные книги!", id);
                return null;
            }
            Client client = clientRepository.getOne(id);
            ClientDto clientDto = MapperUtil.mapToClientDto(client);
            clientRepository.delete(client);
            return clientDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
