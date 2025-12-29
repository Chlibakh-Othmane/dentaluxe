package ma.dentaluxe.mvc.controllers.modules.ordonnance.api;

import ma.dentaluxe.mvc.dto.ordonnance.*;
import java.util.List;

public interface OrdonnanceController {
    // Création d'une ordonnance complète avec ses lignes de médicaments
    void saveOrdonnance(OrdonnanceCreateDTO ordoDto, List<PrescriptionCreateDTO> prescriptions);

    // Récupération de l'historique pour un patient (via son ID Dossier)
    List<OrdonnanceDTO> getHistoryByDossier(Long idDM);

    // Suppression
    void deleteOrdonnance(Long idOrdo);

    // Action d'impression (très utile pour une ordonnance)
    void printOrdonnance(Long idOrdo);
}