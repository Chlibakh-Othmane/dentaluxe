package ma.dentaluxe.mvc.controllers.modules.Acte.batch_implementation;

import ma.dentaluxe.mvc.controllers.modules.Acte.api.ActeController;
import ma.dentaluxe.service.actes.api.ActeService;
import ma.dentaluxe.service.actes.dto.ActeDTO;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.service.actes.exception.*;

import java.util.List;
public class ActeControllerImpl implements ActeController {

    private final ActeService acteService;

    public ActeControllerImpl(ActeService acteService) {
        if (acteService == null) {
            throw new IllegalArgumentException("Le service des actes ne peut pas être null");
        }
        this.acteService = acteService;
    }

    // ========== CRUD de base ==========

    @Override
    public ActeDTO createActe(ActeDTO acteDTO) {
        try {
            System.out.println(" [CONTROLLER] Création d'un nouvel acte...");
            //Appel au service (la vraie logique)
            ActeDTO created = acteService.createActe(acteDTO);
            System.out.println(" [CONTROLLER] Acte créé avec succès (ID: " + created.getIdActe() + ")");
            //Retour du résultat
            return created;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (ActeAlreadyExistsException e) {
            System.err.println(" [CONTROLLER] L'acte existe déjà : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la création : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création de l'acte", e);
        }
    }

    @Override
    public ActeDTO getActeById(Long id) {
        try {
            System.out.println("[CONTROLLER] Recherche de l'acte ID: " + id);
            ActeDTO acte = acteService.getActeById(id);
            System.out.println("[CONTROLLER] Acte trouvé : " + acte.getLibelle());
            return acte;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération de l'acte", e);
        }
    }

    @Override
    public List<ActeDTO> getAllActes() {
        try {
            System.out.println(" [CONTROLLER] Récupération de tous les actes...");
            List<ActeDTO> actes = acteService.getAllActes();
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) récupéré(s)");
            return actes;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la récupération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des actes", e);
        }
    }

    @Override
    public ActeDTO updateActe(ActeDTO acteDTO) {
        try {
            System.out.println(" [CONTROLLER] Mise à jour de l'acte ID: " + acteDTO.getIdActe());
            ActeDTO updated = acteService.updateActe(acteDTO);
            System.out.println(" [CONTROLLER] Acte mis à jour avec succès");
            return updated;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la mise à jour : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour de l'acte", e);
        }
    }

    @Override
    public void deleteActe(Long id) {
        try {
            System.out.println(" [CONTROLLER] Suppression de l'acte ID: " + id);
            acteService.deleteActe(id);
            System.out.println(" [CONTROLLER] Acte supprimé avec succès");
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] ID invalide : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la suppression : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la suppression de l'acte", e);
        }
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<ActeDTO> getActesByCategorie(CategorieActe categorie) {
        try {
            System.out.println(" [CONTROLLER] Recherche des actes de catégorie : " + categorie);
            List<ActeDTO> actes = acteService.getActesByCategorie(categorie);
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) trouvé(s)");
            return actes;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Catégorie invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par catégorie", e);
        }
    }

    @Override
    public List<ActeDTO> searchActesByLibelle(String keyword) {
        try {
            System.out.println(" [CONTROLLER] Recherche des actes avec le mot-clé : " + keyword);
            List<ActeDTO> actes = acteService.searchActesByLibelle(keyword);
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) trouvé(s)");
            return actes;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Mot-clé invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par libellé", e);
        }
    }

    @Override
    public ActeDTO getActeByLibelle(String libelle) {
        try {
            System.out.println(" [CONTROLLER] Recherche de l'acte : " + libelle);
            ActeDTO acte = acteService.getActeByLibelle(libelle);
            if (acte != null) {
                System.out.println(" [CONTROLLER] Acte trouvé");
            } else {
                System.out.println("️ [CONTROLLER] Aucun acte trouvé avec ce libellé");
            }
            return acte;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Libellé invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par libellé", e);
        }
    }

    @Override
    public List<ActeDTO> getActesByPriceRange(double prixMin, double prixMax) {
        try {
            System.out.println(" [CONTROLLER] Recherche des actes entre " + prixMin + " et " + prixMax + " DH");
            List<ActeDTO> actes = acteService.getActesByPriceRange(prixMin, prixMax);
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) trouvé(s)");
            return actes;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Fourchette de prix invalide : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche par prix", e);
        }
    }

    @Override
    public List<ActeDTO> getActesSortedByPrice(boolean ascending) {
        try {
            System.out.println(" [CONTROLLER] Tri des actes par prix (" +
                    (ascending ? "croissant" : "décroissant") + ")");
            List<ActeDTO> actes = acteService.getActesSortedByPrice(ascending);
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) trié(s)");
            return actes;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du tri : " + e.getMessage());
            throw new RuntimeException("Erreur lors du tri par prix", e);
        }
    }

    @Override
    public List<ActeDTO> getActesSortedByLibelle() {
        try {
            System.out.println("[CONTROLLER] Tri des actes par libellé (ordre alphabétique)");
            List<ActeDTO> actes = acteService.getActesSortedByLibelle();
            System.out.println(" [CONTROLLER] " + actes.size() + " acte(s) trié(s)");
            return actes;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du tri : " + e.getMessage());
            throw new RuntimeException("Erreur lors du tri par libellé", e);
        }
    }

    // ========== Statistiques ==========

    @Override
    public int countActes() {
        try {
            System.out.println(" [CONTROLLER] Comptage des actes...");
            int count = acteService.countActes();
            System.out.println(" [CONTROLLER] Nombre total d'actes : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage des actes", e);
        }
    }

    @Override
    public int countActesByCategorie(CategorieActe categorie) {
        try {
            System.out.println(" [CONTROLLER] Comptage des actes de catégorie : " + categorie);
            int count = acteService.countActesByCategorie(categorie);
            System.out.println(" [CONTROLLER] Nombre d'actes : " + count);
            return count;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du comptage : " + e.getMessage());
            throw new RuntimeException("Erreur lors du comptage par catégorie", e);
        }
    }

    @Override
    public double getAveragePrixActes() {
        try {
            System.out.println(" [CONTROLLER] Calcul du prix moyen...");
            double avg = acteService.getAveragePrixActes();
            System.out.println(" [CONTROLLER] Prix moyen : " + String.format("%.2f", avg) + " DH");
            return avg;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors du calcul : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul du prix moyen", e);
        }
    }

    @Override
    public ActeDTO getMostExpensiveActe() {
        try {
            System.out.println(" [CONTROLLER] Recherche de l'acte le plus cher...");
            ActeDTO acte = acteService.getMostExpensiveActe();
            if (acte != null) {
                System.out.println(" [CONTROLLER] Acte le plus cher : " + acte.getLibelle() +
                        " (" + acte.getPrixDeBase() + " DH)");
            } else {
                System.out.println("⚠ [CONTROLLER] Aucun acte trouvé");
            }
            return acte;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche de l'acte le plus cher", e);
        }
    }

    @Override
    public ActeDTO getCheapestActe() {
        try {
            System.out.println(" [CONTROLLER] Recherche de l'acte le moins cher...");
            ActeDTO acte = acteService.getCheapestActe();
            if (acte != null) {
                System.out.println(" [CONTROLLER] Acte le moins cher : " + acte.getLibelle() +
                        " (" + acte.getPrixDeBase() + " DH)");
            } else {
                System.out.println(" [CONTROLLER] Aucun acte trouvé");
            }
            return acte;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la recherche : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la recherche de l'acte le moins cher", e);
        }
    }

    @Override
    public double getTotalPrixActes() {
        try {
            System.out.println(" [CONTROLLER] Calcul du prix total...");
            double total = acteService.getTotalPrixActes();
            System.out.println(" [CONTROLLER] Prix total : " + String.format("%.2f", total) + " DH");
            return total;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du calcul : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul du prix total", e);
        }
    }

    @Override
    public List<ActeService.CategorieStatDTO> getStatistiquesByCategorie() {
        try {
            System.out.println(" [CONTROLLER] Génération des statistiques par catégorie...");
            List<ActeService.CategorieStatDTO> stats = acteService.getStatistiquesByCategorie();
            System.out.println(" [CONTROLLER] Statistiques générées pour " + stats.size() + " catégorie(s)");
            return stats;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la génération : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la génération des statistiques", e);
        }
    }

    // ========== Validation métier ==========

    @Override
    public boolean validateActe(ActeDTO acteDTO) {
        try {
            boolean valid = acteService.validateActe(acteDTO);
            if (valid) {
                System.out.println(" [CONTROLLER] Acte valide");
            } else {
                System.out.println("️ [CONTROLLER] Acte invalide");
            }
            return valid;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la validation : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean acteExists(Long id) {
        try {
            boolean exists = acteService.acteExists(id);
            if (exists) {
                System.out.println(" [CONTROLLER] L'acte existe (ID: " + id + ")");
            } else {
                System.out.println(" [CONTROLLER] L'acte n'existe pas (ID: " + id + ")");
            }
            return exists;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean acteExistsByLibelle(String libelle) {
        try {
            boolean exists = acteService.acteExistsByLibelle(libelle);
            if (exists) {
                System.out.println(" [CONTROLLER] L'acte existe : " + libelle);
            } else {
                System.out.println(" [CONTROLLER] L'acte n'existe pas : " + libelle);
            }
            return exists;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la vérification : " + e.getMessage());
            return false;
        }
    }

    // ========== Gestion des prix ==========

    @Override
    public ActeDTO updatePrix(Long idActe, double nouveauPrix) {
        try {
            System.out.println(" [CONTROLLER] Mise à jour du prix de l'acte ID: " + idActe);
            ActeDTO updated = acteService.updatePrix(idActe, nouveauPrix);
            System.out.println(" [CONTROLLER] Prix mis à jour avec succès");
            return updated;
        } catch (InvalidActeDataException e) {
            System.err.println("[CONTROLLER] Prix invalide : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de la mise à jour : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour du prix", e);
        }
    }

    @Override
    public ActeDTO applyDiscount(Long idActe, double pourcentage) {
        try {
            System.out.println(" [CONTROLLER] Application d'une remise de " + pourcentage + "% sur l'acte ID: " + idActe);
            ActeDTO updated = acteService.applyDiscount(idActe, pourcentage);
            System.out.println(" [CONTROLLER] Remise appliquée avec succès");
            return updated;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Pourcentage invalide : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("[CONTROLLER] Erreur lors de l'application de la remise : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'application de la remise", e);
        }
    }

    @Override
    public List<ActeDTO> applyDiscountToCategorie(CategorieActe categorie, double pourcentage) {
        try {
            System.out.println(" [CONTROLLER] Application d'une remise de " + pourcentage +
                    "% à la catégorie : " + categorie);
            List<ActeDTO> updated = acteService.applyDiscountToCategorie(categorie, pourcentage);
            System.out.println(" [CONTROLLER] Remise appliquée à " + updated.size() + " acte(s)");
            return updated;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors de l'application de la remise : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'application de la remise à la catégorie", e);
        }
    }

    @Override
    public double calculatePrixAvecRemise(Long idActe, double pourcentage) {
        try {
            System.out.println(" [CONTROLLER] Calcul du prix avec remise de " + pourcentage +
                    "% pour l'acte ID: " + idActe);
            double prixAvecRemise = acteService.calculatePrixAvecRemise(idActe, pourcentage);
            System.out.println(" [CONTROLLER] Prix calculé : " + String.format("%.2f", prixAvecRemise) + " DH");
            return prixAvecRemise;
        } catch (InvalidActeDataException e) {
            System.err.println(" [CONTROLLER] Données invalides : " + e.getMessage());
            throw e;
        } catch (ActeNotFoundException e) {
            System.err.println(" [CONTROLLER] Acte non trouvé : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println(" [CONTROLLER] Erreur lors du calcul : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul du prix avec remise", e);
        }
    }
}