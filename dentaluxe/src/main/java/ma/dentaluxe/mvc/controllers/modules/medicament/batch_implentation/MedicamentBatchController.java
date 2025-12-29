package ma.dentaluxe.mvc.controllers.modules.medicament.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.medicament.api.MedicamentController;
import ma.dentaluxe.service.medicament.api.MedicamentService;
import ma.dentaluxe.mvc.dto.medicament.MedicamentDTO;
import java.util.List;

public class MedicamentBatchController implements MedicamentController {

    private final MedicamentService service = (MedicamentService) ApplicationContext.getBean("medicamentService");

    @Override
    public void saveMedicament(MedicamentDTO dto) {
        System.out.println("BATCH : Traitement médicament " + dto.getNom());
        if (dto.getIdMedicament() == null) service.create(dto);
        else service.update(dto.getIdMedicament(), dto);
    }

    @Override public void deleteMedicament(Long id) { service.delete(id); }
    @Override public List<MedicamentDTO> getAllMedicaments() { return service.findAll(); }
    @Override public List<MedicamentDTO> searchByName(String nom) { return service.findByNom(nom); }
    @Override public void applyPromotion(Long id, Double p) { service.applyDiscount(id, p); }
    @Override public void showMedicamentStats() { System.out.println("Stats batch : " + service.getStatistics().getTotalMedicaments()); }
}