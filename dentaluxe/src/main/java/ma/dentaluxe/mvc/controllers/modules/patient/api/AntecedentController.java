package ma.dentaluxe.mvc.controllers.modules.patient.api;

import ma.dentaluxe.mvc.dto.antecedent.AntecedentDTO;
import java.util.List;

public interface AntecedentController {
    void createAntecedent(AntecedentDTO dto);
    List<AntecedentDTO> getAllAntecedents();
    void deleteAntecedent(Long id);
}