package ru.sunbrothers.library.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCountDto {

    private Long totalBookCount;
    private Long currentBookCount;
}
