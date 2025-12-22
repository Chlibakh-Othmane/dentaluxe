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

public class PatientControllerImpl implements PatientController {

    private final PatientService patientService;

    public PatientControllerImpl() {
        this.patientService = (PatientService) ApplicationContext.getBean("patientService");
    }

    @Override
    public void showRecentPatients() {
        // 1. Récupérer tous les patients
        List<PatientDTO> allPatients = patientService.getAllPatients();

        // 2. Filtrer (ceux créés aujourd'hui) et trier par date/heure
        List<PatientDTO> todayPatients = allPatients.stream()
                .filter(p -> p.getDateCreation() != null &&
                        p.getDateCreation().toLocalDate().isEqual(LocalDate.now()))
                .sorted(Comparator.comparing(PatientDTO::getDateCreation))
                .collect(Collectors.toList());

        // 3. Préparer l'affichage textuel
        if (todayPatients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun patient ajouté aujourd'hui.", "Patients Récents", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("--- PATIENTS DU JOUR ---\n\n");
        for (PatientDTO p : todayPatients) {
            sb.append(String.format("- %s | %d ans | Ajouté à : %s\n",
                    p.getNomComplet(),
                    p.getAge(),
                    p.getDateCreationFormatee()));
        }

        // 4. Afficher dans une popup Swing
        JOptionPane.showMessageDialog(null, sb.toString(), "Récapitulatif Quotidien", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void savePatient(PatientCreateDTO dto) {
        try {
            patientService.createPatient(dto);
            JOptionPane.showMessageDialog(null, "Patient enregistré !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override public List<PatientDTO> getAllPatients() { return patientService.getAllPatients(); }
    @Override public void removePatient(Long id) { patientService.deletePatient(id); }
}