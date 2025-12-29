package ma.dentaluxe.mvc.controllers.modules.patient.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.AntecedentController;
import ma.dentaluxe.service.patient.api.AntecedentService;
import ma.dentaluxe.mvc.dto.antecedent.AntecedentDTO;
import java.util.List;

public class AntecedentBatchController implements AntecedentController {
    private final AntecedentService service = (AntecedentService) ApplicationContext.getBean("antecedentService");

    @Override
    public void createAntecedent(AntecedentDTO dto) {
        System.out.println("Batch : Ajout catalogue -> " + dto.getNom());
        service.create(dto);
    }

    @Override public List<AntecedentDTO> getAllAntecedents() { return service.findAll(); }
    @Override public void deleteAntecedent(Long id) { service.delete(id); }
}