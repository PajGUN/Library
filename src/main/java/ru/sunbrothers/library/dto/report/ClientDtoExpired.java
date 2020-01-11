package ru.sunbrothers.library.dto.report;

import lombok.Getter;
import lombok.Setter;
import ru.sunbrothers.library.dto.BookDto;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class ClientDtoExpired {
    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private Long passportNumber;

    private LocalDate birthday;

    private LocalDate created;

    private String email;

    private Long telephoneNumber;

    private Set<BookDto> books;
}
