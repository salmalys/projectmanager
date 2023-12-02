DROP
    TABLE IF EXISTS "Notes";
DROP
    TABLE IF EXISTS "Binome";
DROP
    TABLE IF EXISTS "Projet";
DROP
    TABLE IF EXISTS "Etudiant";
DROP
    TABLE IF EXISTS "Formation";
CREATE
    TABLE IF NOT EXISTS "Formation"(
        "ID_Formation" INTEGER NOT NULL
        ,"Nom" VARCHAR(100) NOT NULL
        ,"Promotion" VARCHAR(50) NOT NULL
        ,PRIMARY KEY ("ID_Formation")
        ,UNIQUE (
            "Nom"
            ,"Promotion"
        )
    );
CREATE
    TABLE IF NOT EXISTS "Etudiant"(
        "ID_Etudiant" INTEGER NOT NULL
        ,"Nom" VARCHAR(100) NOT NULL
        ,"Prenom" VARCHAR(100) NOT NULL
        ,"ID_Formation" INTEGER NOT NULL
        ,PRIMARY KEY ("ID_Etudiant")
        ,FOREIGN KEY ("ID_Formation") REFERENCES "Formation"("ID_Formation") UNIQUE (
            "Nom"
            ,"Prenom"
        )
    );
CREATE
    TABLE IF NOT EXISTS "Projet"(
        "ID_Projet" INTEGER NOT NULL
        ,"Nom_Matiere" VARCHAR(100) NOT NULL
        ,"Sujet" TEXT NOT NULL
        ,"Date_Remise_Prevue" DATE NOT NULL
        ,PRIMARY KEY ("ID_Projet")
        ,UNIQUE (
            "Nom_Matiere"
            ,"Sujet"
        )
    );
CREATE
    TABLE IF NOT EXISTS "BinomeProjet"(
        "ID_BinomeProjet" INTEGER NOT NULL
        ,"ID_Etudiant1" INTEGER NOT NULL
        ,"ID_Etudiant2" INTEGER
        ,--optionnel
        "ID_Projet" INTEGER NOT NULL
        ,"Date_Remise_Effective" TEXT
        ,PRIMARY KEY ("ID_BinomeProjet")
        ,FOREIGN KEY ("ID_Etudiant2") REFERENCES "Etudiant"("ID_Etudiant")
        ,FOREIGN KEY ("ID_Etudiant1") REFERENCES "Etudiant"("ID_Etudiant")
        ,FOREIGN KEY ("ID_Projet") REFERENCES "Projet"("ID_Projet")
        ,UNIQUE (
            "ID_Etudiant1"
            ,"ID_ETudiant2"
            ,"ID_Projet"
        )
    );
CREATE
    TABLE IF NOT EXISTS "Notes"(
        "ID_Notes" INTEGER NOT NULL
        ,"ID_BinomeProjet" INT NOT NULL
        ,"Note_Rapport" FLOAT CHECK("Note_Rapport" BETWEEN 0 AND 20) NOT NULL
        ,"Note_Soutenance_Etudiant1" FLOAT CHECK("Note_Soutenance_Etudiant1" BETWEEN 0 AND 20) NOT NULL
        ,"Note_Soutenance_Etudiant2" FLOAT CHECK("Note_Soutenance_Etudiant2" BETWEEN 0 AND 20)
        ,--optionnel 
        PRIMARY KEY ("ID_Notes")
        ,FOREIGN KEY ("ID_BinomeProjet") REFERENCES "BinomeProjet"("ID_BinomeProjet")
        ,UNIQUE ("ID_BinomeProjet")
    );
DROP TABLE IF EXISTS "admin";
CREATE TABLE IF NOT EXISTS "admin" (
	"Id"	INTEGER NOT NULL,
	"username"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);
DROP TABLE IF EXISTS "users";
CREATE TABLE IF NOT EXISTS "users" (
	"Id"	INTEGER NOT NULL,
	"username"	TEXT NOT NULL,
	"password"	TEXT NOT NULL,
	PRIMARY KEY("Id")
);
INSERT INTO "admin" VALUES (1,'root','root'), 
(2,'salma','salma'),
(3,'ilyes','ilyes'),
(4,'khitema','mdpKHITEMA');
INSERT INTO "Formation" ("ID_Formation","Nom","Promotion") VALUES (1,'I2D','Initial'),
(2,'MIAGE','Alternance'),
(3,'MIAGE','Initial'),
(4,'LSO','Formation Continue'),
(5,'MEFA','Initial');
INSERT INTO "Etudiant" ("ID_Etudiant","Nom","Prenom","ID_Formation") VALUES (1,'LAHRACH','Salma',1),
(2,'SAIS','Ilyes',1);
INSERT INTO "Projet" ("ID_Projet","Nom_Matiere","Sujet","Date_Remise_Prevue") VALUES (1,'POA','Gestion de Projet des Etudiants','2023-12-03'),
(2,'Programmation C','Algorithme du PageRank','2024-05-15');
INSERT INTO "BinomeProjet" ("ID_BinomeProjet", "ID_Etudiant1", "ID_Etudiant2", "ID_Projet", "Date_Remise_Effective") 
VALUES (1, 1, 2, 1, '2023-12-01');
COMMIT;
