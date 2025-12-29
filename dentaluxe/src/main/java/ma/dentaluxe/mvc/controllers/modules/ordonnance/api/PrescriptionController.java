package ma.dentaluxe.mvc.controllers.modules.ordonnance.api;

import ma.dentaluxe.mvc.dto.ordonnance.PrescriptionDTO;
import ma.dentaluxe.mvc.dto.ordonnance.PrescriptionCreateDTO;
import java.util.List;

public interface PrescriptionController {
    // Ajouter une ligne de médicament à une ordonnance existante
    void addPrescriptionToOrdo(PrescriptionCreateDTO dto);

    // Modifier la posologie ou la durée d'une ligne
    void updatePrescriptionDetails(Long id, Integer qte, String freq, Integer duree);

    // Supprimer une ligne précise
    void removePrescription(Long idPrescription);

    // Récupérer les lignes d'une ordonnance
    List<PrescriptionDTO> getLinesByOrdonnance(Long idOrdo);
}