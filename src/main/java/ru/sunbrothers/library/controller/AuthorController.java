package ru.sunbrothers.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sunbrothers.library.dto.AuthorDto;
import ru.sunbrothers.library.model.Author;
import ru.sunbrothers.library.service.AuthorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;


    @GetMapping("/all")
    public ResponseEntity<List<AuthorDto>> getAllAuthors(){
        List<AuthorDto> authors = authorService.getAllAuthors();
        if (authors.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id){
        AuthorDto author = authorService.getAuthorById(id);
        if (author == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(author);
    }

    @GetMapping("/getbybookname/{bookname}")
    public ResponseEntity<List<AuthorDto>> getAuthorByBookname(@PathVariable String bookname){
        List<AuthorDto> authors = authorService.getAuthorByBookname(bookname);
        if (authors == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(authors);
    }

    @PostMapping("/add")
    public ResponseEntity<AuthorDto> addAuthor(@Valid @RequestBody Author author){
        AuthorDto authorDto = authorService.save(author);
        if (authorDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authorDto);
    }

    @PostMapping("/addbook/{bookId}")
    public ResponseEntity<AuthorDto> addBookToAuthor(@PathVariable Long bookId, @RequestBody Long authorId){
        AuthorDto authorDto = authorService.addBookToAuthor(bookId, authorId);
        if (authorDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(authorDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable Long id){
        AuthorDto authorDto = authorService.deleteAuthor(id);
        if (authorDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(authorDto);
    }

    @DeleteMapping("/deletewithbooks/{id}")
    public ResponseEntity<AuthorDto> deleteAuthorWithBooks(@PathVariable Long id){
        AuthorDto authorDto = authorService.deleteAuthorWithBooks(id);
        if (authorDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(authorDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(@PathVariable Long id, @Valid @RequestBody Author author){
        AuthorDto authorById = authorService.getAuthorById(id);
        if (authorById == null) return ResponseEntity.notFound().build();
        author.setId(id);
        AuthorDto authorDto = authorService.save(author);
        return ResponseEntity.ok(authorDto);
    }
}
