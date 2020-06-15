use BURIALSCHEME;

insert into role (name) values ("admin");
insert into role (name) values ("coordinator");
insert into role (name) values ("clerk");

insert into transaction_type(name) values ("premium");
insert into transaction_type(name) values ("claim");
insert into transaction_type(name) values ("income");
insert into transaction_type(name) values ("expense");

insert into relationship(name) values ("child");
insert into relationship(name) values ("parent");
insert into relationship(name) values ("spouse");
insert into relationship(name) values ("other");

insert into member(name,surname,email,IDNumber,address, area,home_Number,cell_Number,work_Number,DOB, doe,claimed,paid_joining_fee) values
                ("Areeb","Royepen","a@g.c","1111111111111","16 Smartt Rd","Goodwood","1111111111","1111111111","1111111111","1998-04-02","2020-01-01", false, false);

insert into premium(amount, date, transaction_typeid,memberid) values (100,"2020-04-02",1,1);

insert into claim(amount, memberid, transaction_typeid,burial_place,claim_date) values (100, 1, 2,"Goodwood","2020-05-05");




