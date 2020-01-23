package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.MapperUtil;
import ru.sunbrothers.library.model.Author;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.repository.AuthorRepository;
import ru.sunbrothers.library.repository.BookRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional
    public List<BookDto> getAllBooks(Pageable pageable) {
        List<BookDto> bookList = new ArrayList<>();
        for (Book book : bookRepository.findAll(pageable)) {
            BookDto bookDto = MapperUtil.mapToBookDto(book);
            bookList.add(bookDto);
        }
        return bookList;
    }

    @Transactional
    public BookDto getBookById(Long id){
        try {
            Book book = bookRepository.getOne(id);
            return MapperUtil.mapToBookDto(book);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public BookDto save(Book book){
        Book b = bookRepository.save(book);
        return MapperUtil.mapToBookDto(b);
    }

    @Transactional
    public BookDto deleteBook(Long id) {
        try {
            Book book = bookRepository.getOne(id);
            for (Author author : book.getAuthors()) {
                author.getBooks().removeIf(b -> b.equals(book));
            }
            book.setAuthors(null);
            BookDto bookDto = MapperUtil.mapToBookDto(book);
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
            BookDto bookDto = MapperUtil.mapToBookDto(book);
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
            return MapperUtil.mapToBookDto(book);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<BookDto> getBookByAuthorname(String firstName) {
        List<Book> books = bookRepository.findByAuthors_FirstName(firstName);
        if (books == null || books.isEmpty()) return Collections.emptyList();
        return MapperUtil.mapToListBookDto(books);
    }

    @Transactional
    public List<BookDto> getBookByFirstAndLastName(String firstName, String lastName) {
        List<Book> books = bookRepository.findByAuthors_FirstNameAndAuthors_LastName(firstName, lastName);
        if (books == null || books.isEmpty()) return Collections.emptyList();
        return MapperUtil.mapToListBookDto(books);
    }

    public BookDto update(Long bookId, Book book) {
        try {
            Book bookTmp = bookRepository.getOne(bookId);
            book.setId(bookId);
            book.setAuthors(bookTmp.getAuthors());
            Book bookSave = bookRepository.save(book);
            return MapperUtil.mapToBookDto(bookSave);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
