package ru.sunbrothers.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sunbrothers.library.model.Author;
import ru.sunbrothers.library.repository.AuthorRepository;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorById(Long id){
        return authorRepository.getOne(id);
    }
    // попробовать с Set
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

}
