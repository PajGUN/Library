package ru.sunbrothers.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.ClientDto;
import ru.sunbrothers.library.model.Borrower;
import ru.sunbrothers.library.model.Client;
import ru.sunbrothers.library.service.BorrowerService;
import ru.sunbrothers.library.service.ClientService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private BorrowerService borrowerService;

    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAllClients(){
        List<ClientDto> clientDtos = clientService.getAllClients();
        if (clientDtos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(clientDtos);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id){
        ClientDto clientDto = clientService.getClientById(id);
        if (clientDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping("/getbyphonenumber/{phoneNumber}")
    public ResponseEntity<ClientDto> getClientByPhoneNumber(@PathVariable Long phoneNumber){
        ClientDto clientDto = clientService.getClientByPhonenumber(phoneNumber);
        if (clientDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clientDto);
    }

    @GetMapping("/getbypassportnumber/{passportNumber}")
    public ResponseEntity<ClientDto> getClientByPassportNumber(@PathVariable Long passportNumber){
        ClientDto clientDto = clientService.getClientByPassportnumber(passportNumber);
        if (clientDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(clientDto);
    }

    @PostMapping("/add")
    public ResponseEntity<ClientDto> addClient(@Valid @RequestBody Client client){
        ClientDto clientDto = clientService.save(client);
        if (clientDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(clientDto);
    }
//
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ClientDto> deleteClient(@PathVariable Long id){
        ClientDto clientDto = clientService.delete(id);
        if (clientDto == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return ResponseEntity.ok(clientDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody Client client){
        ClientDto clientDtoById = clientService.getClientById(id);
        if (clientDtoById == null) return ResponseEntity.notFound().build();
        client.setId(id);
        ClientDto clientDto = clientService.save(client);
        return ResponseEntity.ok(clientDto);
    }

    @PostMapping("/addbook/{bookId}")
    public ResponseEntity<List<BookDto>> addBook(@PathVariable Long bookId, @RequestBody Long clientId){
        Borrower borrower = new Borrower(bookId, clientId);
        List<BookDto> bookDtos = borrowerService.addBookToClient(borrower);
        if (bookDtos == null || bookDtos.isEmpty()) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return ResponseEntity.ok(bookDtos);
    }

    @DeleteMapping("/deletebook/{bookId}")
    public ResponseEntity<List<BookDto>> deleteBook(@PathVariable Long bookId, @RequestBody Long clientId){
        List<BookDto> bookDtos = borrowerService.deleteBookFromClient(bookId, clientId);
        if (bookDtos == null) return new ResponseEntity<>(HttpStatus.CONFLICT);
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("/getbooks/{clientId}")
    public ResponseEntity<List<BookDto>> getBooks(@PathVariable Long clientId){
        List<BookDto> bookDtos = borrowerService.getAllBooks(clientId);
        if (bookDtos == null || bookDtos.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bookDtos);
    }
}
