package ma.dentaluxe.mvc.controllers.modules.ordonnance.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.ordonnance.api.PrescriptionController;
import ma.dentaluxe.service.ordonnance.api.PrescriptionService;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import javax.swing.JOptionPane;
import java.util.List;

public class PrescriptionSwingController implements PrescriptionController {

    private final PrescriptionService service;

    public PrescriptionSwingController() {
        this.service = (PrescriptionService) ApplicationContext.getBean("prescriptionService");
    }

    @Override
    public void addPrescriptionToOrdo(PrescriptionCreateDTO dto) {
        try {
            service.createPrescription(dto);
            JOptionPane.showMessageDialog(null, "Médicament ajouté à l'ordonnance.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void updatePrescriptionDetails(Long id, Integer qte, String freq, Integer duree) {
        try {
            // 1. On emballe les 4 arguments dans le DTO que le service attend
            PrescriptionUpdateDTO updateDTO = PrescriptionUpdateDTO.builder()
                    .idPrescription(id)
                    .quantite(qte)
                    .frequence(freq)
                    .dureeEnJours(duree)
                    .build();

            // 2. On appelle le service avec le DTO (1 seul argument au lieu de 4)
            service.updatePrescription(updateDTO);

            JOptionPane.showMessageDialog(null, "Posologie mise à jour avec succès.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void removePrescription(Long idPrescription) {
        int confirm = JOptionPane.showConfirmDialog(null, "Retirer ce médicament ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deletePrescription(idPrescription);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    @Override
    public List<PrescriptionDTO> getLinesByOrdonnance(Long idOrdo) {
        return service.getPrescriptionsByOrdonnance(idOrdo);
    }
}