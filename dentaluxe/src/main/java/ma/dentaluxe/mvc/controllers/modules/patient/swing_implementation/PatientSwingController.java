package ma.dentaluxe.mvc.controllers.modules.patient.swing_implementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.PatientController;
import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.mvc.dto.patient.*;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PatientSwingController implements PatientController {

    private final PatientService patientService;

    public PatientSwingController() {
        // Utilisation de getBean avec cast vers l'interface
        this.patientService = (PatientService) ApplicationContext.getBean("patientService");
    }

    @Override
    public void showRecentPatients() {
        List<PatientDTO> allPatients = patientService.getAllPatients();

        List<PatientDTO> todayPatients = allPatients.stream()
                .filter(p -> p.getDateCreation() != null &&
                        p.getDateCreation().toLocalDate().isEqual(LocalDate.now()))
                .sorted(Comparator.comparing(PatientDTO::getDateCreation))
                .collect(Collectors.toList());

        if (todayPatients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun patient ajouté aujourd'hui.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Patients du jour :\n");
        todayPatients.forEach(p -> sb.append("- ").append(p.getNomComplet()).append("\n"));
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    @Override
    public void savePatient(PatientCreateDTO dto) {

    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return List.of();
    }

    @Override
    public void removePatient(Long id) {

    }

    // Ajoute ici tes autres méthodes (savePatient, etc.)
}