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
import ru.sunbrothers.library.repository.AuthorRepository;
import ru.sunbrothers.library.repository.BookRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@EnableTransactionManagement
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public List<BookDto> getAllBooks() {
        List<BookDto> bookList = new ArrayList<>();
        for (Book book : bookRepository.findAll()) {
            BookDto bookDto = getBookDto(book);
            bookList.add(bookDto);
        }
        return bookList;
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

    @Transactional
    public BookDto getBookById(Long id){
        try {
            Book book = bookRepository.getOne(id);
            return getBookDto(book);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public BookDto save(Book book){
        Book b = bookRepository.save(book);
        return getBookDto(b);
    }

    @Transactional
    public BookDto deleteBook(Long id) {
        try {
            Book book = bookRepository.getOne(id);
            for (Author author : book.getAuthors()) {
                author.getBooks().removeIf(b -> b.equals(book));
            }
            book.setAuthors(null);
            BookDto bookDto = getBookDto(book);
            bookRepository.delete(book);
            return bookDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public BookDto deleteBookWithAuthors(Long id) {
        try {
            Book book = bookRepository.getOne(id);
            BookDto bookDto = getBookDto(book);
            bookRepository.delete(book);
            return bookDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public BookDto addAuthorToBook(Long authorId, Long bookId) {
        try {
            Book book = bookRepository.getOne(bookId);
            Author author = authorRepository.getOne(authorId);
            book.addAuthor(author);
            bookRepository.save(book);
            return getBookDto(book);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<BookDto> getBookByAuthorname(String firstName) {
        System.out.println(1);
        List<Book> books = bookRepository.findByAuthors_FirstName(firstName);

        return getBookDtos(books);
    }

    @Transactional
    public List<BookDto> getBookByFirstAndLastName(String firstName, String lastName) {
        System.out.println(2);
        List<Book> books = bookRepository.findByAuthors_FirstNameAndAuthors_LastName(firstName, lastName);

        return getBookDtos(books);
    }

    private List<BookDto> getBookDtos(List<Book> books) {
        if (books == null || books.isEmpty()) return null;

        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();
            bookDto.setId(book.getId());
            bookDto.setBookName(book.getBookName());
            bookDto.setPublishingHouse(book.getPublishingHouse());
            bookDto.setTotalCount(book.getTotalCount());
            bookDto.setCurrentCount(book.getCurrentCount());

            bookDtos.add(bookDto);
        }
        return bookDtos;
    }
}
