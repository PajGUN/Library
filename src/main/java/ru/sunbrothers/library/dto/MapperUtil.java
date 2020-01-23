package ru.sunbrothers.library.dto;

import ru.sunbrothers.library.dto.report.ClientDtoExpired;
import ru.sunbrothers.library.model.Author;
import ru.sunbrothers.library.model.Book;
import ru.sunbrothers.library.model.Client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapperUtil {
    public static AuthorDto mapToAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setMiddleName(author.getMiddleName());

        Set<Book> books = author.getBooks();
        if (books.isEmpty()) return authorDto;

        Set<BookDto> bookDtos = new HashSet<>();
        for (Book book : books) {
            BookDto bookDto = getBookDto(book);
            bookDtos.add(bookDto);
        }
        authorDto.setBooks(bookDtos);
        return authorDto;
    }

    public static List<AuthorDto> mapToListAuthorDto(List<Author> authorList){
        List<AuthorDto> authorDtos = new ArrayList<>();
        for (Author author : authorList) {
            AuthorDto authorDto = getAuthorDto(author);
            authorDtos.add(authorDto);
        }
        return authorDtos;
    }

    public static BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookName(book.getBookName());
        bookDto.setPublishingHouse(book.getPublishingHouse());
        bookDto.setTotalCount(book.getTotalCount());
        bookDto.setCurrentCount(book.getCurrentCount());

        Set<Author> authors = book.getAuthors();
        if (authors.isEmpty()) return bookDto;

        Set<AuthorDto> authorDtos = new HashSet<>();
        for (Author author : authors){
            AuthorDto authorDto = getAuthorDto(author);
            authorDtos.add(authorDto);
        }
        bookDto.setAuthors(authorDtos);
        return bookDto;
    }

    public static List<BookDto> mapToListBookDto(List<Book> bookList){
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : bookList) {
            BookDto bookDto = getBookDto(book);
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }

//    public static ClientDto mapToClientDto(Client client) {
//        ClientDto clientDto = new ClientDto();
//        clientDto.setId(client.getId());
//        clientDto.setFirstName(client.getFirstName());
//        clientDto.setLastName(client.getLastName());
//        clientDto.setMiddleName(client.getMiddleName());
//        clientDto.setPassportNumber(client.getPassportNumber());
//        clientDto.setBirthday(client.getBirthday());
//        clientDto.setCreated(client.getCreated());
//        clientDto.setTelephoneNumber(client.getTelephoneNumber());
//        clientDto.setEmail(client.getEmail());
//        return clientDto;
//    }


    private static BookDto getBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookName(book.getBookName());
        bookDto.setPublishingHouse(book.getPublishingHouse());
        bookDto.setTotalCount(book.getTotalCount());
        bookDto.setCurrentCount(book.getCurrentCount());
        return bookDto;
    }

    private static AuthorDto getAuthorDto(Author author){
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setMiddleName(author.getMiddleName());
        return authorDto;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ClientDto> T mapToClientDto(Client client) {
        ClientDtoExpired clientDtoExpired = new ClientDtoExpired();
        clientDtoExpired.setId(client.getId());
        clientDtoExpired.setFirstName(client.getFirstName());
        clientDtoExpired.setLastName(client.getLastName());
        clientDtoExpired.setMiddleName(client.getMiddleName());
        clientDtoExpired.setPassportNumber(client.getPassportNumber());
        clientDtoExpired.setBirthday(client.getBirthday());
        clientDtoExpired.setCreated(client.getCreated());
        clientDtoExpired.setTelephoneNumber(client.getTelephoneNumber());
        clientDtoExpired.setEmail(client.getEmail());
        return (T) clientDtoExpired;
    }
}
