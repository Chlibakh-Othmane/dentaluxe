package ma.dentaluxe.repository.test;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.*;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.AntecedentPatient.AntecedentPatient;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.entities.caisse.Facture;
import ma.dentaluxe.entities.caisse.SituationFinanciere;
import ma.dentaluxe.entities.utilisateur.Utilisateur;

// --- IMPORTS DES INTERFACES (API) ---
import ma.dentaluxe.repository.modules.AntecedentPatient.api.AntecedentPatientRepository;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.*;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.caisse.api.*;
import ma.dentaluxe.repository.modules.ordonnance.api.*;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.repository.modules.certificat.api.CertificatRepository;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AUTEUR : AYA LEZREGUE  & BAKIR AYA
 * Test complet des repositories avec Injection de Dépendances via ApplicationContext
 * ORDRE : Cabinet → Staff → Caisse → Patient → Antécédents → Dossier → RDV → Consultation →
 *         Actes → Intervention → SF → Facture → Ordonnance → Certificat
 */
public class Test {

    // --- DÉCLARATION DES REPOSITORIES VIA LEURS INTERFACES ---
    private static PatientRepository patientRepo;
    private static DossierMedicalRepository dossierRepo;
    private static RDVRepository rdvRepo;
    private static ConsultationRepository consultationRepo;
    private static ActeRepository acteRepo;
    private static InterventionMedecinRepository interventionRepo;
    private static FactureRepository factureRepo;
    private static SituationFinanciereRepository sfRepo;
    private static OrdonnanceRepository ordonnanceRepo;
    private static PrescriptionRepository prescriptionRepo;
    private static MedicamentRepository medicamentRepo;
    private static CertificatRepository certificatRepo;
    private static AntecedentsRepository antecedentsRepo;
    private static AntecedentPatientRepository antecedentPatientRepo;
    private static AuthRepository authRepo;

    // IDs pour les tests
    private static Long idCabinet;
    private static Long idAdmin;
    private static Long idMedecin;
    private static Long idSecretaire;
    private static Long idPatient;
    private static Long idAntecedent1;
    private static Long idAntecedent2;
    private static Long idDM;
    private static Long idRDV;
    private static Long idConsultation;
    private static Long idActe;
    private static Long idIntervention;
    private static Long idSF;
    private static Long idFacture;
    private static Long idOrdonnance;
    private static Long idCertificat;

    /**
     * BLOC STATIQUE D'INITIALISATION (INJECTION DE DÉPENDANCES)
     * Charge les implémentations définies dans beans.properties
     */
    static {
        System.out.println("Initialisation du contexte d'application...");
        try {
            patientRepo = (PatientRepository) ApplicationContext.getBean("patientRepo");
            dossierRepo = (DossierMedicalRepository) ApplicationContext.getBean("dossierRepo");
            rdvRepo = (RDVRepository) ApplicationContext.getBean("rdvRepo");
            consultationRepo = (ConsultationRepository) ApplicationContext.getBean("consultationRepo");
            acteRepo = (ActeRepository) ApplicationContext.getBean("acteRepo");
            interventionRepo = (InterventionMedecinRepository) ApplicationContext.getBean("interventionRepo");
            factureRepo = (FactureRepository) ApplicationContext.getBean("factureRepo");
            sfRepo = (SituationFinanciereRepository) ApplicationContext.getBean("sfRepo");
            ordonnanceRepo = (OrdonnanceRepository) ApplicationContext.getBean("ordonnanceRepo");
            prescriptionRepo = (PrescriptionRepository) ApplicationContext.getBean("prescriptionRepo");
            medicamentRepo = (MedicamentRepository) ApplicationContext.getBean("medicamentRepo");
            certificatRepo = (CertificatRepository) ApplicationContext.getBean("certificatRepo");
            antecedentsRepo = (AntecedentsRepository) ApplicationContext.getBean("antecedentsRepo");
            antecedentPatientRepo = (AntecedentPatientRepository) ApplicationContext.getBean("AntecedentPatientRepo");
            authRepo = (AuthRepository) ApplicationContext.getBean("authRepo");

            System.out.println(" Tous les repositories ont été injectés avec succès depuis beans.properties.\n");
        } catch (Exception e) {
            System.err.println(" Erreur critique lors de l'injection des dépendances : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * PROCESSUS D'INSERTION COMPLET
     * Cabinet → Staff → Caisse → Patient → Antécédents → Dossier → RDV → Consultation →
     * Actes → Intervention → SF → Facture → Ordonnance → Certificat
     */
    void insertProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║            PROCESSUS D'INSERTION COMPLET                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            // ========================================
            // NETTOYAGE PRÉALABLE DES DONNÉES EXISTANTES
            // ========================================
            cleanupExistingData();

            // ========================================
            // ÉTAPE 1 : CRÉER LE CABINET MÉDICAL
            // ========================================
            System.out.println(" ÉTAPE 1 : Création du cabinet médical");
            System.out.println("─────────────────────────────────────────");

            Utilisateur admin = Utilisateur.builder()
                    .nom("ADMIN")
                    .prenom("Cabinet")
                    .email("admin@dentaluxe.ma")
                    .tel("0522334455")
                    .login("admin")
                    .passwordHash("admin123")
                    .actif(true)
                    .creationDate(LocalDateTime.now())
                    .lastModificationDate(LocalDateTime.now())
                    .build();

            authRepo.create(admin);
            idAdmin = admin.getId();

            if (idAdmin == null) {
                throw new RuntimeException("Échec de création de l'utilisateur admin - ID null");
            }

            affecterRole(idAdmin, "ADMIN");
            System.out.println("     Admin créé - ID: " + idAdmin);

            executeSql("INSERT INTO CabinetMedical (idUser, nom, email, tel, adresse, siteWeb, description) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    idAdmin, "DentaLuxe Clinic", "contact@dentaluxe.ma", "0522334455",
                    "123 Boulevard Mohamed V, Casablanca", "www.dentaluxe.ma",
                    "Cabinet dentaire moderne et équipé");
            idCabinet = idAdmin;
            System.out.println("    Cabinet créé - ID: " + idCabinet + "\n");

            // ========================================
            // ÉTAPE 2 : CRÉER LE STAFF DU CABINET
            // ========================================
            System.out.println(" ÉTAPE 2 : Création du staff");
            System.out.println("─────────────────────────────────────────");

            Utilisateur medecin = Utilisateur.builder()
                    .nom("ALAOUI")
                    .prenom("Hassan")
                    .email("dr.alaoui@dentaluxe.ma")
                    .tel("0612345678")
                    .login("dr.alaoui")
                    .passwordHash("medecin123")
                    .actif(true)
                    .creationDate(LocalDateTime.now())
                    .lastModificationDate(LocalDateTime.now())
                    .build();

            authRepo.create(medecin);
            idMedecin = medecin.getId();

            if (idMedecin == null) {
                throw new RuntimeException("Échec de création du médecin - ID null");
            }

            affecterRole(idMedecin, "MEDECIN");

            executeSql("INSERT INTO staff (id, salaire, date_recrutement) VALUES (?, ?, ?)",
                    idMedecin, 15000.0, LocalDate.now().minusYears(3));
            executeSql("INSERT INTO medecin (id, specialite) VALUES (?, ?)",
                    idMedecin, "Chirurgien-dentiste");
            System.out.println("    Médecin créé - Dr. " + medecin.getNom() + " (ID: " + idMedecin + ")");

            Utilisateur secretaire = Utilisateur.builder()
                    .nom("BENALI")
                    .prenom("Fatima")
                    .email("fatima@dentaluxe.ma")
                    .tel("0623456789")
                    .login("f.benali")
                    .passwordHash("secret123")
                    .actif(true)
                    .creationDate(LocalDateTime.now())
                    .lastModificationDate(LocalDateTime.now())
                    .build();

            authRepo.create(secretaire);
            idSecretaire = secretaire.getId();

            if (idSecretaire == null) {
                throw new RuntimeException("Échec de création de la secrétaire - ID null");
            }

            affecterRole(idSecretaire, "SECRETAIRE");

            executeSql("INSERT INTO staff (id, salaire, date_recrutement) VALUES (?, ?, ?)",
                    idSecretaire, 5000.0, LocalDate.now().minusYears(1));
            executeSql("INSERT INTO secretaire (id, num_cnss, commission) VALUES (?, ?, ?)",
                    idSecretaire, "CNSS12345678", 500.0);
            System.out.println("    Secrétaire créée - " + secretaire.getPrenom() + " (ID: " + idSecretaire + ")\n");

            // ========================================
            // ÉTAPE 3 : INITIALISER LA CAISSE
            // ========================================
            System.out.println("ÉTAPE 3 : Initialisation de la caisse");
            System.out.println("─────────────────────────────────────────");

            executeSql("INSERT INTO Revenus (idCabinet, titre, description, montant, dateRevenu) VALUES (?, ?, ?, ?, ?)",
                    idCabinet, "Capital initial", "Investissement de démarrage", 100000.0, LocalDateTime.now());
            System.out.println("     Revenu initial : 100,000 DH");

            executeSql("INSERT INTO Charges (idCabinet, titre, description, montant, dateCharge) VALUES (?, ?, ?, ?, ?)",
                    idCabinet, "Équipements", "Achat fauteuils et matériel", 50000.0, LocalDateTime.now());
            System.out.println("     Charge initiale : 50,000 DH\n");

            // ========================================
            // ÉTAPE 4 : CRÉER UN PATIENT
            // ========================================
            System.out.println(" ÉTAPE 4 : Création d'un patient");
            System.out.println("─────────────────────────────────────────");

            Patient patient = Patient.builder()
                    .nom("LEZREGUE")
                    .prenom("Aya")
                    .dateNaissance(LocalDate.of(1995, 5, 15))
                    .sexe(Sexe.FEMME)
                    .telephone("0612345678")
                    .email("aya.lezregue@test.com")
                    .adresse("123 Rue Test, Casablanca")
                    .assurance(Assurance.CNOPS)
                    .build();
            patientRepo.create(patient);
            idPatient = patient.getId();
            System.out.println("   Patient créé - " + patient.getNom() + " " + patient.getPrenom() + " (ID: " + idPatient + ")\n");

            // ========================================
            // ÉTAPE 5 : CRÉER DES ANTÉCÉDENTS
            // ========================================
            System.out.println(" ÉTAPE 5 : Création des antécédents du patient");
            System.out.println("─────────────────────────────────────────");

            Long idAntecedentAllergie = 1L;   // Allergie Pénicilline
            Long idAntecedentDiabete  = 2L;   // Diabète Type 2

            AntecedentPatient ap1 = AntecedentPatient.builder()
                    .idPatient(idPatient)
                    .idAntecedent(idAntecedentAllergie)
                    .dateAjout(LocalDate.now())
                    .actif(true)
                    .notes("Allergie découverte en 2020")
                    .build();

            antecedentPatientRepo.create(ap1);

            AntecedentPatient ap2 = AntecedentPatient.builder()
                    .idPatient(idPatient)
                    .idAntecedent(idAntecedentDiabete)
                    .dateAjout(LocalDate.now())
                    .actif(true)
                    .notes("Diabète stabilisé avec Metformine")
                    .build();

            antecedentPatientRepo.create(ap2);

            System.out.println("    Antécédents personnels associés au patient\n");

            // ========================================
            // ÉTAPE 6 : CRÉER LE DOSSIER MÉDICAL
            // ========================================
            System.out.println(" ÉTAPE 6 : Création du dossier médical");
            System.out.println("─────────────────────────────────────────");

            DossierMedical dossier = DossierMedical.builder()
                    .idPatient(idPatient)
                    .dateDeCreation(LocalDate.now())
                    .build();
            dossierRepo.create(dossier);
            idDM = dossier.getIdDM();
            System.out.println("   Dossier médical créé - ID: " + idDM + "\n");

            // ========================================
            // ÉTAPE 7 : CRÉER UN RENDEZ-VOUS
            // ========================================
            System.out.println(" ÉTAPE 7 : Création d'un rendez-vous");
            System.out.println("─────────────────────────────────────────");

            RDV rdv = RDV.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)  // ID maintenant disponible
                    .dateRDV(LocalDate.now().plusDays(1))
                    .heureRDV(LocalTime.of(10, 0))
                    .motif("Consultation dentaire de routine")
                    .statut(StatutRDV.CONFIRME)
                    .noteMedecin("")
                    .build();
            rdvRepo.create(rdv);
            idRDV = rdv.getIdRDV();
            System.out.println("   RDV créé - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() + " (ID: " + idRDV + ")\n");

            // ========================================
            // ÉTAPE 8 : CRÉER UNE CONSULTATION
            // ========================================
            System.out.println(" ÉTAPE 8 : Création d'une consultation");
            System.out.println("─────────────────────────────────────────");

            Consultation consultation = Consultation.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateConsultation(LocalDate.now())
                    .statut(StatutConsultation.TERMINEE)
                    .observation("Patient présente une carie sur molaire gauche. Traitement nécessaire.")
                    .build();
            consultationRepo.create(consultation);
            idConsultation = consultation.getIdConsultation();
            System.out.println("     Consultation créée - ID: " + idConsultation + "\n");

            // ========================================
            // ÉTAPE 9 : CRÉER DES ACTES
            // ========================================
            System.out.println(" ÉTAPE 9 : Création d'actes dentaires");
            System.out.println("─────────────────────────────────────────");

            Acte acte = Acte.builder()
                    .libelle("Détartrage + Plombage")
                    .description("Nettoyage dentaire + Obturation composite")
                    .prixDeBase(800.0)
                    .categorie(CategorieActe.CONSULTATION)
                    .build();
            acteRepo.create(acte);
            idActe = acte.getIdActe();
            System.out.println("     Acte créé - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH (ID: " + idActe + ")\n");

            // ========================================
            // ÉTAPE 10 : CRÉER UNE INTERVENTION
            // ========================================
            System.out.println(" ÉTAPE 10 : Création d'intervention médecin");
            System.out.println("─────────────────────────────────────────");

            InterventionMedecin intervention = InterventionMedecin.builder()
                    .idConsultation(idConsultation)
                    .idMedecin(idMedecin)
                    .idActe(idActe)
                    .numDent(2)
                    .prixIntervention(800.0)
                    .build();
            interventionRepo.create(intervention);
            idIntervention = intervention.getIdIM();
            System.out.println("    Intervention créée - ID: " + idIntervention + "\n");

            // ========================================
            // ÉTAPE 11 : CRÉER LA SITUATION FINANCIÈRE
            // ========================================
            System.out.println(" ÉTAPE 11 : Création de la situation financière");
            System.out.println("─────────────────────────────────────────");

            SituationFinanciere sf = SituationFinanciere.builder()
                    .idDM(idDM)
                    .totalDesActes(800.0)
                    .totalPaye(0.0)
                    .resteDu(800.0)
                    .creance(800.0)
                    .statut(StatutSituationFinanciere.DEBIT)
                    .enPromo(false)
                    .build();
            sfRepo.create(sf);
            idSF = sf.getIdSF();
            System.out.println("    Situation financière créée - Total: " + sf.getTotalDesActes() + " DH (ID: " + idSF + ")\n");

            // ========================================
            // ÉTAPE 12 : CRÉER UNE FACTURE
            // ========================================
            System.out.println(" ÉTAPE 12 : Création d'une facture");
            System.out.println("─────────────────────────────────────────");

            Facture facture = Facture.builder()
                    .idConsultation(idConsultation)
                    .idSF(idSF)
                    .dateCreation(LocalDateTime.now())
                    .totalFacture(800.0)
                    .montantPaye(0.0)
                    .reste(800.0)
                    .statut(StatutFacture.EN_ATTENTE)
                    .build();
            factureRepo.create(facture);
            idFacture = facture.getIdFacture();
            System.out.println("    Facture créée - Montant: " + facture.getTotalFacture() + " DH (ID: " + idFacture + ")\n");

            // ========================================
            // ÉTAPE 13 : CRÉER UNE ORDONNANCE
            // ========================================
            System.out.println("ÉTAPE 13 : Création d'une ordonnance");
            System.out.println("─────────────────────────────────────────");

            Ordonnance ordonnance = Ordonnance.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateOrdonnance(LocalDate.now())
                    .build();
            ordonnanceRepo.create(ordonnance);
            idOrdonnance = ordonnance.getIdOrdo();
            System.out.println("    Ordonnance créée - ID: " + idOrdonnance);

            List<Medicament> medicaments = medicamentRepo.findAll();
            if (!medicaments.isEmpty()) {
                Medicament med = medicaments.get(0);
                Prescription prescription = Prescription.builder()
                        .idOrdo(idOrdonnance)
                        .idMedicament(med.getIdMedicament())
                        .quantite(1)
                        .frequence("3x/jour")
                        .dureeEnJours(7)
                        .build();
                prescriptionRepo.create(prescription);
                System.out.println("    Prescription ajoutée - " + med.getNom() + "\n");
            }

            // ========================================
            // ÉTAPE 14 : CRÉER UN CERTIFICAT
            // ========================================
            System.out.println(" ÉTAPE 14 : Création d'un certificat médical");
            System.out.println("─────────────────────────────────────────");

            Certificat certificat = Certificat.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateDebut(LocalDate.now())
                    .dateFin(LocalDate.now().plusDays(3))
                    .duree(3)
                    .noteMedecin("Repos recommandé après intervention dentaire")
                    .build();
            certificatRepo.create(certificat);
            idCertificat = certificat.getIdCertif();
            System.out.println("    Certificat créé - " + certificat.getDuree() + " jours (ID: " + idCertificat + ")\n");

            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║         PROCESSUS D'INSERTION TERMINÉ AVEC SUCCÈS       ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n✗ ERREUR lors de l'insertion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE MISE À JOUR
     */
    void updateProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║            PROCESSUS DE MISE À JOUR                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            System.out.println(" Mise à jour des entités créées...\n");

            Patient patient = patientRepo.findById(idPatient);
            if (patient != null) {
                patient.setTelephone("0698765432");
                patientRepo.update(patient);
                System.out.println(" Patient mis à jour");
            }

            if (idFacture != null) {
                Facture facture = factureRepo.findById(idFacture);
                if (facture != null) {
                    double paiement = 400.0;
                    facture.setMontantPaye(paiement);
                    facture.setReste(facture.getTotalFacture() - paiement);
                    facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
                    factureRepo.update(facture);
                    System.out.println(" Paiement enregistré : " + paiement + " DH");

                    if (idSF != null) {
                        SituationFinanciere sf = sfRepo.findById(idSF);
                        if (sf != null) {
                            sf.setTotalPaye(paiement);
                            sf.setResteDu(sf.getTotalDesActes() - paiement);
                            sf.setCreance(sf.getResteDu());
                            sf.setStatut(StatutSituationFinanciere.SOLDE);
                            sfRepo.update(sf);
                            System.out.println(" Situation financière mise à jour");
                        }
                    }
                }
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║        PROCESSUS DE MISE À JOUR TERMINÉ AVEC SUCCÈS      ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\nERREUR lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE SÉLECTION (LECTURE)
     */
    void selectProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              PROCESSUS DE SÉLECTION                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            System.out.println(" Lecture de toutes les données créées...\n");

            System.out.println("✓ Cabinet : ID " + idCabinet);
            System.out.println("✓ Staff : Admin(" + idAdmin + "), Médecin(" + idMedecin + "), Secrétaire(" + idSecretaire + ")");
            System.out.println("✓ Patient : ID " + idPatient);
            System.out.println("✓ Antécédents : " + antecedentPatientRepo.countByPatientId(idPatient) + " associé(s)");
            System.out.println("✓ Dossier médical : ID " + idDM);
            System.out.println("✓ RDV : ID " + idRDV);
            System.out.println("✓ Consultation : ID " + idConsultation);
            System.out.println("✓ Acte : ID " + idActe);
            System.out.println("✓ Intervention : ID " + idIntervention);

            if (idFacture != null) {
                Facture facture = factureRepo.findById(idFacture);
                if (facture != null) {
                    System.out.println("✓ Facture : " + facture.getMontantPaye() + " DH payé sur " + facture.getTotalFacture() + " DH");
                }
            }

            System.out.println("✓ Situation financière : ID " + idSF);
            System.out.println("✓ Ordonnance : ID " + idOrdonnance);
            System.out.println("✓ Certificat : ID " + idCertificat);

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║        PROCESSUS DE SÉLECTION TERMINÉ AVEC SUCCÈS       ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n ERREUR lors de la sélection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE SUPPRESSION
     */
    void deleteProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                PROCESSUS DE SUPPRESSION                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            System.out.println(" Suppression dans l'ordre inverse de la création...\n");

            if (idCertificat != null) {
                certificatRepo.deleteById(idCertificat);
                System.out.println(" Certificat supprimé");
            }
            if (idOrdonnance != null) {
                ordonnanceRepo.deleteById(idOrdonnance);
                System.out.println(" Ordonnance supprimée");
            }
            if (idFacture != null) {
                factureRepo.deleteById(idFacture);
                System.out.println(" Facture supprimée");
            }
            if (idSF != null) {
                sfRepo.deleteById(idSF);
                System.out.println("Situation financière supprimée");
            }
            if (idIntervention != null) {
                interventionRepo.deleteById(idIntervention);
                System.out.println("Intervention supprimée");
            }
            if (idActe != null) {
                acteRepo.deleteById(idActe);
                System.out.println(" Acte supprimé");
            }
            if (idConsultation != null) {
                consultationRepo.deleteById(idConsultation);
                System.out.println(" Consultation supprimée");
            }
            if (idRDV != null) {
                rdvRepo.deleteById(idRDV);
                System.out.println(" RDV supprimé");
            }
            if (idDM != null) {
                dossierRepo.deleteById(idDM);
                System.out.println(" Dossier médical supprimé");
            }
            if (idPatient != null) {
                patientRepo.deleteById(idPatient);
                System.out.println(" Patient supprimé");
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║        PROCESSUS DE SUPPRESSION TERMINÉ AVEC SUCCÈS      ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n ERREUR lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Nettoie les données existantes pour éviter les erreurs de duplication
     */
    private static void cleanupExistingData() {
        try {
            // Suppression des données existantes dans l'ordre inverse des dépendances
            executeSqlSilent("DELETE FROM certificat WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM prescription WHERE idOrdo IN (SELECT idOrdo FROM ordonnance WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma')))");
            executeSqlSilent("DELETE FROM ordonnance WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM facture WHERE idConsultation IN (SELECT idConsultation FROM consultation WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma')))");
            executeSqlSilent("DELETE FROM situationfinanciere WHERE idDM IN (SELECT idDM FROM dossiermedical WHERE idPatient IN (SELECT id FROM patient WHERE email = 'aya.lezregue@test.com'))");
            executeSqlSilent("DELETE FROM interventionmedecin WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM consultation WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM rdv WHERE idMedecin IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM dossiermedical WHERE idPatient IN (SELECT id FROM patient WHERE email = 'aya.lezregue@test.com')");
            executeSqlSilent("DELETE FROM antecedent_patient WHERE idPatient IN (SELECT id FROM patient WHERE email = 'aya.lezregue@test.com')");
            executeSqlSilent("DELETE FROM patient WHERE email = 'aya.lezregue@test.com'");
            executeSqlSilent("DELETE FROM Charges WHERE idCabinet IN (SELECT idUser FROM CabinetMedical WHERE email = 'contact@dentaluxe.ma')");
            executeSqlSilent("DELETE FROM Revenus WHERE idCabinet IN (SELECT idUser FROM CabinetMedical WHERE email = 'contact@dentaluxe.ma')");
            executeSqlSilent("DELETE FROM CabinetMedical WHERE email = 'contact@dentaluxe.ma'");
            executeSqlSilent("DELETE FROM secretaire WHERE id IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM medecin WHERE id IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM staff WHERE id IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM utilisateur_role WHERE utilisateur_id IN (SELECT id FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma'))");
            executeSqlSilent("DELETE FROM utilisateur WHERE email IN ('admin@dentaluxe.ma', 'dr.alaoui@dentaluxe.ma', 'fatima@dentaluxe.ma')");

        } catch (Exception e) {
            // Ignorer les erreurs de nettoyage (si les données n'existent pas)
        }
    }

    // Méthodes utilitaires
    private static void affecterRole(Long userId, String roleName) {
        executeSql("INSERT INTO utilisateur_role (utilisateur_id, role_id) SELECT ?, id FROM role WHERE libelle = ?",
                userId, roleName);
    }

    private static void executeSql(String sql, Object... params) {
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(" Erreur SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Exécute une requête SQL sans afficher les erreurs (pour le nettoyage)
     */
    private static void executeSqlSilent(String sql, Object... params) {
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Ignorer silencieusement les erreurs
        }
    }

    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          DENTALUXE - TEST COMPLET REPOSITORIES          ║");
        System.out.println("║                   Par AYA LEZREGUE                         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try (Connection conn = Db.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println(" Connexion à la base de données réussie!\n");
            }
        } catch (Exception e) {
            System.out.println(" Erreur de connexion: " + e.getMessage());
            return;
        }

        Test test = new Test();

        try {
            test.insertProcess();
            Thread.sleep(1000);
            test.selectProcess();
            Thread.sleep(1000);
            test.updateProcess();
            Thread.sleep(1000);
            test.deleteProcess();

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║               TOUS LES TESTS TERMINÉS                    ║");
            System.out.println("║  ORDRE: Cabinet → Staff → Caisse → Patient → Antécédents  ║");
            System.out.println("║  → Dossier → RDV → Consultation → Actes → Intervention    ║");
            System.out.println("║  → SF → Facture → Ordonnance → Certificat                  ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        } catch (Exception e) {
            System.err.println("\nERREUR CRITIQUE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}