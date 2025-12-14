package ma.dentaluxe.service.TestOthmane;

import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.AntecedentRepositoryImpl;
import ma.dentaluxe.service.patient.api.AntecedentService;
import ma.dentaluxe.service.patient.baseImplimentation.AntecedentServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TestAntecedent {

    private static AntecedentService antecedentService;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Initialisation
        AntecedentRepository antecedentRepository = new AntecedentRepositoryImpl();
        antecedentService = new AntecedentServiceImpl(antecedentRepository);
        scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║      TEST ANTECEDENT SERVICE - MENU INTERACTIF ║");
        System.out.println("╚════════════════════════════════════════════════╝\n");

        boolean continuer = true;

        while (continuer) {
            afficherMenu();
            int choix = lireChoix();

            switch (choix) {
                case 1 -> testCreate();
                case 2 -> testFindAll();
                case 3 -> testFindById();
                case 4 -> testUpdate();
                case 5 -> testDelete();
                case 6 -> testFindByNom();
                case 7 -> testFindByCategorie();
                case 8 -> testFindByNiveauRisque();
                case 9 -> testStatistics();
                case 10 -> testCount();
                case 11 -> testExistsById();
                case 12 -> testPagination();
                case 0 -> {
                    System.out.println("\n👋 Au revoir !");
                    continuer = false;
                }
                default -> System.out.println("\n❌ Choix invalide !");
            }

            if (continuer) {
                System.out.println("\nAppuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void afficherMenu() {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║                  MENU PRINCIPAL                ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("║  1.   CREATE - Créer un antécédent          ║");
        System.out.println("║  2.   FIND ALL - Afficher tous               ║");
        System.out.println("║  3.   FIND BY ID - Rechercher par ID         ║");
        System.out.println("║  4.   UPDATE - Modifier un antécédent       ║");
        System.out.println("║  5.   DELETE - Supprimer un antécédent      ║");
        System.out.println("║  6.   FIND BY NOM - Rechercher par nom       ║");
        System.out.println("║  7.   FIND BY CATEGORIE - Par catégorie      ║");
        System.out.println("║  8.   FIND BY RISQUE - Par niveau risque    ║");
        System.out.println("║  9.   STATISTICS - Voir statistiques         ║");
        System.out.println("║  10.  COUNT - Compter les antécédents        ║");
        System.out.println("║  11.  EXISTS - Vérifier existence            ║");
        System.out.println("║  12.  PAGINATION - Afficher avec pagination  ║");
        System.out.println("║  0.   QUITTER                                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.print("\n👉 Votre choix: ");
    }

    private static int lireChoix() {
        try {
            int choix = Integer.parseInt(scanner.nextLine());
            return choix;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ==================== TEST 1: CREATE ====================
    private static void testCreate() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│     ➕ CREATE - Créer un antécédent     │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Nom de l'antécédent: ");
            String nom = scanner.nextLine();

            System.out.println("\nChoisissez la catégorie:");
            System.out.println("1. MEDICAL");
            System.out.println("2. CHIRURGICAL");
            System.out.println("3. FAMILIAL");
            System.out.println("4. AUTRE");
            System.out.print("Votre choix: ");
            int catChoix = Integer.parseInt(scanner.nextLine());

            CategorieAntecedent categorie = switch (catChoix) {
                case 1 -> CategorieAntecedent.MEDICAL;
                case 2 -> CategorieAntecedent.CHIRURGICAL;
                case 3 -> CategorieAntecedent.FAMILIAL;
                case 4 -> CategorieAntecedent.AUTRE;
                default -> CategorieAntecedent.MEDICAL;
            };

            System.out.println("\nChoisissez le niveau de risque:");
            System.out.println("1. FAIBLE");
            System.out.println("2. MOYEN");
            System.out.println("3. ELEVE");
            System.out.println("4. CRITIQUE");
            System.out.print("Votre choix: ");
            int risqueChoix = Integer.parseInt(scanner.nextLine());

            NiveauRisque niveauRisque = switch (risqueChoix) {
                case 1 -> NiveauRisque.FAIBLE;
                case 2 -> NiveauRisque.MOYEN;
                case 3 -> NiveauRisque.ELEVE;
                case 4 -> NiveauRisque.CRITIQUE;
                default -> NiveauRisque.MOYEN;
            };

            Antecedent antecedent = Antecedent.builder()
                    .nom(nom)
                    .categorie(categorie)
                    .niveauRisque(niveauRisque)
                    .dateCreation(LocalDate.now())
                    .build();

            antecedentService.create(antecedent);

            System.out.println("\n✅ Antécédent créé avec succès !");
            System.out.println("   ID: " + antecedent.getId());
            System.out.println("   Nom: " + antecedent.getNom());
            System.out.println("   Catégorie: " + antecedent.getCategorie());
            System.out.println("   Niveau de risque: " + antecedent.getNiveauRisque());

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 2: FIND ALL ====================
    private static void testFindAll() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│     📋 FIND ALL - Afficher tous         │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            List<Antecedent> antecedents = antecedentService.findAll();

            if (antecedents.isEmpty()) {
                System.out.println("❌ Aucun antécédent trouvé.");
            } else {
                System.out.println("📋 Total: " + antecedents.size() + " antécédent(s)\n");
                System.out.println("┌─────┬─────────────────────┬──────────────┬────────────────┐");
                System.out.println("│ ID  │ Nom                 │ Catégorie    │ Niveau Risque  │");
                System.out.println("├─────┼─────────────────────┼──────────────┼────────────────┤");

                for (Antecedent a : antecedents) {
                    System.out.printf("│ %-3d │ %-19s │ %-12s │ %-14s │%n",
                            a.getId(),
                            a.getNom().length() > 19 ? a.getNom().substring(0, 16) + "..." : a.getNom(),
                            a.getCategorie(),
                            a.getNiveauRisque());
                }
                System.out.println("└─────┴─────────────────────┴──────────────┴────────────────┘");
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 3: FIND BY ID ====================
    private static void testFindById() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│   🔍 FIND BY ID - Rechercher par ID     │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Entrez l'ID de l'antécédent: ");
            Long id = Long.parseLong(scanner.nextLine());

            Antecedent antecedent = antecedentService.findById(id);

            System.out.println("\n✅ Antécédent trouvé:");
            System.out.println("   ID: " + antecedent.getId());
            System.out.println("   Nom: " + antecedent.getNom());
            System.out.println("   Catégorie: " + antecedent.getCategorie());
            System.out.println("   Niveau de risque: " + antecedent.getNiveauRisque());
            System.out.println("   Date création: " + antecedent.getDateCreation());

        } catch (NumberFormatException e) {
            System.out.println("\n❌ ID invalide !");
        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 4: UPDATE ====================
    private static void testUpdate() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│   ✏️  UPDATE - Modifier un antécédent   │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Entrez l'ID de l'antécédent à modifier: ");
            Long id = Long.parseLong(scanner.nextLine());

            Antecedent antecedent = antecedentService.findById(id);

            System.out.println("\n📋 Antécédent actuel:");
            System.out.println("   Nom: " + antecedent.getNom());
            System.out.println("   Catégorie: " + antecedent.getCategorie());
            System.out.println("   Niveau de risque: " + antecedent.getNiveauRisque());

            System.out.print("\nNouveau nom (ou Entrée pour garder): ");
            String nouveauNom = scanner.nextLine();
            if (!nouveauNom.trim().isEmpty()) {
                antecedent.setNom(nouveauNom);
            }

            System.out.println("\nNouveau niveau de risque:");
            System.out.println("1. FAIBLE");
            System.out.println("2. MOYEN");
            System.out.println("3. ELEVE");
            System.out.println("4. CRITIQUE");
            System.out.println("0. Ne pas modifier");
            System.out.print("Votre choix: ");
            int risqueChoix = Integer.parseInt(scanner.nextLine());

            if (risqueChoix != 0) {
                NiveauRisque niveauRisque = switch (risqueChoix) {
                    case 1 -> NiveauRisque.FAIBLE;
                    case 2 -> NiveauRisque.MOYEN;
                    case 3 -> NiveauRisque.ELEVE;
                    case 4 -> NiveauRisque.CRITIQUE;
                    default -> antecedent.getNiveauRisque();
                };
                antecedent.setNiveauRisque(niveauRisque);
            }

            antecedentService.update(id, antecedent);

            System.out.println("\n✅ Antécédent modifié avec succès !");
            System.out.println("   Nouveau nom: " + antecedent.getNom());
            System.out.println("   Nouveau niveau: " + antecedent.getNiveauRisque());

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 5: DELETE ====================
    private static void testDelete() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  🗑️  DELETE - Supprimer un antécédent   │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Entrez l'ID de l'antécédent à supprimer: ");
            Long id = Long.parseLong(scanner.nextLine());

            Antecedent antecedent = antecedentService.findById(id);

            System.out.println("\n⚠️  Voulez-vous vraiment supprimer cet antécédent ?");
            System.out.println("   Nom: " + antecedent.getNom());
            System.out.println("   Catégorie: " + antecedent.getCategorie());
            System.out.print("\nConfirmer (O/N): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("O") || confirmation.equalsIgnoreCase("OUI")) {
                antecedentService.delete(id);
                System.out.println("\n✅ Antécédent supprimé avec succès !");
            } else {
                System.out.println("\n❌ Suppression annulée.");
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 6: FIND BY NOM ====================
    private static void testFindByNom() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  🔎 FIND BY NOM - Rechercher par nom    │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Entrez le nom à rechercher: ");
            String nom = scanner.nextLine();

            List<Antecedent> resultats = antecedentService.findByNom(nom);

            if (resultats.isEmpty()) {
                System.out.println("\n❌ Aucun antécédent trouvé avec ce nom.");
            } else {
                System.out.println("\n📋 Résultats: " + resultats.size() + " antécédent(s)\n");
                for (Antecedent a : resultats) {
                    System.out.println("   - " + a.getNom() + " (" + a.getCategorie() + " - " + a.getNiveauRisque() + ")");
                }
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 7: FIND BY CATEGORIE ====================
    private static void testFindByCategorie() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│ 📂 FIND BY CATEGORIE - Par catégorie    │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.println("Choisissez la catégorie:");
            System.out.println("1. MEDICAL");
            System.out.println("2. CHIRURGICAL");
            System.out.println("3. FAMILIAL");
            System.out.println("4. AUTRE");
            System.out.print("Votre choix: ");
            int choix = Integer.parseInt(scanner.nextLine());

            CategorieAntecedent categorie = switch (choix) {
                case 1 -> CategorieAntecedent.MEDICAL;
                case 2 -> CategorieAntecedent.CHIRURGICAL;
                case 3 -> CategorieAntecedent.FAMILIAL;
                case 4 -> CategorieAntecedent.AUTRE;
                default -> null;
            };

            if (categorie != null) {
                List<Antecedent> resultats = antecedentService.findByCategorie(categorie);

                if (resultats.isEmpty()) {
                    System.out.println("\n❌ Aucun antécédent trouvé dans cette catégorie.");
                } else {
                    System.out.println("\n📋 Résultats (" + categorie + "): " + resultats.size() + " antécédent(s)\n");
                    for (Antecedent a : resultats) {
                        System.out.println("   - " + a.getNom() + " (Risque: " + a.getNiveauRisque() + ")");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 8: FIND BY NIVEAU RISQUE ====================
    private static void testFindByNiveauRisque() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│ ⚠️  FIND BY RISQUE - Par niveau risque  │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.println("Choisissez le niveau de risque:");
            System.out.println("1. FAIBLE");
            System.out.println("2. MOYEN");
            System.out.println("3. ELEVE");
            System.out.println("4. CRITIQUE");
            System.out.print("Votre choix: ");
            int choix = Integer.parseInt(scanner.nextLine());

            NiveauRisque niveauRisque = switch (choix) {
                case 1 -> NiveauRisque.FAIBLE;
                case 2 -> NiveauRisque.MOYEN;
                case 3 -> NiveauRisque.ELEVE;
                case 4 -> NiveauRisque.CRITIQUE;
                default -> null;
            };

            if (niveauRisque != null) {
                List<Antecedent> resultats = antecedentService.findByNiveauRisque(niveauRisque);

                if (resultats.isEmpty()) {
                    System.out.println("\n❌ Aucun antécédent trouvé avec ce niveau de risque.");
                } else {
                    System.out.println("\n📋 Résultats (" + niveauRisque + "): " + resultats.size() + " antécédent(s)\n");
                    for (Antecedent a : resultats) {
                        System.out.println("   - " + a.getNom() + " (" + a.getCategorie() + ")");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 9: STATISTICS ====================
    private static void testStatistics() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  📊 STATISTICS - Voir statistiques      │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            String stats = antecedentService.getStatistics();
            System.out.println(stats);

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 10: COUNT ====================
    private static void testCount() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  🔢 COUNT - Compter les antécédents     │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            long count = antecedentService.count();
            System.out.println("📊 Nombre total d'antécédents: " + count);

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 11: EXISTS BY ID ====================
    private static void testExistsById() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│  ✅ EXISTS - Vérifier existence         │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Entrez l'ID à vérifier: ");
            Long id = Long.parseLong(scanner.nextLine());

            boolean existe = antecedentService.existsById(id);

            if (existe) {
                System.out.println("\n✅ L'antécédent avec l'ID " + id + " existe.");
            } else {
                System.out.println("\n❌ L'antécédent avec l'ID " + id + " n'existe pas.");
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }

    // ==================== TEST 12: PAGINATION ====================
    private static void testPagination() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│ 📄 PAGINATION - Afficher par pages     │");
        System.out.println("└─────────────────────────────────────────┘\n");

        try {
            System.out.print("Numéro de page (0 = première page): ");
            int page = Integer.parseInt(scanner.nextLine());

            System.out.print("Nombre d'éléments par page: ");
            int size = Integer.parseInt(scanner.nextLine());

            List<Antecedent> resultats = antecedentService.findWithPagination(page, size);

            if (resultats.isEmpty()) {
                System.out.println("\n❌ Aucun résultat sur cette page.");
            } else {
                System.out.println("\n📄 Page " + page + " (" + resultats.size() + " élément(s)):\n");
                for (Antecedent a : resultats) {
                    System.out.println("   - " + a.getNom() + " (" + a.getCategorie() + ")");
                }
            }

        } catch (Exception e) {
            System.out.println("\n❌ Erreur: " + e.getMessage());
        }
    }
}