create table author
(
	id bigint generated by default as identity
		constraint author_pkey
			primary key,
	first_name varchar(20) not null,
	last_name varchar(30) not null,
	middle_name varchar(30)
);
alter table author owner to postgres;

create table book
(
	id bigint generated by default as identity
		constraint book_pkey
			primary key,
	book_name varchar(255) not null,
	current_count integer not null,
	publishing_house varchar(255),
	total_count integer not null
);
alter table book owner to postgres;

create table book_author
(
	book_id bigint not null
		constraint fkhwgu59n9o80xv75plf9ggj7xn
			references book,
	author_id bigint not null
		constraint fkbjqhp85wjv8vpr0beygh6jsgo
			references author,
	constraint book_author_pkey
		primary key (book_id, author_id)
);
alter table book_author owner to postgres;

create table borrower
(
	id bigint generated by default as identity
		constraint borrower_pkey
			primary key,
	book_id bigint not null,
	client_id bigint not null,
	loan_date date not null
);
alter table borrower owner to postgres;

create table client
(
	id bigint generated by default as identity
		constraint client_pkey
			primary key,
	birthday_date date not null,
	created_date date,
	email varchar(255),
	first_name varchar(20) not null,
	last_name varchar(30) not null,
	middle_name varchar(30),
	passport_num bigint not null,
	telephone_num bigint not null
);
alter table client owner to postgres;

