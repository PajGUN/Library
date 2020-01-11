package ru.sunbrothers.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AuthorDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private Set<BookDto> books;
}
