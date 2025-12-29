package ma.dentaluxe.mvc.controllers.modules.ordonnance.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.ordonnance.api.OrdonnanceController;
import ma.dentaluxe.service.ordonnance.api.OrdonnanceService;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import javax.swing.JOptionPane;
import java.util.List;

public class OrdonnanceSwingController implements OrdonnanceController {

    private final OrdonnanceService service = (OrdonnanceService) ApplicationContext.getBean("ordonnanceService");

    @Override
    public void saveOrdonnance(OrdonnanceCreateDTO ordoDto, List<PrescriptionCreateDTO> prescriptions) {
        try {
            service.createOrdonnance(ordoDto, prescriptions);
            JOptionPane.showMessageDialog(null, "L'ordonnance a été générée et enregistrée avec succès !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de la création : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<OrdonnanceDTO> getHistoryByDossier(Long idDM) {
        return service.getOrdonnancesByDossierMedical(idDM);
    }

    @Override
    public void deleteOrdonnance(Long idOrdo) {
        int confirm = JOptionPane.showConfirmDialog(null, "Voulez-vous vraiment supprimer cette ordonnance ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.deleteOrdonnance(idOrdo);
                JOptionPane.showMessageDialog(null, "Ordonnance supprimée.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible de supprimer : " + e.getMessage());
            }
        }
    }

    @Override
    public void printOrdonnance(Long idOrdo) {
        // Logique de simulation d'impression
        JOptionPane.showMessageDialog(null, "Lancement de l'impression pour l'ordonnance #" + idOrdo);
    }
}