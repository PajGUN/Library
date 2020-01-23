package ru.sunbrothers.library.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sunbrothers.library.dto.AuthorDto;
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
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public AuthorDto getAuthorById(Long id){
        try {
            Author author = authorRepository.getOne(id);
            return MapperUtil.mapToAuthorDto(author);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public List<AuthorDto> getAllAuthors(Pageable pageable){
        List<AuthorDto> authorList = new ArrayList<>();
        for (Author author : authorRepository.findAll(pageable)) {
            AuthorDto authorDto = MapperUtil.mapToAuthorDto(author);
            authorList.add(authorDto);
        }
        return authorList;
    }

    @Transactional
    public AuthorDto save(Author author){
        Author aut = authorRepository.save(author);
        return MapperUtil.mapToAuthorDto(aut);
    }

    @Transactional
    public List<AuthorDto> getAuthorByBookname(String bookName) {
        List<Author> authors = authorRepository.findByBooks_BookName(bookName);
        if(authors == null || authors.isEmpty()) return Collections.emptyList();
        return MapperUtil.mapToListAuthorDto(authors);
    }

    @Transactional
    public AuthorDto deleteAuthor(Long id) {
        try {
            Author author = authorRepository.getOne(id);

            for (Book book : author.getBooks()) {
                book.getAuthors().removeIf(a -> a.equals(author));
            }
            author.setBooks(null);
            AuthorDto authorDto = MapperUtil.mapToAuthorDto(author);
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
            AuthorDto authorDto = MapperUtil.mapToAuthorDto(author);
            authorRepository.deleteById(id);
            return authorDto;
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Transactional
    public AuthorDto addBookToAuthor(Long bookId, Long authorId) {
        try {
            Book book = bookRepository.getOne(bookId);
            Author author = authorRepository.getOne(authorId);
            author.addBook(book);
            book.addAuthor(author);
            authorRepository.save(author);
            return MapperUtil.mapToAuthorDto(author);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public AuthorDto update(Long id, Author author) {
        try {
            Author authorTmp = authorRepository.getOne(id);
            author.setId(id);
            author.setBooks(authorTmp.getBooks());
            Author authorSave = authorRepository.save(author);
            return MapperUtil.mapToAuthorDto(authorSave);
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
