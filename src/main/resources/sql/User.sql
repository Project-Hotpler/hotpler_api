Create table login_user(
	id varchar(100) primary key,
    password varchar(100) NOT NULL,
    name varchar(20) NOT NULL,
    join_date Timestamp default now()
	
);

//자동로그인을 위한 컬럼 추가
alter table login_user add column session_id varchar(100) not null default "none";
alter table login_user add column limit_date timestamp;