package ru.sunbrothers.library.dto.report;

import lombok.Getter;
import lombok.Setter;
import ru.sunbrothers.library.dto.ClientDto;

import java.util.Set;

@Getter
@Setter
public class ClientBookExpiredDto {
    private Set<ClientDto> clientDtos;
}
