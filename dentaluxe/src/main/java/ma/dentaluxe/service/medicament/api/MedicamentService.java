package ma.dentaluxe.service.medicament.api;

import ma.dentaluxe.mvc.dto.medicament.MedicamentDTO;
import ma.dentaluxe.mvc.dto.medicament.MedicamentStatisticsDTO;
import java.util.List;

public interface MedicamentService {
    // CRUD utilisant les DTO
    List<MedicamentDTO> findAll();
    MedicamentDTO findById(Long id);
    MedicamentDTO create(MedicamentDTO dto);
    MedicamentDTO update(Long id, MedicamentDTO dto);
    void delete(Long id);

    // Recherche et filtres
    List<MedicamentDTO> findByNom(String nom);

    // Métier et Stats
    MedicamentStatisticsDTO getStatistics();
    MedicamentDTO applyDiscount(Long id, Double pourcentage);
}