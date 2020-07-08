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

##### BELOW FOR TESTING #####
insert into member(name,surname,email,IDNumber,address, area,home_Number,cell_Number,work_Number,DOB, doe,claimed,paid_joining_fee) values
("Rifaat","Royepen","rroyepen@gmail.com","6901245174084","16 Smartt Rd","Goodwood","0215917721","0711111111","0211111111","1969-01-24","2015-01-01", false, true);

insert into member(name,surname,email,IDNumber,address, area,home_Number,cell_Number,work_Number,DOB, doe,claimed,paid_joining_fee) values
                ("Areeb","Royepen","areeb.royepen@gmail.com","9804025234085","16 Smartt Rd","Goodwood","0215917721","0111111111","0211111111","1998-04-02","2020-01-01", false, true);

INSERT INTO dependant(name, surname, IDNumber,memberID, relationshipID, child,claimed,dob)  values
            ("Nabeel", "Royepen","11158711111",1,1, true, false,"2001-08-04");

INSERT INTO dependant(name, surname, IDNumber,memberID, relationshipID, child,claimed,dob)  values
                    ("Zerina", "Royepen","93158711111",1,3, false, false,"1971-04-24");


