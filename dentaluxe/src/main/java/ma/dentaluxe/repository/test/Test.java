
package ma.dentaluxe.repository.test;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.api.FactureRepository;
import ma.dentaluxe.repository.modules.caisse.api.SituationFinanciereRepository;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.FactureRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.SituationFinanciereRepositoryImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Test {

    //AUTEURS : AYA LEZREGUE


//classe pour tester tous les repository
//Teste les opérations CRUD et les méthodes spécifiques



// Instances des repositories
private ActeRepository acteRepository;
private RDVRepository rdvRepository;
private FactureRepository factureRepository;
private SituationFinanciereRepository  situationFinanciereRepository;


// Initialisation des repositories(par le constructeur)
public Test() {
 this.acteRepository = new ActeRepositoryImpl();
 this.rdvRepository = new RDVRepositoryImpl();
 this.factureRepository = new FactureRepositoryImpl();
 this.situationFinanciereRepository = new SituationFinanciereRepositoryImpl();
}

//TESTS ACTE REPOSITORY
    //teste 1: creation des actes
void testCreateActes() {
    System.out.println("\n========== TEST 1 : Création des Actes ==========");
//acte 1:
    //creation acte par builder
    Acte acte1 = Acte.builder()
            .idInterventionMedecin(1L)
            .libelle("Consultation générale")
            .description("Consultation dentaire de contrôle")
            .prixDeBase(200.0)
            .categorie(CategorieActe.CONSULTATION)
            .build();
    acteRepository.create(acte1);
    System.out.println("Acte créé : " + acte1.getLibelle() + " (ID: " + acte1.getIdActe() + ")");

//acte2:
    Acte acte2 = Acte.builder()
            .idInterventionMedecin(1L)
            .libelle("Extraction dentaire simple")
            .description("Extraction d'une dent simple")
            .prixDeBase(300.0)
            .categorie(CategorieActe.EXTRACTION)
            .build();
    acteRepository.create(acte2);
    System.out.println("Acte créé : " + acte2.getLibelle() + " (ID: " + acte2.getIdActe() + ")");

//acte3:
    Acte acte3 = Acte.builder()
            .idInterventionMedecin(1L)
            .libelle("Détartrage complet")
            .description("Nettoyage et détartrage des dents")
            .prixDeBase(400.0)
            .categorie(CategorieActe.DETARTRAGE)
            .build();
    acteRepository.create(acte3);
    System.out.println("Acte créé : " + acte3.getLibelle() + " (ID: " + acte3.getIdActe() + ")");


}
    // Test 2 : Lire tous les actes
    void testFindAllActes() {
    System.out.println("\n========== TEST 2 : Lire tous les Actes ==========");
        List<Acte> actes = acteRepository.findAll();
        System.out.println("Nombre total d'actes : " + actes.size());

        for (Acte acte : actes) {
            System.out.println("  - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH (" + acte.getCategorie() + ")");
        }
    }


   //Test 3 : Trouver un acte par ID
   void testFindActeById() {
       System.out.println("\n========== TEST 3 : Trouver Acte par ID ==========");
        Acte acte = acteRepository.findById(1L);
       if (acte != null) {
           System.out.println("Acte trouvé :");
           System.out.println("   ID : " + acte.getIdActe());
           System.out.println("   Libellé : " + acte.getLibelle());
           System.out.println("   Prix : " + acte.getPrixDeBase() + " DH");
           System.out.println("   Catégorie : " + acte.getCategorie());
       } else {
           System.out.println("Acte non trouvé");
       }
   }

   //Test 4 : Trouver actes par catégorie
   void testFindActesByCategorie() {
       System.out.println("\n========== TEST 4 : Trouver Actes par Catégorie ==========");

       List<Acte> consultations = acteRepository.findByCategorie(CategorieActe.CONSULTATION);
       System.out.println("Actes de type CONSULTATION : " + consultations.size());
       for (Acte acte : consultations) {
           System.out.println("  - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
       }

       List<Acte> extractions = acteRepository.findByCategorie(CategorieActe.EXTRACTION);
       System.out.println("Actes de type EXTRACTION : " + extractions.size());
       for (Acte acte : extractions) {
           System.out.println("  - " + acte.getLibelle() + " : " + acte.getPrixDeBase() + " DH");
       }
   }

   //Test 5 : Rechercher un acte par mot-clé
   void testSearchActeByLibelle() {
       System.out.println("\n========== TEST 5 : Rechercher Acte par mot-clé ==========");

       List<Acte> resultats = acteRepository.searchByLibelle("Consultation générale");
       System.out.println("Recherche 'Consultation générale' : " + resultats.size() + " résultats");
       for (Acte acte : resultats) {
           System.out.println("  - " + acte.getLibelle());
       }
   }

    //Test 6 : Mettre à jour un acte
    void testUpdateActe() {
        System.out.println("\n========== TEST 6 : Mise à jour d'un Acte ==========");

        Acte acte = acteRepository.findById(1L);
        if (acte != null) {
            System.out.println("Avant : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");

            acte.setPrixDeBase(250.0);
            acte.setDescription("Consultation dentaire complète avec radiographie");
            acteRepository.update(acte);

            Acte acteUpdated = acteRepository.findById(1L);
            System.out.println("Après : " + acteUpdated.getLibelle() + " - " + acteUpdated.getPrixDeBase() + " DH");
        }
    }

    //Test 7 : Supprimer un acte
    void testDeleteActe() {
        System.out.println("\n========== TEST 7 : Suppression d'un Acte ==========");
        // Créer un acte temporaire pour le supprimer
        Acte acteTemp = Acte.builder()
                .libelle("Acte temporaire")
                .description("Pour test de suppression")
                .prixDeBase(100.0)
                .categorie(CategorieActe.AUTRE)
                .build();
        acteRepository.create(acteTemp);

        System.out.println("Acte temporaire créé (ID: " + acteTemp.getIdActe() + ")");

        acteRepository.deleteById(acteTemp.getIdActe());
        System.out.println("Acte supprimé (ID: " + acteTemp.getIdActe() + ")");

        Acte acteDeleted = acteRepository.findById(acteTemp.getIdActe());
        if(acteDeleted == null){
            System.out.println(" Confirmation : Acte bien supprimé");
        }
        else {
            System.out.println(" Erreur : Acte toujours présent");
        }

    }


// TESTS RDV REPOSITORY
    //creation des RDV
void testCreateRDV() {
    System.out.println("\n========== TEST 8 : Création des RDV ==========");

    // RDV 1 :
    RDV rdv1 = RDV.builder()
            .idDM(1L)
            .idMedecin(1L)
            .dateRDV(LocalDate.now())
            .heureRDV(LocalTime.of(9, 0))
            .motif("Consultation de contrôle")
            .statut(StatutRDV.PLANIFIE)
            .noteMedecin("Consultation de contrôle")
            .build();
    rdvRepository.create(rdv1);
    System.out.println("RDV créé : " + rdv1.getMotif() + " le " + rdv1.getDateRDV() + " à " + rdv1.getHeureRDV());

    // RDV 2 : Demain
    RDV rdv2 = RDV.builder()
            .idDM(2L)
            .idMedecin(1L)
            .dateRDV(LocalDate.now().plusDays(1))
            .heureRDV(LocalTime.of(10, 30))
            .motif("Extraction dentaire")
            .statut(StatutRDV.CONFIRME)
            .noteMedecin("Extraction dentaire")
            .build();
    rdvRepository.create(rdv2);
    System.out.println(" RDV créé : " + rdv2.getMotif() + " le " + rdv2.getDateRDV() + " à " + rdv2.getHeureRDV());

    // RDV 3 : Dans 2 jours
    RDV rdv3 = RDV.builder()
            .idDM(1L)
            .idMedecin(1L)
            .dateRDV(LocalDate.now().plusDays(2))
            .heureRDV(LocalTime.of(14, 0))
            .motif("Détartrage")
            .statut(StatutRDV.PLANIFIE)
            .noteMedecin("Détartrage")
            .build();
    rdvRepository.create(rdv3);
    System.out.println(" RDV créé : " + rdv3.getMotif() + " le " + rdv3.getDateRDV() + " à " + rdv3.getHeureRDV());
}
   //test 9 : Lire tous les RDV
   void testFindAllRDV() {
       System.out.println("\n========== TEST 9 : Lire tous les RDV ==========");

       List<RDV> rdvs = rdvRepository.findAll();
       System.out.println(" Nombre total de RDV : " + rdvs.size());

       for (RDV rdv : rdvs) {
           System.out.println("  - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                   " : " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
       }
   }
  //Test 10 : Trouver RDV par date
  void testFindRDVByDate() {
      System.out.println("\n========== TEST 10 : Trouver RDV par Date ==========");

      LocalDate today = LocalDate.now();
      List<RDV> rdvsToday = rdvRepository.findByDate(today);
      System.out.println(" RDV aujourd'hui (" + today + ") : " + rdvsToday.size());

      for (RDV rdv : rdvsToday) {
          System.out.println("  - " + rdv.getHeureRDV() + " : " + rdv.getMotif());
      }
  }

   // Test 11 : Trouver RDV par médecin
   void testFindRDVByMedecin() {
       System.out.println("\n========== TEST 11 : Trouver RDV par Médecin ==========");

       List<RDV> rdvsMedecin = rdvRepository.findByMedecinId(1L);
       System.out.println("RDV du médecin (ID: 1) : " + rdvsMedecin.size());

       for (RDV rdv : rdvsMedecin) {
           System.out.println("  - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                   " : " + rdv.getMotif());
       }
   }

   //Test 12 : Trouver RDV par patient
   void testFindRDVByPatient() {
       System.out.println("\n========== TEST 12 : Trouver RDV par Patient ==========");

       List<RDV> rdvsPatient = rdvRepository.findByPatientDossierId(1L);
       System.out.println(" RDV du patient (DM ID: 1) : " + rdvsPatient.size());

       for (RDV rdv : rdvsPatient) {
           System.out.println("  - " + rdv.getDateRDV() + " à " + rdv.getHeureRDV() +
                   " : " + rdv.getMotif() + " (" + rdv.getStatut() + ")");
       }
   }
   //Test 13 : Trouver RDV par statut
   void testFindRDVByStatut() {
       System.out.println("\n========== TEST 13 : Trouver RDV par Statut ==========");

       List<RDV> rdvsPlanifies = rdvRepository.findByStatut(StatutRDV.PLANIFIE);
       System.out.println(" RDV PLANIFIÉS : " + rdvsPlanifies.size());

       List<RDV> rdvsConfirmes = rdvRepository.findByStatut(StatutRDV.CONFIRME);
       System.out.println(" RDV CONFIRMÉS : " + rdvsConfirmes.size());
   }
    //Test 14 : Mettre à jour un RDV
    void testUpdateRDV() {
        System.out.println("\n========== TEST 14 : Mise à jour d'un RDV ==========");

        RDV rdv = rdvRepository.findById(1L);
        if (rdv != null) {
            System.out.println(" Avant : " + rdv.getStatut() + " - " + rdv.getHeureRDV());

            rdv.setStatut(StatutRDV.CONFIRME);
            rdv.setNoteMedecin("Patient confirmé par téléphone");
            rdvRepository.update(rdv);

            RDV rdvUpdated = rdvRepository.findById(1L);
            System.out.println(" Après : " + rdvUpdated.getStatut() + " - Note: " + rdvUpdated.getNoteMedecin());
        }
    }



//TESTS CAISSE (Facture & SituationFinanciere)


   //Test 15 : Créer une situation financière
void testCreateSituationFinanciere() {
    System.out.println("\n========== TEST 15 : Création Situation Financière ==========");

    SituationFinanciere sf = SituationFinanciere.builder()
            .idDM(1L)
            .totalDesActes(1500.0)
            .totalPaye(500.0)
            .resteDu(1000.0)
            .creance(0.0)
            .statut(StatutSituationFinanciere.DEBIT)
            .enPromo(false)
            .build();

    situationFinanciereRepository.create(sf);
    System.out.println(" Situation financière créée (ID: " + sf.getIdSF() + ")");
    System.out.println("   Total actes : " + sf.getTotalDesActes() + " DH");
    System.out.println("   Total payé : " + sf.getTotalPaye() + " DH");
    System.out.println("   Reste dû : " + sf.getResteDu() + " DH");
    System.out.println("   Statut : " + sf.getStatut());
}

    //Test 16 : Créer des factures
    void testCreateFactures() {
        System.out.println("\n========== TEST 16 : Création des Factures ==========");

        // Facture 1 : Payée
        Facture facture1 = Facture.builder()
                .idSF(1L)
                .idConsultation(1L)
                .totalFacture(500.0)
                .totalDesActes(500.0)
                .montantPaye(500.0)
                .reste(0.0)
                .statut(StatutFacture.PAYEE)
                .build();
        factureRepository.create(facture1);
        System.out.println("Facture 1 créée : " + facture1.getTotalFacture() + " DH (" + facture1.getStatut() + ")");

        // Facture 2 : Partiellement payée
        Facture facture2 = Facture.builder()
                .idSF(1L)
                .idConsultation(2L)
                .totalFacture(800.0)
                .totalDesActes(800.0)
                .montantPaye(300.0)
                .reste(500.0)
                .statut(StatutFacture.PARTIELLEMENT_PAYEE)
                .build();
        factureRepository.create(facture2);
        System.out.println("Facture 2 créée : " + facture2.getTotalFacture() + " DH (" + facture2.getStatut() + ")");

        // Facture 3 : En attente
        Facture facture3 = Facture.builder()
                .idSF(1L)
                .idConsultation(3L)
                .totalFacture(600.0)
                .totalDesActes(600.0)
                .montantPaye(0.0)
                .reste(600.0)
                .statut(StatutFacture.EN_ATTENTE)
                .build();
        factureRepository.create(facture3);
        System.out.println("Facture 3 créée : " + facture3.getTotalFacture() + " DH (" + facture3.getStatut() + ")");
    }



    //Test 17 : Lire toutes les factures
    void testFindAllFactures() {
        System.out.println("\n========== TEST 17 : Lire toutes les Factures ==========");

        List<Facture> factures = factureRepository.findAll();
        System.out.println(" Nombre total de factures : " + factures.size());

        double totalFacture = 0;
        double totalPaye = 0;
        double totalReste = 0;

        for (Facture facture : factures) {
            System.out.println("  - Facture #" + facture.getIdFacture() +
                    " : " + facture.getTotalFacture() + " DH (" + facture.getStatut() + ")");
            totalFacture += facture.getTotalFacture();
            totalPaye += facture.getMontantPaye();
            totalReste += facture.getReste();
        }

        System.out.println(" Résumé financier :");
        System.out.println("   Total facturé : " + totalFacture + " DH");
        System.out.println("   Total payé : " + totalPaye + " DH");
        System.out.println("   Total restant : " + totalReste + " DH");
    }

    //Test 19 : Calculer total impayé
    void testCalculateTotalImpaye() {
        System.out.println("\n========== TEST 19 : Calculer Total Impayé ==========");

        double totalImpaye = factureRepository.calculateTotalFacturesImpayees();
        System.out.println(" Total des factures impayées : " + totalImpaye + " DH");
    }

    //Test 20 : Mettre à jour après paiement
    void testUpdateAfterPayment() {
        System.out.println("\n========== TEST 20 : Mise à jour après Paiement ==========");

        SituationFinanciere sf = situationFinanciereRepository.findById(1L);
        if (sf != null) {
            System.out.println(" Avant paiement :");
            System.out.println("   Total payé : " + sf.getTotalPaye() + " DH");
            System.out.println("   Reste dû : " + sf.getResteDu() + " DH");
            System.out.println("   Statut : " + sf.getStatut());

            // Simuler un paiement de 500 DH
            situationFinanciereRepository.updateAfterPayment(sf.getIdSF(), 500.0);

            SituationFinanciere sfUpdated = situationFinanciereRepository.findById(1L);
            System.out.println("\n Après paiement de 500 DH :");
            System.out.println("   Total payé : " + sfUpdated.getTotalPaye() + " DH");
            System.out.println("   Reste dû : " + sfUpdated.getResteDu() + " DH");
            System.out.println("   Statut : " + sfUpdated.getStatut());
        }
    }


//nettoyage des tables
void cleanDatabase() {
    System.out.println(" Nettoyage de la base de données...");

    try (Connection conn = Db.getConnection();
         Statement stmt = conn.createStatement()) {

        // Désactiver temporairement les contraintes FK
        stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

        // Vider les tables dans l'ordre inverse des dépendances
        stmt.execute("TRUNCATE TABLE Facture");
        stmt.execute("TRUNCATE TABLE SituationFinanciere");
        stmt.execute("TRUNCATE TABLE RDV");
        stmt.execute("TRUNCATE TABLE Acte");

        // Réactiver les contraintes
        stmt.execute("SET FOREIGN_KEY_CHECKS = 1");

        System.out.println("Base de données nettoyée");

    } catch (SQLException e) {
        System.err.println(" Erreur lors du nettoyage : " + e.getMessage());
    }
}



//MÉTHODE PRINCIPALE DE TEST(Exécuter tous les tests dans l'ordre)

    void testProcess() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║   TESTS DES REPOSITORIES - CABINET DENTAIRE   ║");
        System.out.println("╚════════════════════════════════════════════════╝");

        try {

            cleanDatabase();
            // ===== TESTS ACTE REPOSITORY =====
            testCreateActes();
            testFindAllActes();
            testFindActeById();
            testFindActesByCategorie();
            testSearchActeByLibelle();
            testUpdateActe();
            testDeleteActe();

            // ===== TESTS RDV REPOSITORY =====
            testCreateRDV();
            testFindAllRDV();
            testFindRDVByDate();
            testFindRDVByMedecin();
            testFindRDVByPatient();
            testFindRDVByStatut();
            testUpdateRDV();

            // ===== TESTS CAISSE =====
            testCreateSituationFinanciere();
            testCreateFactures();
            testFindAllFactures();
            testCalculateTotalImpaye();
            testUpdateAfterPayment();

            System.out.println("\n╔════════════════════════════════════════════════╗");
            System.out.println("║           TOUS LES TESTS TERMINÉS !          ║");
            System.out.println("╚════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n ERREUR lors des tests : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.testProcess();
    }
}





