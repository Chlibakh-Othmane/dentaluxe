package ma.dentaluxe.repository.modules.statistiques.api;

import ma.dentaluxe.entities.statistique.Statistiques;
import ma.dentaluxe.entities.enums.CategorieStatistique;
import ma.dentaluxe.repository.common.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface StatistiqueRepository extends CrudRepository<Statistiques, Long>{
    List<Statistiques> findByCategorie(CategorieStatistique categorie);
    List<Statistiques> findByPeriod(LocalDate debut, LocalDate fin);
    List<Statistiques> findByCabinetId(Long idCabinet);
}
