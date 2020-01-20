package ru.sunbrothers.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.service.BookService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookDto>> getAllAuthors(
            @PageableDefault(sort = {"bookName"}, direction = Sort.Direction.ASC) Pageable pageable){
        List<BookDto> bookDtos = bookService.getAllBooks(pageable);
        if (bookDtos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok().body(bookDtos);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id){
        BookDto bookDto = bookService.getBookById(id);
        if (bookDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bookDto);
    }

    @GetMapping("/getbyauthorname/{authorName}")
    public ResponseEntity<List<BookDto>> getBookByAuthorname(@PathVariable String authorName){
        String[] name = authorName.split(" ");
        if (name.length == 1) {
            List<BookDto> bookDtos = bookService.getBookByAuthorname(authorName);
            if (bookDtos == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(bookDtos);
        }
        else if (name.length == 2) {
            List<BookDto> bookDtos = bookService.getBookByFirstAndLastName(name[0], name[1]);
            if (bookDtos == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(bookDtos);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody Book book){
        BookDto bookDto = bookService.save(book);
        if (bookDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(bookDto);
    }

    @PostMapping("/addauthor/{authorId}")
    public ResponseEntity<BookDto> addAuthorToBook(@PathVariable Long authorId, @RequestBody Long bookId){
        BookDto bookDto = bookService.addAuthorToBook(authorId, bookId);
        if (bookDto == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(bookDto);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable Long bookId){
        BookDto bookDto = bookService.deleteBook(bookId);
        if (bookDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bookDto);
    }

    @DeleteMapping("/deletewithauthors/{bookId}")
    public ResponseEntity<BookDto> deleteAuthorWithAuthors(@PathVariable Long bookId){
        BookDto bookDto = bookService.deleteBookWithAuthors(bookId);
        if (bookDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(bookDto);
    }

    @PutMapping("/update/{bookId}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long bookId,
                                              @Valid @RequestBody Book book){
        BookDto bookById = bookService.getBookById(bookId);
        if (bookById == null) return ResponseEntity.notFound().build();
        book.setId(bookId);
        BookDto bookDto = bookService.save(book);
        return ResponseEntity.ok(bookDto);
    }
}
