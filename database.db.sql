BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "appointments" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"date"	TEXT,
	"time"	TEXT,
	"patient_id"	INTEGER,
	"doctor_id"	INTEGER,
	"type"	TEXT,
	"office_id"	INTEGER,
	"anamnesis"	TEXT,
	"diagnosis"	TEXT,
	"recommendation"	TEXT
);
CREATE TABLE IF NOT EXISTS "doctors" (
	"id"	INTEGER,
	"firstName"	TEXT,
	"lastName"	TEXT,
	"JMBG"	TEXT,
	"DOB"	TEXT,
	"POB"	TEXT,
	"address"	TEXT,
	"email"	TEXT,
	"DOE"	TEXT,
	"specialty"	TEXT,
	"office_id"	INTEGER NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "patients" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"firstName"	TEXT,
	"lastName"	TEXT,
	"JMBG"	TEXT,
	"gender"	TEXT,
	"DOB"	TEXT,
	"POB"	TEXT,
	"address"	TEXT,
	"status"	TEXT,
	"email"	TEXT,
	"office_id"	INTEGER
);
CREATE TABLE IF NOT EXISTS "offices" (
	"id"	INTEGER PRIMARY KEY AUTOINCREMENT,
	"name"	TEXT,
	"address"	TEXT,
	"username"	TEXT,
	"password"	TEXT
);
INSERT INTO "doctors" VALUES (1,'Emina','Letic','123456789','1995-01-01','Sarajevo','Antuna Hangija 17','emina95@gmail.com','1999-01-01','Hirurg',1);
INSERT INTO "doctors" VALUES (2,'Vildana','Beglerovic','147852369','1999-07-07','Sarajevo','Donji Hotonj 21','vildanabeglerovic@gmail.com','1999-07-06','',1);
INSERT INTO "doctors" VALUES (3,'Sanela','Selmanovic','987456123','1956-10-12','Tuzla','Asima Ferhatovica 59','','1982-07-07','Pedijatar',10);
INSERT INTO "doctors" VALUES (4,'Mirela','Arapovic','123456789','1972-01-01','Zenica','Marcela Snajdera 25','mirelakurtovic@gmail.com','1992-01-01','Pedijatar',1);
INSERT INTO "doctors" VALUES (6,'Lejla','Buturovic','987654321','1999-03-03','Sarajevo','Sutjeska 1','lejlabuturovic@gmail.com','2020-03-12','Hirurg',14);
INSERT INTO "doctors" VALUES (7,'Nerma','Terzo','13456789','1978-01-01','Sarajevo','Bolnicka 23','','2020-01-01','',15);
INSERT INTO "patients" VALUES (1,'Kerima','Dedovic','123456789','FEMALE','1942-07-09','Sarajevo','Donji Hotonj 21','RETIREE','amiladedic1@gmail.com',1);
INSERT INTO "patients" VALUES (14,'Hana','Halilovic','456123879','ZENSKO','2000-11-08','Zenica','Kosevo 25','STUDENT','hanahalilovic@gmail.com',1);
INSERT INTO "patients" VALUES (15,'Kenan','Ahmetspahic','741852963','MUSKO','1969-10-16','Zenica','Asima Ferhatovica 49','EMPLOYEE','ahmetspahic_kenan@gmail.com',10);
INSERT INTO "patients" VALUES (17,'Lana','Suljevic','986547123','ZENSKO','1999-10-24','Zenica','Marcela Snajdera 13','STUDENT','lanna@gmail.com',1);
INSERT INTO "patients" VALUES (18,'Mirela','Ahmetovic','986547123','FEMALE','1943-01-01','Sarajevo','Gornji Hotonj 25','EMPLOYEE','mirelaahmetovic@gmail.com',1);
INSERT INTO "patients" VALUES (20,'Hana','Beglerovic','123456789','FEMALE','2005-01-01','Sarajevo','Jukiceva 23','EMPLOYEE','hanabeglerovic@gmail.com',1);
INSERT INTO "patients" VALUES (24,'Kanita','Becic','124578963','ZENSKO','1982-08-25','Sarajevo','Marcela Snajdera 33','EMPLOYEE','kanitabecic@gmail.com',1);
INSERT INTO "patients" VALUES (26,'Kanita','Avdibasic','123456789','ZENSKO','1997-07-07','Sarajevo','Antuna Hangija 23','EMPLOYEE','kanita97@gmail.com',14);
INSERT INTO "patients" VALUES (28,'Amira','Handzic','986574231','ZENSKO','1971-07-01','Sarajevo','Jukiceva 23','EMPLOYEE','',1);
INSERT INTO "patients" VALUES (29,'Tarik','Mahmutovic','879564123','MALE','1942-01-01','Sarajevo','Jukiceva 23','EMPLOYEE','',1);
INSERT INTO "patients" VALUES (30,'Selma','Selmanovic','123456','MALE','1999-03-08','Sarajevo','Donji Hotonj 21','EMPLOYEE','',15);
INSERT INTO "patients" VALUES (31,'niko','nikic','123456','MALE','1999-01-01','Sarajevo','Jukicava 21','EMPLOYEE','',15);
INSERT INTO "offices" VALUES (1,'aaaa','aaa','aaa','aaa');
COMMIT;
