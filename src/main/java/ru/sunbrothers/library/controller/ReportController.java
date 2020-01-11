package ru.sunbrothers.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sunbrothers.library.dto.report.BookCountDto;
import ru.sunbrothers.library.dto.report.ClientCountDto;
import ru.sunbrothers.library.dto.report.ClientDtoExpired;
import ru.sunbrothers.library.service.ReportService;

import java.util.List;

@Controller
@RequestMapping("api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/bookcount")
    public ResponseEntity<BookCountDto> getAllBookCount(){
        return ResponseEntity.ok(reportService.getAllBookCount());
    }

    @GetMapping("/clientcount")
    public ResponseEntity<ClientCountDto> getAllClientCount(){
        return ResponseEntity.ok(reportService.getAllClientCount());
    }

    @GetMapping("/expiredbookclients")
    public ResponseEntity<List<ClientDtoExpired>> getAllClientsWithExpiredBooks(){
        List<ClientDtoExpired> clientDtos = reportService.getAllClientsWithExpiredBooks();
        return ResponseEntity.ok(clientDtos);
    }
}
