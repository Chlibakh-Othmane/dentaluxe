package ma.dentaluxe.mvc.controllers.modules.medicament.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.medicament.api.MedicamentController;
import ma.dentaluxe.service.medicament.api.MedicamentService;
import ma.dentaluxe.mvc.dto.medicament.MedicamentDTO;
import ma.dentaluxe.mvc.dto.medicament.MedicamentStatisticsDTO;
import javax.swing.JOptionPane;
import java.util.List;

public class MedicamentSwingController implements MedicamentController {

    private final MedicamentService service;

    public MedicamentSwingController() {
        // Injection via le singleton de l'ApplicationContext
        this.service = (MedicamentService) ApplicationContext.getBean("medicamentService");
    }

    @Override
    public void saveMedicament(MedicamentDTO dto) {
        try {
            if (dto.getIdMedicament() == null) {
                service.create(dto);
                JOptionPane.showMessageDialog(null, "Médicament ajouté au stock.");
            } else {
                service.update(dto.getIdMedicament(), dto);
                JOptionPane.showMessageDialog(null, "Fiche médicament mise à jour.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'enregistrement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void deleteMedicament(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, "Supprimer ce médicament du stock ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.delete(id);
                JOptionPane.showMessageDialog(null, "Médicament supprimé.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Impossible de supprimer : " + e.getMessage());
            }
        }
    }

    @Override
    public void applyPromotion(Long id, Double pourcentage) {
        try {
            service.applyDiscount(id, pourcentage);
            JOptionPane.showMessageDialog(null, "La remise de " + pourcentage + "% a été appliquée !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur remise : " + e.getMessage());
        }
    }

    @Override
    public void showMedicamentStats() {
        MedicamentStatisticsDTO stats = service.getStatistics();
        String message = String.format("--- STATISTIQUES MÉDICAMENTS ---\n" +
                        "- Total en stock : %d\n" +
                        "- Remboursables : %d\n" +
                        "- Prix moyen : %.2f DH",
                stats.getTotalMedicaments(), stats.getRemboursablesCount(), stats.getPrixMoyen());

        JOptionPane.showMessageDialog(null, message, "Tableau de Bord", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public List<MedicamentDTO> getAllMedicaments() { return service.findAll(); }
    @Override public List<MedicamentDTO> searchByName(String nom) { return service.findByNom(nom); }
}