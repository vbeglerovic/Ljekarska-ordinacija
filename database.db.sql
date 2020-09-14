BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "offices" (
	"id" INTEGER,
	"name"	TEXT,
	"address"	TEXT,
	"username"	TEXT,
	"password"	TEXT,
	PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "appointments" (
	"id"	INTEGER,
	"date"	TEXT,
	"time"	TEXT,
	"patient_id" INTEGER,
	"doctor_id"	INTEGER,
	"type"	TEXT,
	"office_id"	INTEGER,
	"anamnesis"	TEXT,
	"diagnosis"	TEXT,
	"recommendation" TEXT,
    PRIMARY KEY ("id")
);
CREATE TABLE IF NOT EXISTS "patients" (
	"id"	INTEGER,
	"firstName"	TEXT,
	"lastName"	TEXT,
	"JMBG"	TEXT,
	"gender"	TEXT,
	"DOB"	TEXT,
	"POB"	TEXT,
	"address"	TEXT,
	"status"	TEXT,
	"email"	TEXT,
	"active"	INTEGER,
	"office_id"	INTEGER,
    PRIMARY KEY ("id")
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
	"active"	INTEGER,
	"office_id"	INTEGER NOT NULL,
    PRIMARY KEY ("id")
);
INSERT INTO "offices" VALUES (1,'Office1','Address1','username1','password1');
INSERT INTO "offices" VALUES (2,'Office2','Address2','username2','password2');
INSERT INTO "appointments" VALUES (1,'2020-09-12','10:00',1,1,'Prvi pregled',1,NULL,NULL,NULL);
INSERT INTO "appointments" VALUES (2,'2020-09-12','10:00',2,2,'Prvi pregled',2,NULL,NULL,NULL);
INSERT INTO "patients" VALUES (1,'Sanela','Beglerović','124563987','FEMALE','1964-02-02','Sarajevo','Gornji Hotonj 22','EMPLOYEE','sanela1964@gmail.com',1,1);
INSERT INTO "patients" VALUES (2,'Amar','Beglerović','875946123','MALE','1998-06-06','Sarajevo','Gornji Hotonj 22','STUDENT','amar.beglerovic@gmail.com',1,2);
INSERT INTO "doctors" VALUES (1,'Vildana','Beglerović','123456789','1999-07-07','Sarajevo','Donji Hotonj 21','beglerovic.vildana@gmail.com','2020-01-01','Pedijatar',1,1);
INSERT INTO "doctors" VALUES (2,'Samira','Beglerović','987654321','1972-08-25','Sarajevo','Donji Hotonj 21','samirabeglerovic@gmail.com','1992-12-12','Stomatolog',1,2);
COMMIT;
