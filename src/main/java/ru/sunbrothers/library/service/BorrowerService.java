package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.MapperUtil;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.model.Borrower;
import ru.sunbrothers.library.model.Client;
import ru.sunbrothers.library.repository.BookRepository;
import ru.sunbrothers.library.repository.BorrowerRepository;
import ru.sunbrothers.library.repository.ClientRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public BorrowerService(BorrowerRepository borrowerRepository, BookRepository bookRepository, ClientRepository clientRepository) {
        this.borrowerRepository = borrowerRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional
    public List<BookDto> addBookToClient(Borrower borrower) {
        try {
            Book book = bookRepository.getOne(borrower.getBookId());
            Client client = clientRepository.getOne(borrower.getClientId());
            if (book.getCurrentCount().equals(0)) {
                log.info("Книги - {} нет в наличии! Количество экземпляров - {}.",
                        book.getBookName(), book.getTotalCount());
                return Collections.emptyList();
            }
            Borrower matchBorrower = borrowerRepository.findBorrowerByClientIdAndBookId(client.getId(), book.getId());
            if (matchBorrower != null) {
                log.info("У клиента с Id - {} на руках уже есть книга - {}.",
                        borrower.getClientId(), book.getBookName());
                return Collections.emptyList();
            }
            borrowerRepository.save(borrower);
            book.setCurrentCount(book.getCurrentCount()-1);
            bookRepository.save(book);
            return getBookDtos(borrower.getClientId());
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Transactional
    public List<BookDto> deleteBookFromClient(Long bookId, Long clientId) {
        try {
            Borrower matchBorrower = borrowerRepository.findBorrowerByClientIdAndBookId(clientId, bookId);
            if (matchBorrower == null) {
                log.info("У клиента с Id - {} на руках нет книги с Id - {}.",
                        clientId, bookId);
                return null;
            }
            Book book = bookRepository.getOne(bookId);
            book.setCurrentCount(book.getCurrentCount()+1);
            bookRepository.save(book);
            borrowerRepository.delete(matchBorrower);

            return getBookDtos(clientId);
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage());
            return null;
        }
        // если был просрок добавить клиенту штраф
    }

    @Transactional
    public List<BookDto> getAllBooks(Long clientId) {
        return getBookDtos(clientId);
    }

    private List<BookDto> getBookDtos(Long clientId){
        List<BookDto> bookDtos = new ArrayList<>();

        List<Long> bookIds = borrowerRepository.findBookIdsByClientId(clientId);
        List<Book> books = bookRepository.findAllById(bookIds);
        for (Book book : books) {
            BookDto bookDto = MapperUtil.mapToBookDto(book);
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }
}
