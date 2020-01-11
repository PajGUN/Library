package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sunbrothers.library.dto.ClientDto;
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

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BorrowerRepository borrowerRepository;

    public List<ClientDto> getAllClients() {
        List<ClientDto> clientDtos = new ArrayList<>();
        for (Client client : clientRepository.findAll()) {
            ClientDto clientDto = getClientDto(client);
            clientDtos.add(clientDto);
        }
        return clientDtos;
    }

    private ClientDto getClientDto(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setMiddleName(client.getMiddleName());
        clientDto.setPassportNumber(client.getPassportNumber());
        clientDto.setBirthday(client.getBirthday());
        clientDto.setCreated(client.getCreated());
        clientDto.setTelephoneNumber(client.getTelephoneNumber());
        clientDto.setEmail(client.getEmail());
        return clientDto;
    }

    public ClientDto getClientById(Long id) {
        try {
            Client client = clientRepository.getOne(id);
            return getClientDto(client);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public ClientDto getClientByPhonenumber(Long phoneNumber) {
        Client client = clientRepository.getClientByTelephoneNumber(phoneNumber);
        if (client == null) return null;
        return getClientDto(client);
    }

    public ClientDto getClientByPassportnumber(Long passportNumber) {
        Client client = clientRepository.getClientByPassportNumber(passportNumber);
        if (client == null) return null;
        return getClientDto(client);
    }

    public ClientDto save(Client client) {
        Client c = clientRepository.save(client);
        return getClientDto(c);
    }

    public ClientDto delete(Long id) {
        try {
            List<Borrower> borrowers = borrowerRepository.findBorrowersByClientId(id);
            if (!borrowers.isEmpty()) {
                log.info("У клиента с Id - {} имеются не сданные книги!", id);
                return null;
            }

            Client client = clientRepository.getOne(id);
            ClientDto clientDto = getClientDto(client);
            clientRepository.delete(client);
            return clientDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
