package ru.sunbrothers.library.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Entity
//@NoArgsConstructor
@Data
//@EqualsAndHashCode(exclude = "books")
//@ToString(exclude = "books")
@Table(name = "client")
public class Client {

    public Client() {
        this.created = LocalDate.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не указано")
    @Size(min = 2, max = 20)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не указана")
    @Size(min = 2, max = 30)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    //Исправить на String и NotBlank c Size
    @NotNull
    @Digits(integer = 10, fraction = 0, message = "Номер паспорта указано не верно")
//    @Pattern(regexp="([0-9]{10})", message = "Номер паспорта указано не верно")
    @Column(name = "passport_num", nullable = false)
    private Long passportNumber;

    @NotNull
    @Past
    @Column(name = "birthday_date", nullable = false)
    private LocalDate birthday;

    @Column(name = "created_date")
    private LocalDate created;

    @Email(message = "Адрес электронной почты не верен")
    @Column(name = "email")
    private String email;

    //Исправить на String
    @NotNull
    @Digits(integer = 11, fraction = 0, message = "Не верный телефонный номер")
    @Column(name = "telephone_num", nullable = false)
    private Long telephoneNumber;

    //address
    //добавить штрафы если были задержки сдачи книг
}
