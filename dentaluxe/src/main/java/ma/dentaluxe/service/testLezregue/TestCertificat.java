package ma.dentaluxe.service.testLezregue;

import ma.dentaluxe.service.certificat.dto.CertificatDTO;
import ma.dentaluxe.repository.modules.certificat.api.CertificatRepository;
import ma.dentaluxe.repository.modules.certificat.inMemDB_implementation.CertificatRepositoryImpl;
import ma.dentaluxe.service.certificat.api.CertificatService;
import ma.dentaluxe.service.certificat.baseImplementation.CertificatServiceImpl;

import java.time.LocalDate;
import java.util.List;

/**
 * AUTEUR : AYA LEZREGUE
 * Classe de test complète pour CertificatService
 */
public class TestCertificat {

    private CertificatService certificatService;
    private CertificatRepository certificatRepository;

    public TestCertificat() {
        this.certificatRepository = new CertificatRepositoryImpl();
        this.certificatService = new CertificatServiceImpl(certificatRepository);
    }

    public void runAllTests() {
        System.out.println("\n========================================");
        System.out.println("🧪 TESTS DU SERVICE CERTIFICAT");
        System.out.println("========================================\n");

        testCreateCertificat();
        testGetCertificatById();
        testGetAllCertificats();
        testUpdateCertificat();
        testGetCertificatsByDossierMedical();
        testGetCertificatsByMedecin();
        testGetCertificatsByDateDebutBetween();
        testGetCertificatsByDateFinBetween();
        testGetCertificatsActifs();
        testGetCertificatsExpires();
        testGetCertificatsAVenir();
        testIsCertificatValide();
        testCalculateDuree();
        testStatistiques();
        testDeleteCertificat();

        System.out.println("\n========================================");
        System.out.println("✅ TOUS LES TESTS CERTIFICAT TERMINÉS");
        System.out.println("========================================\n");
    }

    private void testCreateCertificat() {
        System.out.println("📝 Test 1: Création de certificats");
        try {
            // Certificat actif (en cours)
            CertificatDTO cert1 = CertificatDTO.builder()
                    .idDM(1L)
                    .idMedecin(1L)
                    .dateDebut(LocalDate.now().minusDays(5))
                    .dateFin(LocalDate.now().plusDays(5))
                    .noteMedecin("Repos complet recommandé après intervention")
                    .build();
            certificatService.createCertificat(cert1);
            System.out.println("   ✓ Certificat actif créé (DM:1, durée calculée automatiquement)");

            // Certificat futur (commence dans 10 jours)
            CertificatDTO cert2 = CertificatDTO.builder()
                    .idDM(2L)
                    .idMedecin(1L)
                    .dateDebut(LocalDate.now().plusDays(10))
                    .dateFin(LocalDate.now().plusDays(17))
                    .noteMedecin("Arrêt post-opératoire programmé")
                    .build();
            certificatService.createCertificat(cert2);
            System.out.println("   ✓ Certificat futur créé (DM:2)");

            // Certificat expiré
            CertificatDTO cert3 = CertificatDTO.builder()
                    .idDM(1L)
                    .idMedecin(2L)
                    .dateDebut(LocalDate.now().minusDays(30))
                    .dateFin(LocalDate.now().minusDays(23))
                    .noteMedecin("Certificat médical général - période écoulée")
                    .build();
            certificatService.createCertificat(cert3);
            System.out.println("   ✓ Certificat expiré créé (DM:1, historique)");

            // Certificat de longue durée
            CertificatDTO cert4 = CertificatDTO.builder()
                    .idDM(3L)
                    .idMedecin(2L)
                    .dateDebut(LocalDate.now().minusDays(10))
                    .dateFin(LocalDate.now().plusDays(20))
                    .noteMedecin("Repos prolongé - traitement orthodontique")
                    .build();
            certificatService.createCertificat(cert4);
            System.out.println("   ✓ Certificat longue durée créé (DM:3)");

            System.out.println("\n✅ Test réussi : 4 certificats créés\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatById() {
        System.out.println("📝 Test 2: Récupération d'un certificat par ID");
        try {
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            if (!certificats.isEmpty()) {
                Long id = certificats.get(0).getIdCertif();
                CertificatDTO cert = certificatService.getCertificatById(id);
                System.out.println("   📋 Certificat ID: " + cert.getIdCertif());
                System.out.println("   Dossier médical : " + cert.getIdDM());
                System.out.println("   Médecin : " + cert.getIdMedecin());
                System.out.println("   Période : du " + cert.getDateDebut() + " au " + cert.getDateFin());
                System.out.println("   Durée : " + cert.getDuree() + " jours");
                System.out.println("   Note : " + cert.getNoteMedecin());
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetAllCertificats() {
        System.out.println("📝 Test 3: Récupération de tous les certificats");
        try {
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            System.out.println("   Nombre total de certificats : " + certificats.size());
            System.out.println("   Liste des certificats :");
            certificats.forEach(c -> System.out.println("   - ID:" + c.getIdCertif() + " | DM:" + c.getIdDM() +
                    " | Du " + c.getDateDebut() + " au " + c.getDateFin() +
                    " (" + c.getDuree() + " jours)"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testUpdateCertificat() {
        System.out.println("📝 Test 4: Mise à jour d'un certificat");
        try {
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            if (!certificats.isEmpty()) {
                CertificatDTO cert = certificats.get(0);
                System.out.println("   Certificat ID: " + cert.getIdCertif());
                System.out.println("   Avant modification :");
                System.out.println("     - Période : " + cert.getDateDebut() + " au " + cert.getDateFin());
                System.out.println("     - Durée : " + cert.getDuree() + " jours");
                System.out.println("     - Note : " + cert.getNoteMedecin());

                // Prolonger le certificat
                cert.setDateFin(cert.getDateFin().plusDays(3));
                cert.setNoteMedecin("Prolongation nécessaire - évolution favorable mais repos encore requis");
                certificatService.updateCertificat(cert);

                CertificatDTO certUpdated = certificatService.getCertificatById(cert.getIdCertif());
                System.out.println("\n   Après modification :");
                System.out.println("     - Période : " + certUpdated.getDateDebut() + " au " + certUpdated.getDateFin());
                System.out.println("     - Durée : " + certUpdated.getDuree() + " jours (recalculée automatiquement)");
                System.out.println("     - Note : " + certUpdated.getNoteMedecin());
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsByDossierMedical() {
        System.out.println("📝 Test 5: Filtrage par dossier médical");
        try {
            System.out.println("   Certificats du dossier médical 1 :");
            List<CertificatDTO> certificats1 = certificatService.getCertificatsByDossierMedical(1L);
            System.out.println("   Nombre : " + certificats1.size());
            certificats1.forEach(c -> System.out.println("     - Du " + c.getDateDebut() + " au " +
                    c.getDateFin() + " (" + c.getDuree() + " jours) | Médecin: " + c.getIdMedecin()));

            System.out.println("\n   Certificats du dossier médical 2 :");
            List<CertificatDTO> certificats2 = certificatService.getCertificatsByDossierMedical(2L);
            System.out.println("   Nombre : " + certificats2.size());
            certificats2.forEach(c -> System.out.println("     - Du " + c.getDateDebut() + " au " +
                    c.getDateFin() + " (" + c.getDuree() + " jours)"));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsByMedecin() {
        System.out.println("📝 Test 6: Filtrage par médecin");
        try {
            System.out.println("   Certificats émis par médecin 1 :");
            List<CertificatDTO> certificats1 = certificatService.getCertificatsByMedecin(1L);
            System.out.println("   Nombre : " + certificats1.size());
            certificats1.forEach(c -> System.out.println("     - DM:" + c.getIdDM() + " | Du " +
                    c.getDateDebut() + " au " + c.getDateFin()));

            System.out.println("\n   Certificats émis par médecin 2 :");
            List<CertificatDTO> certificats2 = certificatService.getCertificatsByMedecin(2L);
            System.out.println("   Nombre : " + certificats2.size());
            certificats2.forEach(c -> System.out.println("     - DM:" + c.getIdDM() + " | Du " +
                    c.getDateDebut() + " au " + c.getDateFin()));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsByDateDebutBetween() {
        System.out.println("📝 Test 7: Filtrage par plage de dates de début");
        try {
            LocalDate debut = LocalDate.now().minusDays(40);
            LocalDate fin = LocalDate.now().plusDays(5);
            List<CertificatDTO> certificats = certificatService.getCertificatsByDateDebutBetween(debut, fin);
            System.out.println("   Certificats commençant entre " + debut + " et " + fin);
            System.out.println("   Nombre trouvé : " + certificats.size());
            certificats.forEach(c -> System.out.println("     - Début : " + c.getDateDebut() +
                    " | Fin : " + c.getDateFin() + " | DM:" + c.getIdDM()));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsByDateFinBetween() {
        System.out.println("📝 Test 8: Filtrage par plage de dates de fin");
        try {
            LocalDate debut = LocalDate.now().minusDays(30);
            LocalDate fin = LocalDate.now().plusDays(20);
            List<CertificatDTO> certificats = certificatService.getCertificatsByDateFinBetween(debut, fin);
            System.out.println("   Certificats se terminant entre " + debut + " et " + fin);
            System.out.println("   Nombre trouvé : " + certificats.size());
            certificats.forEach(c -> System.out.println("     - Début : " + c.getDateDebut() +
                    " | Fin : " + c.getDateFin() + " | DM:" + c.getIdDM()));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsActifs() {
        System.out.println("📝 Test 9: Certificats actifs");
        try {
            System.out.println("   Certificats actifs aujourd'hui (" + LocalDate.now() + ") :");
            List<CertificatDTO> certificats = certificatService.getCertificatsActifs();
            System.out.println("   Nombre : " + certificats.size());
            certificats.forEach(c -> System.out.println("     - DM:" + c.getIdDM() + " | Du " +
                    c.getDateDebut() + " au " + c.getDateFin() + " (" + c.getDuree() + " jours)"));

            // Test avec une date spécifique
            LocalDate dateTest = LocalDate.now().minusDays(3);
            List<CertificatDTO> certificatsDate = certificatService.getCertificatsActifsAtDate(dateTest);
            System.out.println("\n   Certificats actifs le " + dateTest + " : " + certificatsDate.size());
            certificatsDate.forEach(c -> System.out.println("     - DM:" + c.getIdDM() +
                    " | " + c.getDateDebut() + " → " + c.getDateFin()));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsExpires() {
        System.out.println("📝 Test 10: Certificats expirés");
        try {
            List<CertificatDTO> certificats = certificatService.getCertificatsExpires();
            System.out.println("   Certificats expirés (terminés) : " + certificats.size());
            certificats.forEach(c -> {
                long joursExpires = java.time.temporal.ChronoUnit.DAYS.between(c.getDateFin(), LocalDate.now());
                System.out.println("     - DM:" + c.getIdDM() + " | Du " + c.getDateDebut() +
                        " au " + c.getDateFin() + " (expiré depuis " + joursExpires + " jours)");
            });
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetCertificatsAVenir() {
        System.out.println("📝 Test 11: Certificats à venir");
        try {
            List<CertificatDTO> certificats = certificatService.getCertificatsAVenir();
            System.out.println("   Certificats à venir (pas encore commencés) : " + certificats.size());
            certificats.forEach(c -> {
                long joursDansAttente = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), c.getDateDebut());
                System.out.println("     - DM:" + c.getIdDM() + " | Du " + c.getDateDebut() +
                        " au " + c.getDateFin() + " (commence dans " + joursDansAttente + " jours)");
            });
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testIsCertificatValide() {
        System.out.println("📝 Test 12: Validation de certificats");
        try {
            List<CertificatDTO> certificats = certificatService.getAllCertificats();

            System.out.println("   Vérification de validité (actif aujourd'hui) :");
            for (CertificatDTO cert : certificats) {
                boolean valide = certificatService.isCertificatValide(cert.getIdCertif());
                String status = valide ? "✓ ACTIF" : "✗ INACTIF";
                String periode = cert.getDateDebut() + " → " + cert.getDateFin();
                System.out.println("     " + status + " | ID:" + cert.getIdCertif() +
                        " | DM:" + cert.getIdDM() + " | " + periode);
            }

            // Test avec une date spécifique
            if (!certificats.isEmpty()) {
                CertificatDTO cert = certificats.get(0);
                LocalDate dateTest = cert.getDateDebut().plusDays(1);
                boolean actifDate = certificatService.isCertificatActifAtDate(cert.getIdCertif(), dateTest);
                System.out.println("\n   Test date spécifique :");
                System.out.println("     Certificat " + cert.getIdCertif() + " actif le " +
                        dateTest + " : " + (actifDate ? "✓ OUI" : "✗ NON"));
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testCalculateDuree() {
        System.out.println("📝 Test 13: Calcul de durée");
        try {
            // Test calcul manuel
            LocalDate debut = LocalDate.of(2024, 1, 1);
            LocalDate fin = LocalDate.of(2024, 1, 7);
            int duree = certificatService.calculateDuree(debut, fin);
            System.out.println("   Calcul manuel :");
            System.out.println("     Du " + debut + " au " + fin + " = " + duree + " jours");

            // Vérifier que la durée est calculée automatiquement
            System.out.println("\n   Vérification calcul automatique :");
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            for (CertificatDTO cert : certificats.stream().limit(3).toList()) {
                int dureeCalculee = certificatService.calculateDuree(cert.getDateDebut(), cert.getDateFin());
                boolean coherent = (cert.getDuree() == dureeCalculee);
                System.out.println("     Certificat " + cert.getIdCertif() + " : durée stockée = " +
                        cert.getDuree() + ", durée calculée = " + dureeCalculee +
                        " " + (coherent ? "✓" : "✗ INCOHÉRENT"));
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testStatistiques() {
        System.out.println("📝 Test 14: Statistiques");
        try {
            System.out.println("   📊 STATISTIQUES GLOBALES");
            System.out.println("   ────────────────────────");
            System.out.println("   Nombre total de certificats : " + certificatService.countAllCertificats());

            System.out.println("\n   📊 PAR DOSSIER MÉDICAL");
            System.out.println("   ──────────────────────");
            System.out.println("   Certificats DM 1 : " + certificatService.countCertificatsByDossierMedical(1L));
            System.out.println("   Certificats DM 2 : " + certificatService.countCertificatsByDossierMedical(2L));
            System.out.println("   Certificats DM 3 : " + certificatService.countCertificatsByDossierMedical(3L));

            System.out.println("\n   📊 PAR MÉDECIN");
            System.out.println("   ──────────────");
            System.out.println("   Certificats médecin 1 : " + certificatService.countCertificatsByMedecin(1L));
            System.out.println("   Certificats médecin 2 : " + certificatService.countCertificatsByMedecin(2L));

            System.out.println("\n   📊 RÉPARTITION PAR STATUT");
            System.out.println("   ─────────────────────────");
            int actifs = certificatService.getCertificatsActifs().size();
            int expires = certificatService.getCertificatsExpires().size();
            int aVenir = certificatService.getCertificatsAVenir().size();
            int total = actifs + expires + aVenir;

            System.out.println("   Actifs (en cours) : " + actifs + " (" +
                    String.format("%.1f", actifs * 100.0 / total) + "%)");
            System.out.println("   Expirés (terminés) : " + expires + " (" +
                    String.format("%.1f", expires * 100.0 / total) + "%)");
            System.out.println("   À venir : " + aVenir + " (" +
                    String.format("%.1f", aVenir * 100.0 / total) + "%)");

            // Durée moyenne
            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            if (!certificats.isEmpty()) {
                double dureeMoyenne = certificats.stream()
                        .mapToInt(CertificatDTO::getDuree)
                        .average()
                        .orElse(0.0);
                int dureeMin = certificats.stream()
                        .mapToInt(CertificatDTO::getDuree)
                        .min()
                        .orElse(0);
                int dureeMax = certificats.stream()
                        .mapToInt(CertificatDTO::getDuree)
                        .max()
                        .orElse(0);

                System.out.println("\n   📊 ANALYSE DES DURÉES");
                System.out.println("   ────────────────────");
                System.out.println("   Durée moyenne : " + String.format("%.1f", dureeMoyenne) + " jours");
                System.out.println("   Durée minimale : " + dureeMin + " jours");
                System.out.println("   Durée maximale : " + dureeMax + " jours");
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testDeleteCertificat() {
        System.out.println("📝 Test 15: Suppression de certificat");
        try {
            int countAvant = (int) certificatService.countAllCertificats();
            System.out.println("   Nombre de certificats avant suppression : " + countAvant);

            List<CertificatDTO> certificats = certificatService.getAllCertificats();
            if (!certificats.isEmpty()) {
                CertificatDTO certToDelete = certificats.get(certificats.size() - 1);
                System.out.println("   Suppression du certificat ID:" + certToDelete.getIdCertif() +
                        " (DM:" + certToDelete.getIdDM() + ")");
                System.out.println("     Période : " + certToDelete.getDateDebut() + " → " + certToDelete.getDateFin());

                certificatService.deleteCertificatById(certToDelete.getIdCertif());

                int countApres = (int) certificatService.countAllCertificats();
                System.out.println("   Nombre de certificats après suppression : " + countApres);
                System.out.println("   Différence : " + (countAvant - countApres) + " certificat supprimé");
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        TestCertificat test = new TestCertificat();
        test.runAllTests();
    }
}