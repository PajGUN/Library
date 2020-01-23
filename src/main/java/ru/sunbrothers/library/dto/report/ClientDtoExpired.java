package ru.sunbrothers.library.dto.report;

import lombok.Getter;
import lombok.Setter;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.ClientDto;

import java.util.Set;

@Getter
@Setter
public class ClientDtoExpired extends ClientDto {
    private Set<BookDto> books;
}
