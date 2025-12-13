package ma.dentaluxe.service.testLezregue;

import ma.dentaluxe.mvc.dto.ActeDTO;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.service.actes.api.ActeService;
import ma.dentaluxe.service.actes.baseImplementation.ActeServiceImpl;

import java.util.List;

/**
 * AUTEUR : AYA LEZREGUE
 * Classe de test complète pour ActeService
 */
public class TestActe {

    private ActeService acteService;
    private ActeRepository acteRepository;

    public TestActe() {
        this.acteRepository = new ActeRepositoryImpl();
        this.acteService = new ActeServiceImpl(acteRepository);
    }

    public void runAllTests() {
        System.out.println("\n========================================");
        System.out.println("🧪 TESTS DU SERVICE ACTE");
        System.out.println("========================================\n");

        testCreateActe();
        testGetActeById();
        testGetAllActes();
        testUpdateActe();
        testSearchActesByLibelle();
        testGetActesByCategorie();
        testGetActesByPriceRange();
        testGetActesSortedByPrice();
        testStatistiques();
        testUpdatePrix();
        testApplyDiscount();
        testApplyDiscountToCategorie();
        testValidation();
        testDeleteActe();

        System.out.println("\n========================================");
        System.out.println("✅ TOUS LES TESTS ACTE TERMINÉS");
        System.out.println("========================================\n");
    }

    private void testCreateActe() {
        System.out.println("📝 Test 1: Création d'actes");
        try {
            ActeDTO acte1 = ActeDTO.builder()
                    .libelle("Détartrage")
                    .description("Nettoyage dentaire complet")
                    .prixDeBase(250.0)
                    .categorie(CategorieActe.DETARTRAGE)
                    .build();
            acteService.createActe(acte1);

            ActeDTO acte2 = ActeDTO.builder()
                    .libelle("Extraction dentaire")
                    .description("Extraction simple")
                    .prixDeBase(400.0)
                    .categorie(CategorieActe.EXTRACTION)
                    .build();
            acteService.createActe(acte2);

            ActeDTO acte3 = ActeDTO.builder()
                    .libelle("Blanchiment dentaire")
                    .description("Blanchiment professionnel")
                    .prixDeBase(1500.0)
                    .categorie(CategorieActe.AUTRE)
                    .build();
            acteService.createActe(acte3);

            ActeDTO acte4 = ActeDTO.builder()
                    .libelle("Pose couronne")
                    .description("Couronne céramique")
                    .prixDeBase(2000.0)
                    .categorie(CategorieActe.AUTRE)
                    .build();
            acteService.createActe(acte4);

            System.out.println("✅ Test réussi : 4 actes créés\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetActeById() {
        System.out.println("📝 Test 2: Récupération d'un acte par ID");
        try {
            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                Long id = actes.get(0).getIdActe();
                ActeDTO acte = acteService.getActeById(id);
                System.out.println("   Acte trouvé : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetAllActes() {
        System.out.println("📝 Test 3: Récupération de tous les actes");
        try {
            List<ActeDTO> actes = acteService.getAllActes();
            System.out.println("   Nombre d'actes : " + actes.size());
            actes.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH (" + a.getCategorie() + ")"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testUpdateActe() {
        System.out.println("📝 Test 4: Mise à jour d'un acte");
        try {
            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                ActeDTO acte = actes.get(0);
                System.out.println("   Avant : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");

                acte.setPrixDeBase(300.0);
                acte.setDescription("Description mise à jour");
                acteService.updateActe(acte);

                ActeDTO acteUpdated = acteService.getActeById(acte.getIdActe());
                System.out.println("   Après : " + acteUpdated.getLibelle() + " - " + acteUpdated.getPrixDeBase() + " DH");
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testSearchActesByLibelle() {
        System.out.println("📝 Test 5: Recherche par libellé");
        try {
            List<ActeDTO> actes = acteService.searchActesByLibelle("dent");
            System.out.println("   Actes contenant 'dent' : " + actes.size());
            actes.forEach(a -> System.out.println("   - " + a.getLibelle()));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetActesByCategorie() {
        System.out.println("📝 Test 6: Filtrage par catégorie");
        try {
            for (CategorieActe categorie : CategorieActe.values()) {
                List<ActeDTO> actes = acteService.getActesByCategorie(categorie);
                if (!actes.isEmpty()) {
                    System.out.println("   " + categorie + " : " + actes.size() + " acte(s)");
                    actes.forEach(a -> System.out.println("      - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));
                }
            }
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetActesByPriceRange() {
        System.out.println("📝 Test 7: Filtrage par plage de prix");
        try {
            List<ActeDTO> actes = acteService.getActesByPriceRange(200.0, 1000.0);
            System.out.println("   Actes entre 200 et 1000 DH : " + actes.size());
            actes.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));
            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testGetActesSortedByPrice() {
        System.out.println("📝 Test 8: Tri par prix");
        try {
            System.out.println("   Tri croissant :");
            List<ActeDTO> actesAsc = acteService.getActesSortedByPrice(true);
            actesAsc.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));

            System.out.println("\n   Tri décroissant :");
            List<ActeDTO> actesDesc = acteService.getActesSortedByPrice(false);
            actesDesc.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testStatistiques() {
        System.out.println("📝 Test 9: Statistiques");
        try {
            System.out.println("   Nombre total d'actes : " + acteService.countActes());
            System.out.println("   Prix moyen : " + String.format("%.2f", acteService.getAveragePrixActes()) + " DH");
            System.out.println("   Prix total : " + String.format("%.2f", acteService.getTotalPrixActes()) + " DH");

            ActeDTO plusCher = acteService.getMostExpensiveActe();
            if (plusCher != null) {
                System.out.println("   Acte le plus cher : " + plusCher.getLibelle() + " - " + plusCher.getPrixDeBase() + " DH");
            }

            ActeDTO moinsCher = acteService.getCheapestActe();
            if (moinsCher != null) {
                System.out.println("   Acte le moins cher : " + moinsCher.getLibelle() + " - " + moinsCher.getPrixDeBase() + " DH");
            }

            System.out.println("\n   Statistiques par catégorie :");
            List<ActeService.CategorieStatDTO> stats = acteService.getStatistiquesByCategorie();
            stats.forEach(s -> {
                System.out.println("   - " + s.getCategorie() + " : " + s.getNombreActes() + " acte(s)");
                System.out.println("     Prix moyen : " + String.format("%.2f", s.getPrixMoyen()) + " DH");
                System.out.println("     Prix total : " + String.format("%.2f", s.getPrixTotal()) + " DH");
            });

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testUpdatePrix() {
        System.out.println("📝 Test 10: Mise à jour du prix");
        try {
            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                ActeDTO acte = actes.get(0);
                System.out.println("   Avant : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");

                acteService.updatePrix(acte.getIdActe(), 350.0);

                ActeDTO acteUpdated = acteService.getActeById(acte.getIdActe());
                System.out.println("   Après : " + acteUpdated.getLibelle() + " - " + acteUpdated.getPrixDeBase() + " DH");
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testApplyDiscount() {
        System.out.println("📝 Test 11: Application d'une remise");
        try {
            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                ActeDTO acte = actes.get(0);
                System.out.println("   Avant remise : " + acte.getLibelle() + " - " + acte.getPrixDeBase() + " DH");

                acteService.applyDiscount(acte.getIdActe(), 10.0); // Remise de 10%

                ActeDTO acteUpdated = acteService.getActeById(acte.getIdActe());
                System.out.println("   Après remise 10% : " + acteUpdated.getLibelle() + " - " + acteUpdated.getPrixDeBase() + " DH");
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testApplyDiscountToCategorie() {
        System.out.println("📝 Test 12: Remise sur une catégorie");
        try {
            System.out.println("   Prix avant remise :");
            List<ActeDTO> actesAvant = acteService.getActesByCategorie(CategorieActe.DETARTRAGE);
            actesAvant.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));

            acteService.applyDiscountToCategorie(CategorieActe.DETARTRAGE, 15.0); // 15% de remise

            System.out.println("\n   Prix après remise de 15% :");
            List<ActeDTO> actesApres = acteService.getActesByCategorie(CategorieActe.DETARTRAGE);
            actesApres.forEach(a -> System.out.println("   - " + a.getLibelle() + " : " + a.getPrixDeBase() + " DH"));

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testValidation() {
        System.out.println("📝 Test 13: Validation");
        try {
            // Test acte valide
            ActeDTO acteValide = ActeDTO.builder()
                    .libelle("Test Acte")
                    .prixDeBase(100.0)
                    .categorie(CategorieActe.CONSULTATION)
                    .build();
            System.out.println("   Acte valide : " + acteService.validateActe(acteValide));

            // Test acte invalide (prix négatif)
            ActeDTO acteInvalide = ActeDTO.builder()
                    .libelle("Test Acte")
                    .prixDeBase(-100.0)
                    .categorie(CategorieActe.CONSULTATION)
                    .build();
            System.out.println("   Acte invalide (prix négatif) : " + acteService.validateActe(acteInvalide));

            // Test existence
            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                Long id = actes.get(0).getIdActe();
                System.out.println("   Acte existe (ID=" + id + ") : " + acteService.acteExists(id));
                System.out.println("   Acte existe (ID=9999) : " + acteService.acteExists(9999L));
            }

            System.out.println("✅ Test réussi\n");
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    private void testDeleteActe() {
        System.out.println("📝 Test 14: Suppression d'acte");
        try {
            int countAvant = acteService.countActes();
            System.out.println("   Nombre d'actes avant suppression : " + countAvant);

            List<ActeDTO> actes = acteService.getAllActes();
            if (!actes.isEmpty()) {
                Long idToDelete = actes.get(actes.size() - 1).getIdActe();
                acteService.deleteActe(idToDelete);

                int countApres = acteService.countActes();
                System.out.println("   Nombre d'actes après suppression : " + countApres);
                System.out.println("✅ Test réussi\n");
            }
        } catch (Exception e) {
            System.out.println("❌ Test échoué : " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        TestActe test = new TestActe();
        test.runAllTests();
    }
}