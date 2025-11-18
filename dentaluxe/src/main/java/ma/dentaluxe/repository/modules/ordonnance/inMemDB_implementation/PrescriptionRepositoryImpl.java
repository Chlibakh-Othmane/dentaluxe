package ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation;

import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    private final List<Prescription> data = new ArrayList<>();

    public PrescriptionRepositoryImpl() {
        // Données d'exemple cohérentes avec vos entités
        data.add(Prescription.builder()
                .idPrescription(1L)
                .idOrdo(1L)
                .idMedicament(101L)
                .quantite(30)
                .frequence("3 fois par jour")
                .dureeEnJours(10)
                .build());

        data.add(Prescription.builder()
                .idPrescription(2L)
                .idOrdo(1L)
                .idMedicament(102L)
                .quantite(20)
                .frequence("2 fois par jour")
                .dureeEnJours(5)
                .build());

        data.add(Prescription.builder()
                .idPrescription(3L)
                .idOrdo(2L)
                .idMedicament(103L)
                .quantite(15)
                .frequence("1 fois par jour")
                .dureeEnJours(7)
                .build());

        data.add(Prescription.builder()
                .idPrescription(4L)
                .idOrdo(2L)
                .idMedicament(104L)
                .quantite(10)
                .frequence("1 comprimé si douleur")
                .dureeEnJours(3)
                .build());

        data.add(Prescription.builder()
                .idPrescription(5L)
                .idOrdo(3L)
                .idMedicament(101L)
                .quantite(25)
                .frequence("2 comprimés 3 fois par jour")
                .dureeEnJours(8)
                .build());

        data.add(Prescription.builder()
                .idPrescription(6L)
                .idOrdo(3L)
                .idMedicament(105L)
                .quantite(1)
                .frequence("1 sachet par jour")
                .dureeEnJours(5)
                .build());

        // Tri par ID de prescription
        data.sort(Comparator.comparing(Prescription::getIdPrescription));
    }

    @Override
    public List<Prescription> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Prescription findById(Long id) {
        return data.stream()
                .filter(p -> p.getIdPrescription().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Prescription prescription) {
        // Générer un nouvel ID si non fourni
        if (prescription.getIdPrescription() == null) {
            Long newId = data.stream()
                    .mapToLong(Prescription::getIdPrescription)
                    .max()
                    .orElse(0L) + 1L;
            prescription.setIdPrescription(newId);
        }
        data.add(prescription);
    }

    @Override
    public void update(Prescription prescription) {
        deleteById(prescription.getIdPrescription());
        data.add(prescription);
    }

    @Override
    public void delete(Prescription prescription) {
        data.removeIf(p -> p.getIdPrescription().equals(prescription.getIdPrescription()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(p -> p.getIdPrescription().equals(id));
    }

    @Override
    public List<Prescription> findByOrdonnance(Long idOrdo) {
        return data.stream()
                .filter(p -> p.getIdOrdo().equals(idOrdo))
                .toList();
    }

    @Override
    public void deleteByOrdonnance(Long idOrdo) {
        data.removeIf(p -> p.getIdOrdo().equals(idOrdo));
    }
}