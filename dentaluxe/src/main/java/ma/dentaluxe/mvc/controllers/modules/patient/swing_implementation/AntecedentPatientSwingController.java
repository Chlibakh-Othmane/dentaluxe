package ma.dentaluxe.mvc.controllers.modules.patient.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.AntecedentPatientController;
import ma.dentaluxe.service.patient.api.AntecedentPatientService;
import ma.dentaluxe.mvc.dto.patient.*;
import javax.swing.JOptionPane;
import java.util.List;

public class AntecedentPatientSwingController implements AntecedentPatientController {
    private final AntecedentPatientService service = (AntecedentPatientService) ApplicationContext.getBean("antecedentPatientService");

    @Override
    public void linkToPatient(AntecedentPatientCreateDTO dto) {
        try {
            service.addAntecedentToPatient(dto);
            JOptionPane.showMessageDialog(null, "Antécédent ajouté au dossier du patient.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur dossier : " + e.getMessage(), "Attention", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public List<AntecedentPatientDTO> getPatientMedicalHistory(Long patientId) {
        return service.getPatientMedicalHistory(patientId);
    }

    @Override
    public void updateNote(AntecedentPatientDTO dto) {
        try {
            service.updatePatientAntecedent(dto);
            JOptionPane.showMessageDialog(null, "Note mise à jour.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}