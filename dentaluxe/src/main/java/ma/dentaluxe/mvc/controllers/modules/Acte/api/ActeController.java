package ma.dentaluxe.mvc.controllers.modules.Acte.api;
import ma.dentaluxe.service.actes.dto.ActeDTO;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.service.actes.api.ActeService;

import java.util.List;
public interface ActeController {

    // ========== CRUD de base ==========

    ActeDTO createActe(ActeDTO acteDTO);

    ActeDTO getActeById(Long id);

    List<ActeDTO> getAllActes();

    ActeDTO updateActe(ActeDTO acteDTO);

    void deleteActe(Long id);

    // ========== Recherche et filtres ==========
    List<ActeDTO> getActesByCategorie(CategorieActe categorie);

    List<ActeDTO> searchActesByLibelle(String keyword);

    ActeDTO getActeByLibelle(String libelle);

    List<ActeDTO> getActesByPriceRange(double prixMin, double prixMax);

    List<ActeDTO> getActesSortedByPrice(boolean ascending);

    List<ActeDTO> getActesSortedByLibelle();

    // ========== Statistiques ==========

    int countActes();

    int countActesByCategorie(CategorieActe categorie);

    double getAveragePrixActes();

    ActeDTO getMostExpensiveActe();

    ActeDTO getCheapestActe();

    double getTotalPrixActes();

    List<ActeService.CategorieStatDTO> getStatistiquesByCategorie();

    // ========== Validation métier ==========

    boolean validateActe(ActeDTO acteDTO);

    boolean acteExists(Long id);

    boolean acteExistsByLibelle(String libelle);

    // ========== Gestion des prix ==========

    ActeDTO updatePrix(Long idActe, double nouveauPrix);

    ActeDTO applyDiscount(Long idActe, double pourcentage);

    List<ActeDTO> applyDiscountToCategorie(CategorieActe categorie, double pourcentage);

    double calculatePrixAvecRemise(Long idActe, double pourcentage);
}

