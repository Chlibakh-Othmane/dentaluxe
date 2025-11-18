package ma.dentaluxe.repository.modules.medicament.inMemDB_implementation;

import ma.dentaluxe.entities.ordonnance.Medicament;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MedicamentRepositoryImpl implements MedicamentRepository {

    private final List<Medicament> data = new ArrayList<>();

    public MedicamentRepositoryImpl() {
        // Données d'exemple pour les médicaments
        data.add(Medicament.builder()
                .idMedicament(101L)
                .nom("Paracétamol")
                .type("Antalgique")
                .forme("Comprimé")
                .remboursable(true)
                .prixUnitaire(2.5)
                .description("Antalgique et antipyrétique")
                .build());

        data.add(Medicament.builder()
                .idMedicament(102L)
                .nom("Ibuprofène")
                .type("Anti-inflammatoire")
                .forme("Comprimé")
                .remboursable(true)
                .prixUnitaire(3.2)
                .description("Anti-inflammatoire non stéroïdien")
                .build());

        data.add(Medicament.builder()
                .idMedicament(103L)
                .nom("Amoxicilline")
                .type("Antibiotique")
                .forme("Gélule")
                .remboursable(true)
                .prixUnitaire(8.7)
                .description("Antibiotique à large spectre")
                .build());

        data.add(Medicament.builder()
                .idMedicament(104L)
                .nom("Vitamine C")
                .type("Vitamine")
                .forme("Comprimé")
                .remboursable(false)
                .prixUnitaire(12.0)
                .description("Complément alimentaire")
                .build());

        data.add(Medicament.builder()
                .idMedicament(105L)
                .nom("Smecta")
                .type("Antidiarrhéique")
                .forme("Sachet")
                .remboursable(true)
                .prixUnitaire(4.5)
                .description("Traitement des diarrhées")
                .build());

        // Tri par nom de médicament
        data.sort(Comparator.comparing(Medicament::getNom));
    }

    @Override
    public List<Medicament> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Medicament findById(Long id) {
        return data.stream()
                .filter(m -> m.getIdMedicament().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Medicament medicament) {
        if (medicament.getIdMedicament() == null) {
            Long newId = data.stream()
                    .mapToLong(Medicament::getIdMedicament)
                    .max()
                    .orElse(0L) + 1L;
            medicament.setIdMedicament(newId);
        }
        data.add(medicament);
    }

    @Override
    public void update(Medicament medicament) {
        deleteById(medicament.getIdMedicament());
        data.add(medicament);
    }

    @Override
    public void delete(Medicament medicament) {
        data.removeIf(m -> m.getIdMedicament().equals(medicament.getIdMedicament()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(m -> m.getIdMedicament().equals(id));
    }

    @Override
    public List<Medicament> findByNom(String nom) {
        return data.stream()
                .filter(m -> m.getNom().toLowerCase().contains(nom.toLowerCase()))
                .toList();
    }

    @Override
    public List<Medicament> findByType(String type) {
        return data.stream()
                .filter(m -> m.getType().equalsIgnoreCase(type))
                .toList();
    }

    @Override
    public List<Medicament> findByRemboursable(Boolean remboursable) {
        return data.stream()
                .filter(m -> m.getRemboursable().equals(remboursable))
                .toList();
    }

    @Override
    public List<Medicament> findByForme(String forme) {
        return data.stream()
                .filter(m -> m.getForme().equalsIgnoreCase(forme))
                .toList();
    }
}