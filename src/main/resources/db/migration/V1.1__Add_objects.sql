insert into client
    (birthday_date, created_date, email, first_name,
    last_name, middle_name, passport_num, telephone_num)
    values ('1984-02-23', '2020-01-03', 'zip02@gmail.com',
        'Sergey', 'Markov', 'Michailovich',
        3306532688, 79229993455),
       ('1985-04-13', '2020-01-03', 'zip01@yandex.ru',
        'Pavel', 'Sitnikov', 'Vladimirovich',
        3304556688, 79213662244);

--

insert into book (book_name, current_count, publishing_house, total_count)
    values ('Эпоха', '1', null , '1');
insert into author (first_name, last_name, middle_name)
    values ('Фёдор', 'Достоевский', 'Михайлович');
insert into author (first_name, last_name, middle_name)
    values ('Михаил', 'Достоевский', 'Михайлович');
insert into book_author (book_id, author_id)
    values (1, 1);
insert into book_author (book_id, author_id)
    values (1, 2);

insert into author (first_name, last_name, middle_name)
    values ('Нил', 'Стивенсон', null);
insert into book (book_name, current_count, publishing_house, total_count)
    values ('Семиевие', 2, 'fanzone', 2);
insert into book_author (book_id, author_id)
    values (2, 3);

insert into book (book_name, current_count, publishing_house, total_count)
    values ('Преступление и наказание', 3, 'Наука', 3);
insert into book_author (book_id, author_id)
    values (3, 1);

