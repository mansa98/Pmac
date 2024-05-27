CREATE TABLE user_tbl (
	id number(10) primary key,
	name varchar(100) not null,
	email varchar(100) not null,
	role varchar(20),
	created_date date,
	modified_date date,
    provider varchar2(30 char),
    nickname varchar2(50 char),
    is_user number
);

CREATE SEQUENCE user_tbl_seq 
START WITH 1 
INCREMENT BY 1;