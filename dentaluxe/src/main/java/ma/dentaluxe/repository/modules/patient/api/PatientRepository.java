    package ma.dentaluxe.repository.modules.patient.api;

    import ma.dentaluxe.entities.patient.Patient;
    import ma.dentaluxe.repository.common.CrudRepository;
    import java.util.List;

    public interface PatientRepository extends CrudRepository<Patient, Long> {

        // Méthode spécifique pour rechercher par nom
        List<Patient> findByNom(String nom);
    }
