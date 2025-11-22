package ma.dentaluxe.mvc.controllers.modules.patient.swing_implementation;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.mvc.controllers.modules.patient.api.PatientController;
import ma.dentaluxe.mvc.dto.PatientDTO;
import ma.dentaluxe.mvc.ui.modules.patient.PatientView;
import ma.dentaluxe.service.modules.api.PatientService;

@Data @AllArgsConstructor @NoArgsConstructor
public class PatientControllerImpl implements PatientController {

    private PatientService service;

    @Override
    public void showRecentPatients() {
        List<PatientDTO> dtos = service.getTodayPatientsAsDTO();
        PatientView.showAsync(dtos);
    }
}
