package ma.dentaluxe.repository.modules.AntecedentPatient.api;

import ma.dentaluxe.entities.AntecedentPatient.AntecedentPatient;
import ma.dentaluxe.repository.common.CrudRepository;

import java.util.List;

public interface AntecedentPatientRepository extends CrudRepository<AntecedentPatient, Long> {

    List<AntecedentPatient> findByPatientId(Long idPatient);
    int countByPatientId(Long idPatient);
    void deleteByPatientId(Long idPatient);
}
