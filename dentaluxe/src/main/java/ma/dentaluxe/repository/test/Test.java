package ma.dentaluxe.repository.test;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.enums.*;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.entities.certificat.Certificat;
import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.utilisateur.Utilisateur;

// --- NOUVEAUX IMPORTS : ON IMPORTE LES INTERFACES (API) ---
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.*;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.caisse.api.*;
import ma.dentaluxe.repository.modules.ordonnance.api.*;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.repository.modules.certificat.api.CertificatRepository;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AUTEUR : AYA LEZREGUE
 * Test complet des repositories avec Injection de Dépendances via ApplicationContext
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
    private static AuthRepository authRepo;

    // IDs pour les tests (seront remplis pendant insertProcess)
    private static Long idPatient;
    private static Long idDM;
    private static Long idRDV;
    private static Long idConsultation;
    private static Long idActe;
    private static Long idIntervention;
    private static Long idFacture;
    private static Long idSF;
    private static Long idOrdonnance;
    private static Long idCertificat;
    private static Long idMedecin;

    /**
     * BLOC STATIQUE D'INITIALISATION (INJECTION DE DÉPENDANCES)
     * Charge les implémentations définies dans beans.properties
     */
    static {
        System.out.println("🔄 Initialisation du contexte d'application...");
        try {
            patientRepo      = (PatientRepository) ApplicationContext.getBean("patientRepo");
            dossierRepo      = (DossierMedicalRepository) ApplicationContext.getBean("dossierRepo");
            rdvRepo          = (RDVRepository) ApplicationContext.getBean("rdvRepo");
            consultationRepo = (ConsultationRepository) ApplicationContext.getBean("consultationRepo");
            acteRepo         = (ActeRepository) ApplicationContext.getBean("acteRepo");
            interventionRepo = (InterventionMedecinRepository) ApplicationContext.getBean("interventionRepo");
            factureRepo      = (FactureRepository) ApplicationContext.getBean("factureRepo");
            sfRepo           = (SituationFinanciereRepository) ApplicationContext.getBean("sfRepo");
            ordonnanceRepo   = (OrdonnanceRepository) ApplicationContext.getBean("ordonnanceRepo");
            prescriptionRepo = (PrescriptionRepository) ApplicationContext.getBean("prescriptionRepo");
            medicamentRepo   = (MedicamentRepository) ApplicationContext.getBean("medicamentRepo");
            certificatRepo   = (CertificatRepository) ApplicationContext.getBean("certificatRepo");
            antecedentsRepo  = (AntecedentsRepository) ApplicationContext.getBean("antecedentsRepo");
            authRepo         = (AuthRepository) ApplicationContext.getBean("authRepo");

            System.out.println("✅ Tous les repositories ont été injectés avec succès depuis beans.properties.\n");
        } catch (Exception e) {
            System.err.println("❌ Erreur critique lors de l'injection des dépendances : " + e.getMessage());
            e.printStackTrace();
            System.exit(1); // Arrêt immédiat si la config échoue
        }
    }

    /**
     * PROCESSUS D'INSERTION COMPLET
     * Suit le flux métier : Patient -> DM -> RDV -> Consultation -> Actes -> Facture -> SF -> Ordonnances -> Certificats
     */
    void insertProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           📝 PROCESSUS D'INSERTION COMPLET                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            // 1. CRÉER UN PATIENT
            System.out.println("1️⃣ Création d'un patient...");
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
            System.out.println("   ✅ Patient créé - ID: " + idPatient);

            // 2. CRÉER UN DOSSIER MÉDICAL
            System.out.println("\n2️⃣ Création du dossier médical...");
            DossierMedical dossier = DossierMedical.builder()
                    .idPatient(idPatient)
                    .dateDeCreation(LocalDate.now())
                    .build();
            dossierRepo.create(dossier);
            idDM = dossier.getIdDM();
            System.out.println("   ✅ Dossier médical créé - ID: " + idDM);

            // 2.1 AJOUTER DES ANTÉCÉDENTS
            System.out.println("\n2️⃣.1 Ajout d'antécédents...");
            Antecedents antecedent = Antecedents.builder()
                    .idDM(idDM)
                    .nom("Allergie Pénicilline")
                    .categorie(CategorieAntecedent.ALLERGIE)
                    .niveauDeRisque(NiveauRisque.ELEVE)
                    .build();
            antecedentsRepo.create(antecedent);
            System.out.println("   ✅ Antécédent ajouté - ID: " + antecedent.getIdAntecedent());

            // 3. OBTENIR UN MÉDECIN
            System.out.println("\n3️⃣ Récupération d'un médecin...");
            List<Utilisateur> medecins = authRepo.findAll().stream()
                    .filter(u -> "MEDECIN".equals(getMedecinRole(u.getId())))
                    .limit(1)
                    .toList();

            if (medecins.isEmpty()) {
                System.out.println("   ⚠️  Aucun médecin trouvé, création d'un médecin de test...");
                Utilisateur medecin = Utilisateur.builder()
                        .nom("ALAOUI")
                        .prenom("Hassan")
                        .email("dr.alaoui@dentaluxe.ma")
                        .tel("0612345679")
                        .login("dr.alaoui")
                        .passwordHash("password123")
                        .actif(true)
                        .creationDate(LocalDateTime.now())
                        .lastModificationDate(LocalDateTime.now())
                        .build();
                authRepo.create(medecin);
                idMedecin = medecin.getId();
                System.out.println("   ✅ Médecin créé - ID: " + idMedecin);
            } else {
                idMedecin = medecins.get(0).getId();
                System.out.println("   ✅ Médecin trouvé - ID: " + idMedecin);
            }

            // 4. CRÉER UN RENDEZ-VOUS
            System.out.println("\n4️⃣ Création d'un rendez-vous...");
            RDV rdv = RDV.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateRDV(LocalDate.now().plusDays(1))
                    .heureRDV(LocalTime.of(10, 0))
                    .motif("Consultation dentaire de routine")
                    .statut(StatutRDV.CONFIRME)
                    .noteMedecin("")
                    .build();
            rdvRepo.create(rdv);
            idRDV = rdv.getIdRDV();
            System.out.println("   ✅ RDV créé - ID: " + idRDV);

            // 5. CRÉER UNE CONSULTATION
            System.out.println("\n5️⃣ Création d'une consultation...");
            Consultation consultation = Consultation.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateConsultation(LocalDate.now())
                    .statut(StatutConsultation.TERMINEE)
                    .observation("Patient présente une carie sur molaire gauche. Traitement nécessaire.")
                    .build();
            consultationRepo.create(consultation);
            idConsultation = consultation.getIdConsultation();
            System.out.println("   ✅ Consultation créée - ID: " + idConsultation);

            // 6. CRÉER DES ACTES
            System.out.println("\n6️⃣ Création d'actes dentaires...");
            Acte acte1 = Acte.builder()
                    .libelle("Détartrage complet")
                    .description("Nettoyage dentaire professionnel")
                    .prixDeBase(300.0)
                    .categorie(CategorieActe.DETARTRAGE)
                    .build();
            acteRepo.create(acte1);
            idActe = acte1.getIdActe();
            System.out.println("   ✅ Acte créé - ID: " + idActe);

            Acte acte2 = Acte.builder()
                    .libelle("Plombage dentaire")
                    .description("Obturation composite")
                    .prixDeBase(500.0)
                    .categorie(CategorieActe.EXTRACTION)
                    .build();
            acteRepo.create(acte2);
            System.out.println("   ✅ Acte créé - ID: " + acte2.getIdActe());

            // 7. CRÉER UNE INTERVENTION
            System.out.println("\n7️⃣ Création d'intervention médecin...");
            InterventionMedecin intervention = InterventionMedecin.builder()
                    .idConsultation(idConsultation)
                    .idMedecin(idMedecin)
                    .idActe(idActe)
                    .numDent(5)
                    .prixIntervention(800.0)
                    .build();
            interventionRepo.create(intervention);
            idIntervention = intervention.getIdIM();
            System.out.println("   ✅ Intervention créée - ID: " + idIntervention);

            // 8. CRÉER UNE SITUATION FINANCIÈRE
            System.out.println("\n8️⃣ Création de la situation financière...");
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
            System.out.println("   ✅ Situation financière créée - ID: " + idSF);

            // 9. CRÉER UNE FACTURE
            System.out.println("\n9️⃣ Création d'une facture...");
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
            System.out.println("   ✅ Facture créée - ID: " + idFacture);

            // 10. CRÉER UNE ORDONNANCE
            System.out.println("\n🔟 Création d'une ordonnance...");
            Ordonnance ordonnance = Ordonnance.builder()
                    .idDM(idDM)
                    .idMedecin(idMedecin)
                    .dateOrdonnance(LocalDate.now())
                    .build();
            ordonnanceRepo.create(ordonnance);
            idOrdonnance = ordonnance.getIdOrdo();
            System.out.println("   ✅ Ordonnance créée - ID: " + idOrdonnance);

            // 10.1 AJOUTER DES MÉDICAMENTS
            System.out.println("\n🔟.1 Ajout de prescriptions...");
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
                System.out.println("   ✅ Prescription ajoutée - Médicament: " + med.getNom());
            }

            // 11. CRÉER UN CERTIFICAT
            System.out.println("\n1️⃣1️⃣ Création d'un certificat médical...");
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
            System.out.println("   ✅ Certificat créé - ID: " + idCertificat);

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║         ✅ PROCESSUS D'INSERTION TERMINÉ AVEC SUCCÈS       ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors de l'insertion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE MISE À JOUR
     */
    void updateProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           🔄 PROCESSUS DE MISE À JOUR                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            // 1. MODIFIER LE PATIENT
            System.out.println("1️⃣ Modification du patient...");
            Patient patient = patientRepo.findById(idPatient);
            if (patient != null) {
                patient.setTelephone("0698765432");
                patient.setEmail("aya.lezregue.updated@test.com");
                patientRepo.update(patient);
                System.out.println("   ✅ Patient mis à jour - ID: " + idPatient);
            }

            // 2. MODIFIER LE RDV
            System.out.println("\n2️⃣ Modification du RDV...");
            RDV rdv = rdvRepo.findById(idRDV);
            if (rdv != null) {
                rdv.setHeureRDV(LocalTime.of(11, 0));
                rdv.setStatut(StatutRDV.TERMINE);
                rdv.setNoteMedecin("Patient arrivé à l'heure, consultation effectuée");
                rdvRepo.update(rdv);
                System.out.println("   ✅ RDV mis à jour - ID: " + idRDV);
            }

            // 3. MODIFIER LA CONSULTATION
            System.out.println("\n3️⃣ Modification de la consultation...");
            Consultation consultation = consultationRepo.findById(idConsultation);
            if (consultation != null) {
                consultation.setObservation(consultation.getObservation() +
                        " | Mise à jour: Traitement effectué avec succès.");
                consultationRepo.update(consultation);
                System.out.println("   ✅ Consultation mise à jour - ID: " + idConsultation);
            }

            // 4. MODIFIER L'ACTE
            System.out.println("\n4️⃣ Modification de l'acte...");
            Acte acte = acteRepo.findById(idActe);
            if (acte != null) {
                acte.setPrixDeBase(350.0);
                acteRepo.update(acte);
                System.out.println("   ✅ Acte mis à jour - ID: " + idActe);
            }

            // 5. ENREGISTRER UN PAIEMENT
            System.out.println("\n5️⃣ Enregistrement d'un paiement...");
            Facture facture = factureRepo.findById(idFacture);
            if (facture != null) {
                double paiement = 400.0;
                facture.setMontantPaye(facture.getMontantPaye() + paiement);
                facture.setReste(facture.getTotalFacture() - facture.getMontantPaye());
                facture.setStatut(StatutFacture.PARTIELLEMENT_PAYEE);
                factureRepo.update(facture);
                System.out.println("   ✅ Paiement enregistré: " + paiement + " DH");
                System.out.println("   💰 Reste à payer: " + facture.getReste() + " DH");
            }

            // 6. METTRE À JOUR LA SITUATION FINANCIÈRE
            System.out.println("\n6️⃣ Mise à jour de la situation financière...");
            SituationFinanciere sf = sfRepo.findById(idSF);
            if (sf != null) {
                sf.setTotalPaye(400.0);
                sf.setResteDu(400.0);
                sf.setCreance(400.0);
                sf.setStatut(StatutSituationFinanciere.SOLDE);
                sfRepo.update(sf);
                System.out.println("   ✅ Situation financière mise à jour - ID: " + idSF);
            }

            // 7. PROLONGER LE CERTIFICAT
            System.out.println("\n7️⃣ Prolongation du certificat...");
            Certificat certificat = certificatRepo.findById(idCertificat);
            if (certificat != null) {
                certificat.setDateFin(certificat.getDateFin().plusDays(2));
                certificat.setDuree(certificat.getDuree() + 2);
                certificat.setNoteMedecin(certificat.getNoteMedecin() + " | Prolongation de 2 jours");
                certificatRepo.update(certificat);
                System.out.println("   ✅ Certificat prolongé - Nouvelle durée: " + certificat.getDuree() + " jours");
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║       ✅ PROCESSUS DE MISE À JOUR TERMINÉ AVEC SUCCÈS      ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors de la mise à jour: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE SÉLECTION (LECTURE)
     */
    void selectProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              📊 PROCESSUS DE SÉLECTION                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            // 1. LIRE LE PATIENT ET SON DOSSIER
            System.out.println("1️⃣ Lecture du patient et son dossier...");
            Patient patient = patientRepo.findById(idPatient);
            if (patient != null) {
                System.out.println("   📋 Patient: " + patient.getNom() + " " + patient.getPrenom());
                System.out.println("   📞 Tel: " + patient.getTelephone());
                System.out.println("   📧 Email: " + patient.getEmail());

                DossierMedical dossier = dossierRepo.findById(idDM);
                if (dossier != null) {
                    System.out.println("   📁 Dossier médical: #" + dossier.getIdDM());
                    System.out.println("   📅 Date création: " + dossier.getDateDeCreation());
                }
            }

            // 2. LIRE LES ANTÉCÉDENTS
            System.out.println("\n2️⃣ Lecture des antécédents...");
            List<Antecedents> antecedents = antecedentsRepo.findByDossierMedicalId(idDM);
            System.out.println("   📝 Nombre d'antécédents: " + antecedents.size());
            antecedents.forEach(a -> System.out.println("      • " + a.getNom() +
                    " (" + a.getCategorie() + ", " + a.getNiveauDeRisque() + ")"));

            // 3. LIRE LES RDV
            System.out.println("\n3️⃣ Lecture des rendez-vous...");
            List<RDV> rdvs = rdvRepo.findByPatientDossierId(idDM);
            System.out.println("   📅 Nombre de RDV: " + rdvs.size());
            rdvs.forEach(r -> System.out.println("      • " + r.getDateRDV() + " " +
                    r.getHeureRDV() + " - " + r.getMotif() + " (" + r.getStatut() + ")"));

            // 4. LIRE LES CONSULTATIONS
            System.out.println("\n4️⃣ Lecture des consultations...");
            List<Consultation> consultations = consultationRepo.findByDossierMedicalId(idDM);
            System.out.println("   🩺 Nombre de consultations: " + consultations.size());
            consultations.forEach(c -> System.out.println("      • " + c.getDateConsultation() +
                    " - " + c.getStatut() + " - " +
                    (c.getObservation() != null && c.getObservation().length() > 50 ?
                            c.getObservation().substring(0, 50) + "..." : c.getObservation())));

            // 5. LIRE LES INTERVENTIONS
            System.out.println("\n5️⃣ Lecture des interventions...");
            if (idIntervention != null) {
                List<InterventionMedecin> interventions = interventionRepo.findByIdConsultation(idConsultation);
                System.out.println("   💉 Nombre d'interventions: " + interventions.size());
                interventions.forEach(i -> System.out.println("      • Acte ID: " + i.getIdActe() +
                        " - Prix: " + i.getPrixIntervention() + " DH"));
            }

            // 6. LIRE LES ACTES
            System.out.println("\n6️⃣ Lecture des actes...");
            List<Acte> actes = acteRepo.findAll();
            System.out.println("   🦷 Nombre total d'actes: " + actes.size());
            actes.stream().limit(5).forEach(a -> System.out.println("      • " + a.getLibelle() +
                    " - " + a.getPrixDeBase() + " DH (" + a.getCategorie() + ")"));

            // 7. LIRE LA FACTURE
            System.out.println("\n7️⃣ Lecture de la facture...");
            Facture facture = factureRepo.findById(idFacture);
            if (facture != null) {
                System.out.println("   💰 Facture #" + facture.getIdFacture());
                System.out.println("   💵 Total: " + facture.getTotalFacture() + " DH");
                System.out.println("   ✅ Payé: " + facture.getMontantPaye() + " DH");
                System.out.println("   ⏳ Reste: " + facture.getReste() + " DH");
                System.out.println("   📊 Statut: " + facture.getStatut());
            }

            // 8. LIRE LA SITUATION FINANCIÈRE
            System.out.println("\n8️⃣ Lecture de la situation financière...");
            SituationFinanciere sf = sfRepo.findById(idSF);
            if (sf != null) {
                System.out.println("   💳 Total des actes: " + sf.getTotalDesActes() + " DH");
                System.out.println("   💰 Total payé: " + sf.getTotalPaye() + " DH");
                System.out.println("   📈 Créance: " + sf.getCreance() + " DH");
                System.out.println("   📊 Statut: " + sf.getStatut());
            }

            // 9. LIRE L'ORDONNANCE
            System.out.println("\n9️⃣ Lecture de l'ordonnance...");
            Ordonnance ordonnance = ordonnanceRepo.findById(idOrdonnance);
            if (ordonnance != null) {
                System.out.println("   💊 Ordonnance #" + ordonnance.getIdOrdo());
                System.out.println("   📅 Date: " + ordonnance.getDateOrdonnance());

                List<Prescription> prescriptions = prescriptionRepo.findByOrdonnance(idOrdonnance);
                System.out.println("   📝 Prescriptions: " + prescriptions.size());
                prescriptions.forEach(p -> {
                    Medicament med = medicamentRepo.findById(p.getIdMedicament());
                    if (med != null) {
                        System.out.println("      • " + med.getNom() + " - " +
                                p.getFrequence() + " pendant " + p.getDureeEnJours() + " jours");
                    }
                });
            }

            // 10. LIRE LE CERTIFICAT
            System.out.println("\n🔟 Lecture du certificat...");
            Certificat certificat = certificatRepo.findById(idCertificat);
            if (certificat != null) {
                System.out.println("   📜 Certificat #" + certificat.getIdCertif());
                System.out.println("   📅 Période: " + certificat.getDateDebut() + " → " + certificat.getDateFin());
                System.out.println("   ⏱️  Durée: " + certificat.getDuree() + " jours");
                System.out.println("   📝 Note: " + certificat.getNoteMedecin());
            }

            // 11. STATISTIQUES GLOBALES
            System.out.println("\n1️⃣1️⃣ Statistiques globales...");
            System.out.println("   👥 Patients totaux: " + patientRepo.findAll().size());
            System.out.println("   📁 Dossiers médicaux: " + dossierRepo.findAll().size());
            System.out.println("   📅 RDV totaux: " + rdvRepo.findAll().size());
            System.out.println("   🩺 Consultations totales: " + consultationRepo.findAll().size());
            System.out.println("   🦷 Actes disponibles: " + acteRepo.findAll().size());
            System.out.println("   💰 Factures totales: " + factureRepo.findAll().size());

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║        ✅ PROCESSUS DE SÉLECTION TERMINÉ AVEC SUCCÈS       ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors de la sélection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PROCESSUS DE SUPPRESSION
     */
    void deleteProcess() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              🗑️  PROCESSUS DE SUPPRESSION                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        try {
            // ORDRE DE SUPPRESSION (du plus dépendant au moins dépendant)

            // 1. SUPPRIMER LE CERTIFICAT
            System.out.println("1️⃣ Suppression du certificat...");
            if (idCertificat != null) {
                certificatRepo.deleteById(idCertificat);
                System.out.println("   ✅ Certificat supprimé - ID: " + idCertificat);
            }

            // 2. SUPPRIMER LES PRESCRIPTIONS
            System.out.println("\n2️⃣ Suppression des prescriptions...");
            if (idOrdonnance != null) {
                List<Prescription> prescriptions = prescriptionRepo.findByOrdonnance(idOrdonnance);
                for (Prescription p : prescriptions) {
                    prescriptionRepo.delete(p);
                }
                System.out.println("   ✅ " + prescriptions.size() + " prescription(s) supprimée(s)");
            }

            // 3. SUPPRIMER L'ORDONNANCE
            System.out.println("\n3️⃣ Suppression de l'ordonnance...");
            if (idOrdonnance != null) {
                ordonnanceRepo.deleteById(idOrdonnance);
                System.out.println("   ✅ Ordonnance supprimée - ID: " + idOrdonnance);
            }

            // 4. SUPPRIMER LA FACTURE
            System.out.println("\n4️⃣ Suppression de la facture...");
            if (idFacture != null) {
                factureRepo.deleteById(idFacture);
                System.out.println("   ✅ Facture supprimée - ID: " + idFacture);
            }

            // 5. SUPPRIMER LA SITUATION FINANCIÈRE
            System.out.println("\n5️⃣ Suppression de la situation financière...");
            if (idSF != null) {
                sfRepo.deleteById(idSF);
                System.out.println("   ✅ Situation financière supprimée - ID: " + idSF);
            }

            // 6. SUPPRIMER L'INTERVENTION
            System.out.println("\n6️⃣ Suppression de l'intervention...");
            if (idIntervention != null) {
                interventionRepo.deleteById(idIntervention);
                System.out.println("   ✅ Intervention supprimée - ID: " + idIntervention);
            }

            // 7. SUPPRIMER LES ACTES
            System.out.println("\n7️⃣ Suppression des actes...");
            if (idActe != null) {
                acteRepo.deleteById(idActe);
                System.out.println("   ✅ Acte supprimé - ID: " + idActe);
            }

            // 8. SUPPRIMER LA CONSULTATION
            System.out.println("\n8️⃣ Suppression de la consultation...");
            if (idConsultation != null) {
                consultationRepo.deleteById(idConsultation);
                System.out.println("   ✅ Consultation supprimée - ID: " + idConsultation);
            }

            // 9. SUPPRIMER LE RDV
            System.out.println("\n9️⃣ Suppression du rendez-vous...");
            if (idRDV != null) {
                rdvRepo.deleteById(idRDV);
                System.out.println("   ✅ RDV supprimé - ID: " + idRDV);
            }

            // 10. SUPPRIMER LES ANTÉCÉDENTS
            System.out.println("\n🔟 Suppression des antécédents...");
            if (idDM != null) {
                List<Antecedents> antecedents = antecedentsRepo.findByDossierMedicalId(idDM);
                for (Antecedents a : antecedents) {
                    antecedentsRepo.delete(a);
                }
                System.out.println("   ✅ " + antecedents.size() + " antécédent(s) supprimé(s)");
            }

            // 11. SUPPRIMER LE DOSSIER MÉDICAL
            System.out.println("\n1️⃣1️⃣ Suppression du dossier médical...");
            if (idDM != null) {
                dossierRepo.deleteById(idDM);
                System.out.println("   ✅ Dossier médical supprimé - ID: " + idDM);
            }

            // 12. SUPPRIMER LE PATIENT
            System.out.println("\n1️⃣2️⃣ Suppression du patient...");
            if (idPatient != null) {
                patientRepo.deleteById(idPatient);
                System.out.println("   ✅ Patient supprimé - ID: " + idPatient);
            }

            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║       ✅ PROCESSUS DE SUPPRESSION TERMINÉ AVEC SUCCÈS      ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Méthode utilitaire pour obtenir le rôle d'un médecin
     */
    private static String getMedecinRole(Long userId) {
        try (Connection conn = Db.getConnection()) {
            String sql = "SELECT r.libelle FROM role r " +
                    "JOIN utilisateur_role ur ON r.id = ur.role_id " +
                    "WHERE ur.utilisateur_id = ?";
            var pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, userId);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("libelle");
            }
        } catch (Exception e) {
            // Ignorer
        }
        return null;
    }

    /**
     * Méthode principale
     */
    public static void main(String[] args) {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                            ║");
        System.out.println("║         🦷 DENTALUXE - TEST COMPLET REPOSITORIES          ║");
        System.out.println("║                   Par AYA LEZREGUE                         ║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n");

        // Vérifier la connexion à la base de données
        try (Connection conn = Db.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connexion à la base de données réussie!\n");
            } else {
                System.out.println("❌ Échec de connexion à la base de données");
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion: " + e.getMessage());
            return;
        }

        Test test = new Test();

        // Exécuter les processus dans l'ordre
        try {
            // 1. INSERTION
            test.insertProcess();
            Thread.sleep(1000); // Pause pour la lisibilité

            // 2. SÉLECTION
            test.selectProcess();
            Thread.sleep(1000);

            // 3. MISE À JOUR
            test.updateProcess();
            Thread.sleep(1000);

            // 4. SÉLECTION APRÈS MISE À JOUR
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║         📊 VÉRIFICATION APRÈS MISE À JOUR                  ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            test.selectProcess();
            Thread.sleep(1000);

            // 5. SUPPRESSION
            test.deleteProcess();

            // Résumé final
            System.out.println("\n");
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║                                                            ║");
            System.out.println("║              ✅ TOUS LES TESTS TERMINÉS                    ║");
            System.out.println("║                                                            ║");
            System.out.println("║  Processus exécutés :                                      ║");
            System.out.println("║    ✓ Insertion complète (Patient → Certificat)            ║");
            System.out.println("║    ✓ Sélection (Lecture de toutes les données)            ║");
            System.out.println("║    ✓ Mise à jour (Modification des entités)               ║");
            System.out.println("║    ✓ Suppression (Nettoyage complet)                      ║");
            System.out.println("║                                                            ║");
            System.out.println("║  Flux métier testé :                                       ║");
            System.out.println("║    Patient → Dossier → RDV → Consultation →               ║");
            System.out.println("║    Actes → Intervention → Facture → SF →                  ║");
            System.out.println("║    Ordonnance → Certificat                                ║");
            System.out.println("║                                                            ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println("\n");

        } catch (Exception e) {
            System.err.println("\n❌ ERREUR CRITIQUE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}