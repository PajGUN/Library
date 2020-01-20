package ru.sunbrothers.library.service.Scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sunbrothers.library.dto.AuthorDto;
import ru.sunbrothers.library.dto.BookDto;
import ru.sunbrothers.library.dto.report.ClientDtoExpired;
import ru.sunbrothers.library.service.ReportService;

@Service
@Slf4j
public class SchedulerService {

    private final EmailService emailService;
    private final ReportService reportService;

    @Autowired
    public SchedulerService(EmailService emailService, ReportService reportService) {
        this.emailService = emailService;
        this.reportService = reportService;
    }

    @Scheduled(cron = "${cron.work}")
    public void sendMsgToClients(){
        for (ClientDtoExpired client : reportService.getAllClientsWithExpiredBooks()) {
            if (client.getEmail() == null) continue;
            int count = 1;
            StringBuilder sb = new StringBuilder();
            for (BookDto book : client.getBooks()) {
                sb.append("\t").append(count).append("). Название книги - ").append(book.getBookName()).append("\n")
                        .append("\t\tАвтор - ");
                for (AuthorDto author : book.getAuthors()) {
                    sb.append(author.getFirstName()).append(" ").append(author.getLastName()).append(", ");
                }
                sb.deleteCharAt(sb.length()-2);
                sb.append("\n");
                count++;
            }
            String text = "\tУважаемый(ая) " + client.getFirstName() + "! Просьба вернуть в библиотеку имени Герцена " +
                    "следующие книги:\n" + sb.toString() + "\n\n\n\tЖдём вас ежедневно по адресу пр.Кирова д.9 с 10:00 до 21:00" ;
            emailService.sendMail(client.getEmail(),"Где книги Лебовски?",text);
            log.info("sendMsg to " + client.getFirstName() + " " + client.getLastName());
        }
    }
}
