package ru.sunbrothers.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.MapperUtil;
import ru.sunbrothers.library.dto.report.BookCountDto;
import ru.sunbrothers.library.dto.report.ClientCountDto;
import ru.sunbrothers.library.dto.report.ClientDtoExpired;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.model.Client;
import ru.sunbrothers.library.repository.BookRepository;
import ru.sunbrothers.library.repository.BorrowerRepository;
import ru.sunbrothers.library.repository.ClientRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReportService {

    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;
    private final BorrowerRepository borrowerRepository;
    private final Environment env;

    @Autowired
    public ReportService(BookRepository bookRepository, ClientRepository clientRepository,
                         BorrowerRepository borrowerRepository, Environment env) {
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.borrowerRepository = borrowerRepository;
        this.env = env;
    }


    @Transactional
    public BookCountDto getAllBookCount() {
        BookCountDto bookCountDto = new BookCountDto();
        bookCountDto.setTotalBookCount(bookRepository.getSumTotalBook());
        bookCountDto.setCurrentBookCount(bookRepository.getSumCurrentBook());
        return bookCountDto;
    }

    @Transactional
    public ClientCountDto getAllClientCount() {
        ClientCountDto clientCountDto = new ClientCountDto();
        clientCountDto.setClientCount(clientRepository.count());
        clientCountDto.setClientCountWithBook(borrowerRepository.getCountClientsWithBook());
        return clientCountDto;
    }

    @Transactional
    public List<ClientDtoExpired> getAllClientsWithExpiredBooks() {
        Long loanTime = Long.parseLong(env.getProperty("loan.time"));
        LocalDate localDate = LocalDate.now().minusDays(loanTime);
        List<Long> clientIds = borrowerRepository.findAllClientIdByLoanDateBefore(localDate);
        List<Client> clients = clientRepository.findAllById(clientIds);

        List<ClientDtoExpired> clientDtosExpired = new ArrayList<>();
        for (Client client : clients) {
            ClientDtoExpired clientDtoExpired = MapperUtil.mapToClientDto(client);

            List<Long> bookIds = borrowerRepository.findBookIdsByClientIdAndLoanDateBefore(client.getId(), localDate);
            List<Book> books = bookRepository.findAllById(bookIds);

            Set<BookDto> bookDtos = new HashSet<>(MapperUtil.mapToListBookDto(books));
            clientDtoExpired.setBooks(bookDtos);
            clientDtosExpired.add(clientDtoExpired);
        }
        return clientDtosExpired;
    }
}
