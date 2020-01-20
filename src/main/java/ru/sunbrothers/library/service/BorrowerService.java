package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.AuthorDto;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.model.Author;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.model.Borrower;
import ru.sunbrothers.library.model.Client;
import ru.sunbrothers.library.repository.BookRepository;
import ru.sunbrothers.library.repository.BorrowerRepository;
import ru.sunbrothers.library.repository.ClientRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@EnableTransactionManagement
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
                return null;
            }
            Borrower matchBorrower = borrowerRepository.findBorrowerByClientIdAndBookId(client.getId(), book.getId());
            if (matchBorrower != null) {
                log.info("У клиента с Id - {} на руках уже есть книга - {}.",
                        borrower.getClientId(), book.getBookName());
                return null;
            }
            borrowerRepository.save(borrower);
            book.setCurrentCount(book.getCurrentCount()-1);
            bookRepository.save(book);
            return getBookDtos(borrower.getClientId());
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
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

    public List<BookDto> getAllBooks(Long clientId) {
        return getBookDtos(clientId);
    }

    private List<BookDto> getBookDtos(Long clientId){
        List<BookDto> bookDtos = new ArrayList<>();

        List<Long> bookIds = borrowerRepository.findBookIdsByClientId(clientId);
        List<Book> books = bookRepository.findAllById(bookIds);
        for (Book book : books) {
            BookDto bookDto = getBookDto(book);
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }

    private BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();

        bookDto.setId(book.getId());
        bookDto.setBookName(book.getBookName());
        bookDto.setPublishingHouse(book.getPublishingHouse());
        bookDto.setTotalCount(book.getTotalCount());
        bookDto.setCurrentCount(book.getCurrentCount());

        Set<Author> authors = book.getAuthors();
        if (authors == null) return bookDto;

        Set<AuthorDto> authorList = new HashSet<>();
        for (Author author : authors){
            AuthorDto authorDto = new AuthorDto();

            authorDto.setId(author.getId());
            authorDto.setFirstName(author.getFirstName());
            authorDto.setLastName(author.getLastName());
            authorDto.setMiddleName(author.getMiddleName());
            authorList.add(authorDto);
        }
        bookDto.setAuthors(authorList);
        return bookDto;
    }
}
