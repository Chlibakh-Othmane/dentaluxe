package ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation;

import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepository {

    private final List<Ordonnance> data = new ArrayList<>();

    public OrdonnanceRepositoryImpl() {
        // Données d'exemple : 4 ordonnances
        LocalDate aujourdhui = LocalDate.now();

        data.add(Ordonnance.builder()
                .idOrdo(1L).idDM(101L).idMedecin(201L)
                .dateOrdonnance(aujourdhui.minusDays(2))
                .build());

        data.add(Ordonnance.builder()
                .idOrdo(2L).idDM(102L).idMedecin(202L)
                .dateOrdonnance(aujourdhui.minusDays(1))
                .build());

        data.add(Ordonnance.builder()
                .idOrdo(3L).idDM(101L).idMedecin(201L)
                .dateOrdonnance(aujourdhui)
                .build());

        data.add(Ordonnance.builder()
                .idOrdo(4L).idDM(103L).idMedecin(203L)
                .dateOrdonnance(aujourdhui.minusDays(5))
                .build());

        // Tri par date d'ordonnance (descendant)
        data.sort(Comparator.comparing(Ordonnance::getDateOrdonnance).reversed());
    }

    @Override
    public List<Ordonnance> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Ordonnance findById(Long id) {
        return data.stream()
                .filter(o -> o.getIdOrdo().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void create(Ordonnance ordonnance) {
        // Générer un nouvel ID si non fourni
        if (ordonnance.getIdOrdo() == null) {
            Long newId = data.stream()
                    .mapToLong(Ordonnance::getIdOrdo)
                    .max()
                    .orElse(0L) + 1L;
            ordonnance.setIdOrdo(newId);
        }
        data.add(ordonnance);
    }

    @Override
    public void update(Ordonnance ordonnance) {
        deleteById(ordonnance.getIdOrdo());
        data.add(ordonnance);
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        data.removeIf(o -> o.getIdOrdo().equals(ordonnance.getIdOrdo()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(o -> o.getIdOrdo().equals(id));
    }

    @Override
    public List<Ordonnance> findByDossierMedical(Long idDM) {
        return data.stream()
                .filter(o -> o.getIdDM().equals(idDM))
                .toList();
    }

    @Override
    public List<Ordonnance> findByMedecin(Long idMedecin) {
        return data.stream()
                .filter(o -> o.getIdMedecin().equals(idMedecin))
                .toList();
    }
}