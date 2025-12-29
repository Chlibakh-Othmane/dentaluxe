package ma.dentaluxe.mvc.controllers.modules.medicament.api;

import ma.dentaluxe.mvc.dto.medicament.MedicamentDTO;
import java.util.List;

public interface MedicamentController {
    // Gestion standard
    void saveMedicament(MedicamentDTO dto); // Gère création et modification
    void deleteMedicament(Long id);
    List<MedicamentDTO> getAllMedicaments();
    List<MedicamentDTO> searchByName(String nom);

    // Actions métier spécifiques
    void applyPromotion(Long id, Double pourcentage);
    void showMedicamentStats(); // Affiche une popup avec les statistiques
}