package ma.dentaluxe.mvc.controllers.modules.patient.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.AntecedentController;
import ma.dentaluxe.service.patient.api.AntecedentService;
import ma.dentaluxe.mvc.dto.antecedent.AntecedentDTO;
import javax.swing.JOptionPane;
import java.util.List;

public class AntecedentSwingController implements AntecedentController {
    private final AntecedentService service = (AntecedentService) ApplicationContext.getBean("antecedentService");

    @Override
    public void createAntecedent(AntecedentDTO dto) {
        try {
            service.create(dto);
            JOptionPane.showMessageDialog(null, "Antécédent ajouté au catalogue !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur catalogue : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public List<AntecedentDTO> getAllAntecedents() {
        return service.findAll();
    }

    @Override
    public void deleteAntecedent(Long id) {
        try {
            service.delete(id);
            JOptionPane.showMessageDialog(null, "Supprimé du catalogue.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}