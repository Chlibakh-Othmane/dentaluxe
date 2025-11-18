package ma.dentaluxe.repository.modules.statistiques.api;

import ma.dentaluxe.entities.enums.CategorieStatistique;
import ma.dentaluxe.entities.statistique.Statistiques;
import ma.dentaluxe.repository.common.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatistiqueRepository extends CrudRepository<Statistiques, Long> {
    List<Statistiques> findByIdCabinet(Long idCabinet);
    List<Statistiques> findByCategorie(CategorieStatistique categorie);
    List<Statistiques> findByDateCalculBetween(LocalDate startDate, LocalDate endDate);
    List<Statistiques> findByNomContaining(String nom);
    // Exemple de méthodes pour des statistiques agrégées (si besoin)
    Double sumChiffreByCategorieAndDateRange(CategorieStatistique categorie, LocalDate startDate, LocalDate endDate);
    Long countByCategorieAndDateRange(CategorieStatistique categorie, LocalDate startDate, LocalDate endDate);
}