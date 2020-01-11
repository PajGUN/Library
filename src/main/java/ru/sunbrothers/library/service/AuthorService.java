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
import java.util.*;

@Service
@Slf4j
@EnableTransactionManagement
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public AuthorDto getAuthorById(Long id){
        try {
            Author author = authorRepository.getOne(id);
            return getAuthorDto(author);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<AuthorDto> getAllAuthors(){
        List<AuthorDto> authorList = new ArrayList<>();
        for (Author author : authorRepository.findAll()) {
            AuthorDto authorDto = getAuthorDto(author);
            authorList.add(authorDto);
        }
        return authorList;
    }

    @Transactional
    public AuthorDto save(Author author){
        Author aut = authorRepository.save(author);
        return getAuthorDto(aut);
    }

    @Transactional
    public List<AuthorDto> getAuthorByBookname(String bookName) {
        List<Author> authors = authorRepository.findByBooks_BookName(bookName);
        if(authors == null || authors.isEmpty()) return null;
        List<AuthorDto> authorsDto = new ArrayList<>();
        for (Author author : authors) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(author.getId());
            authorDto.setFirstName(author.getFirstName());
            authorDto.setLastName(author.getLastName());
            authorDto.setMiddleName(author.getMiddleName());
            authorsDto.add(authorDto);
        }
        return authorsDto;
    }

    @Transactional
    public AuthorDto deleteAuthor(Long id) {
        try {
            Author author = authorRepository.getOne(id);

            for (Book book : author.getBooks()) {
                book.getAuthors().removeIf(a -> a.equals(author));
            }
            author.setBooks(null);
            AuthorDto authorDto = getAuthorDto(author);
            authorRepository.delete(author);
            return authorDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public AuthorDto deleteAuthorWithBooks(Long id) {
        try {
            Author author = authorRepository.getOne(id);
            AuthorDto authorDto = getAuthorDto(author);
            authorRepository.deleteById(id);
            return authorDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private AuthorDto getAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setMiddleName(author.getMiddleName());

        Set<Book> books = author.getBooks();
        if (books == null) return authorDto;

        Set<BookDto> bookList = new HashSet<>();
        for (Book book : books) {
            BookDto bookDto = new BookDto();

            bookDto.setId(book.getId());
            bookDto.setBookName(book.getBookName());
            bookDto.setPublishingHouse(book.getPublishingHouse());
            bookDto.setTotalCount(book.getTotalCount());
            bookDto.setCurrentCount(book.getCurrentCount());
            bookList.add(bookDto);
        }
        authorDto.setBooks(bookList);
        return authorDto;
    }

    @Transactional
    public AuthorDto addBookToAuthor(Long bookId, Long authorId) {
        try {
            Book book = bookRepository.getOne(bookId);
            Author author = authorRepository.getOne(authorId);
            author.addBook(book);
            book.addAuthor(author);
            authorRepository.save(author);
            return getAuthorDto(author);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
