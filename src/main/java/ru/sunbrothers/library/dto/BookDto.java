package ru.sunbrothers.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class BookDto {
    private Long id;

    private String bookName;

    private String publishingHouse;

    private Integer totalCount;

    private Integer currentCount;

    private Set<AuthorDto> authors;
}
