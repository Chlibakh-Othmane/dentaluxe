package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.repository.modules.dossierMedical.api.DossierMedicalRepository;
import java.util.ArrayList;
import java.util.List;

public class DossierMedicalRepositoryImpl implements DossierMedicalRepository {

    private final List<DossierMedical> data = new ArrayList<>();

    @Override
    public DossierMedical findByPatientId(Long idPatient) {
       return data.stream().filter(d -> d.getIdPatient().equals(idPatient)).findFirst().orElse(null);
    }

    @Override
    public List<DossierMedical> findAll() {
        return List.copyOf(data);
    }

    @Override
    public DossierMedical findById(Long aLong) {
      return  data.stream().filter(d -> d.getIdDM().equals(id)).findFirst().orElse(null);

    }

    @Override
    public void create(DossierMedical patient) {
        data.add(entity);
    }

    @Override
    public void update(DossierMedical patient) {
        deleteById(entity.getIdDM());
        data.add(entity);
    }

    @Override
    public void delete(DossierMedical patient) {
        data.removeIf(d -> d.getIdDM().equals(entity.getIdDM()));
    }

    @Override
    public void deleteById(Long aLong) {
        data.removeIf(d -> d.getIdDM().equals(id));
    }
}
