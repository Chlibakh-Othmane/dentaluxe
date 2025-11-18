-- FULL SCHEMA (users/staff/medecin/secretaire + roles ...
-- 8 octets=BIGINT
-- ON MRT FK DANS LA TABLE ENFANT
-- 1-N : Dans la table du côté "N" (Many)
-- 1-1 : Dans l'une des deux (selon la logique métier)dentaluxe_db
-- n-n : Dans une table de liaison


CREATE TABLE IF NOT EXISTS utilisateur (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_modification_date TIMESTAMP NULL,
  created_by VARCHAR(64),
  updated_by VARCHAR(64),
  nom VARCHAR(120) NOT NULL,
  prenom VARCHAR(120) NOT NULL,
  email VARCHAR(160) NOT NULL UNIQUE,
  tel VARCHAR(40),
  adresse VARCHAR(255),
  cin VARCHAR(32) UNIQUE,
  sexe ENUM('HOMME','FEMME','AUTRE') DEFAULT 'AUTRE',
  login VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(120) NOT NULL,
  last_login_date TIMESTAMP NULL,
  date_naissance DATE NULL,
  actif BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS staff (
  id BIGINT PRIMARY KEY,
  salaire DECIMAL(12,2) DEFAULT 0,
  prime DECIMAL(12,2) DEFAULT 0,
  date_recrutement DATE,
  solde_conge INT DEFAULT 0,
  CONSTRAINT fk_staff_user FOREIGN KEY (id) REFERENCES utilisateur(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS medecin (
  id BIGINT PRIMARY KEY,
  specialite VARCHAR(120),
  agenda_medecin TEXT,
  CONSTRAINT fk_med_staff FOREIGN KEY (id) REFERENCES staff(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS secretaire (
  id BIGINT PRIMARY KEY,
  num_cnss VARCHAR(64),
  commission DECIMAL(12,2) DEFAULT 0,
  CONSTRAINT fk_sec_staff FOREIGN KEY (id) REFERENCES staff(id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS Admin(
    id BIGINT PRIMARY KEY,
    constraint fk_admin_user FOREIGN KEY (id) references utilisateur(id) on delete cascade
);

CREATE TABLE IF NOT EXISTS role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  libelle VARCHAR(80) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS utilisateur_role (
  utilisateur_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (utilisateur_id, role_id),
  CONSTRAINT fk_ur_user FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
  CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);


CREATE TABLE Patient (
                         idPatient BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(255) NOT NULL,
                         dateDeNaissance DATE,
                         sexe VARCHAR(50),
                         adresse VARCHAR(255),
                         telephone VARCHAR(50),
                         assurance VARCHAR(255)
);




CREATE TABLE IF NOT EXISTS DossierMedical (
                                              idDM BIGINT PRIMARY KEY AUTO_INCREMENT,
                                              idPatient BIGINT NOT NULL UNIQUE,
                                              dateDeCreation DATE DEFAULT CURRENT_TIMESTAMP,
                                              CONSTRAINT fk_dossiermedical_patient FOREIGN KEY (idPatient) REFERENCES Patient(idPatient) ON DELETE CASCADE
);


create table if not exists Antecedents(
    idAntecedent BIGINT primary key auto_increment,
    idDM BIGINT not null ,
    nom varchar(255) not null ,
    categorie ENUM('MALADIE','ALERGIE','CHIRURGIE','AUTRE'),
    niveauDeRisque ENUM('FAIBLE','MODERE','ELEVE') DEFAULT 'MODERE',
    CONSTRAINT fk_antecedent_dossiermedical foreign key (idDM) references DossierMedical(idDM) on delete cascade

);

create table if not exists Consultation (
    idConsultation bigint primary key auto_increment,
    idDM bigint not null ,
    idMedecin bigint not null ,
    dateConsultation date not null ,
    statut ENUM('PLANIFIEE','TERMINEE','ANNULEE') DEFAULT 'PLANIFIEE',
    observation text,
    constraint fk_consultation_dossiermedical foreign key (idDM) REFERENCES DossierMedical(idDM) ON DELETE CASCADE ,
    CONSTRAINT fk_consultation_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(id) ON DELETE CASCADE
);

create table if not exists InterventionMedecin(
    idIM bigint primary key auto_increment,
    idMedecin bigint not null ,
    idConsultation bigint not null ,
    idActe bigint not null ,
    numDent integer,
    prixIntervention double,
    constraint fk_intervention_medecin foreign key (idMedecin) REFERENCES medecin(id) ON DELETE CASCADE ,
    CONSTRAINT fk_intervention_consultation FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation) ON DELETE CASCADE,
    CONSTRAINT fk_acte FOREIGN KEY (idActe) REFERENCES Acte(idActe) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Acte (
                                    idActe BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    libelle VARCHAR(255) NOT NULL,
                                    description TEXT,
                                    prixDeBase DOUBLE NOT NULL,
                                    categorie ENUM('CONSULTATION', 'EXTRACTION', 'PLOMBAGE', 'DETARTRAGE', 'AUTRE'),
                                    idInterventionMedecin BIGINT NOT NULL
);



CREATE TABLE IF NOT EXISTS Medicament (
                                          idMedicament BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          nom VARCHAR(255) NOT NULL,
                                          type VARCHAR(100),
                                          forme VARCHAR(100),
                                          remboursable BOOLEAN DEFAULT TRUE,
                                          prixUnitaire DOUBLE,
                                          description TEXT
);

CREATE TABLE IF NOT EXISTS Ordonnance (
                                          idOrdo BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          idDM BIGINT NOT NULL,
                                          idMedecin BIGINT NOT NULL,
                                          dateOrdonnance DATE NOT NULL,
                                          CONSTRAINT fk_ordonnance_dossiermedical FOREIGN KEY (idDM) REFERENCES DossierMedical(idDM) ON DELETE CASCADE,
                                          CONSTRAINT fk_ordonnance_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Prescription (
                                            idPrescription BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            idOrdo BIGINT NOT NULL,
                                            idMedicament BIGINT NOT NULL,
                                            quantite INT,
                                            frequence VARCHAR(100),
                                            dureeEnJours INT,
                                            CONSTRAINT fk_prescription_ordonnance FOREIGN KEY (idOrdo) REFERENCES Ordonnance(idOrdo) ON DELETE CASCADE,
                                            CONSTRAINT fk_prescription_medicament FOREIGN KEY (idMedicament) REFERENCES Medicament(idMedicament) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Certificat (
                                          idCertif BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          idDM BIGINT NOT NULL,
                                          idMedecin BIGINT NOT NULL,
                                          dateDebut DATE NOT NULL,
                                          dateFin DATE,
                                          duree INT,
                                          noteMedecin TEXT,
                                          CONSTRAINT fk_certificat_dossiermedical FOREIGN KEY (idDM) REFERENCES DossierMedical(idDM) ON DELETE CASCADE,
                                          CONSTRAINT fk_certificat_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS RDV (
                                   idRDV BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   idDM BIGINT NOT NULL,
                                   idMedecin BIGINT NOT NULL,
                                   dateRDV DATE NOT NULL,
                                   heureRDV TIME NOT NULL,
                                   motif VARCHAR(255),
                                   statut ENUM('PLANIFIE', 'CONFIRME', 'ANNULE', 'TERMINE') DEFAULT 'PLANIFIE',
                                   noteMedecin TEXT,
                                   CONSTRAINT fk_rdv_dossiermedical FOREIGN KEY (idDM) REFERENCES DossierMedical(idDM) ON DELETE CASCADE,
                                   CONSTRAINT fk_rdv_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS AgendaMensuel (
                                             idAgenda BIGINT PRIMARY KEY AUTO_INCREMENT,
                                             idMedecin BIGINT NOT NULL,
                                             mois VARCHAR(20) NOT NULL,
                                             annee INT NOT NULL,
                                             joursNonDisponible TEXT,
                                             CONSTRAINT fk_agenda_medecin FOREIGN KEY (idMedecin) REFERENCES Medecin(id) ON DELETE CASCADE,
                                             UNIQUE (idMedecin, mois, annee)
);

CREATE TABLE IF NOT EXISTS SituationFinanciere (
                                                   idSF BIGINT PRIMARY KEY AUTO_INCREMENT,
                                                   idDM BIGINT NOT NULL UNIQUE,
                                                   totalDesActes DOUBLE,
                                                   totalPaye DOUBLE,
                                                   resteDu DOUBLE,
                                                   creance DOUBLE,
                                                   statut ENUM('SOLDE', 'DEBIT', 'CREDIT'),
                                                   enPromo BOOLEAN DEFAULT FALSE,
                                                   CONSTRAINT fk_sf_dossiermedical FOREIGN KEY (idDM) REFERENCES DossierMedical(idDM) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Facture (
                                       idFacture BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       idSF BIGINT NOT NULL,
                                       idConsultation BIGINT UNIQUE,
                                       totalFacture DOUBLE NOT NULL,
                                       totalDesActes DOUBLE NOT NULL,
                                       montantPaye DOUBLE NOT NULL DEFAULT 0.0,
                                       reste DOUBLE NOT NULL DEFAULT 0.0,
                                       statut ENUM('EN_ATTENTE', 'PAYEE', 'PARTIELLEMENT_PAYEE', 'ANNULEE') DEFAULT 'EN_ATTENTE',
                                       dateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_facture_sf FOREIGN KEY (idSF) REFERENCES SituationFinanciere(idSF) ON DELETE CASCADE,
                                       CONSTRAINT fk_facture_consultation FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS CabinetMedical (
                                              idUser BIGINT PRIMARY KEY,
                                              nom VARCHAR(255) NOT NULL,
                                              email VARCHAR(160),
                                              logo VARCHAR(255),
                                              adresse VARCHAR(255),
                                              cin VARCHAR(32),
                                              tel VARCHAR(40),
                                              siteWeb VARCHAR(255),
                                              instagram VARCHAR(255),
                                              facebook VARCHAR(255),
                                              description TEXT,
                                              CONSTRAINT fk_cabinet_user FOREIGN KEY (idUser) REFERENCES Utilisateur(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Charges (
                                       idCharge BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       idCabinet BIGINT NOT NULL,
                                       titre VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       montant DOUBLE NOT NULL,
                                       dateCharge DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_charge_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedical(idUser) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Revenus (
                                       idRevenu BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       idCabinet BIGINT NOT NULL,
                                       titre VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       montant DOUBLE NOT NULL,
                                       dateRevenu DATETIME DEFAULT CURRENT_TIMESTAMP,
                                       CONSTRAINT fk_revenu_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedical(idUser) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Notification (
                                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            idUser BIGINT NOT NULL,
                                            titre VARCHAR(255) NOT NULL,
                                            message TEXT NOT NULL,
                                            dateCreation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                            type ENUM('RDV', 'PAIEMENT', 'SYSTEME', 'ALERT'),
                                            priorite ENUM('BASSE', 'MOYENNE', 'ELEVE') DEFAULT 'MOYENNE',
                                            CONSTRAINT fk_notification_user FOREIGN KEY (idUser) REFERENCES Utilisateur(id) ON DELETE CASCADE
);



CREATE TABLE IF NOT EXISTS Statistiques (
                                            idStat BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            idCabinet BIGINT NOT NULL,
                                            nom VARCHAR(255) NOT NULL,
                                            categorie ENUM('FINANCIERES', 'PATIENTS', 'ACTIVITE', 'AUTRE'),
                                            chiffre DOUBLE,
                                            dateCalcul DATE DEFAULT CURRENT_TIMESTAMP,
                                            CONSTRAINT fk_stats_cabinet FOREIGN KEY (idCabinet) REFERENCES CabinetMedical(idUser) ON DELETE CASCADE
);