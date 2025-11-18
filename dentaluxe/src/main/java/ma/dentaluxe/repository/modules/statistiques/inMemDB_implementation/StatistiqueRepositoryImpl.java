package ma.dentaluxe.repository.modules.statistiques.inMemDB_implementation;

import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.statistique.Statistiques;
import ma.dentaluxe.entities.enums.CategorieStatistique;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.repository.modules.statistiques.api.StatistiqueRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatistiqueRepositoryImpl implements StatistiqueRepository {

    private final List<Statistiques> data = new ArrayList<>();


    @Override
    public List<Statistiques> findByCategorie(CategorieStatistique categorie) {
        return data.stream()
                .filter(s -> s.getCategorie() == categorie)
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findByPeriod(LocalDate debut, LocalDate fin) {
        return data.stream()
                .filter(s -> !s.getDateCalcul().isBefore(debut) && !s.getDateCalcul().isAfter(fin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findByCabinetId(Long idCabinet) {
        return data.stream()
                .filter(s -> s.getIdCabinet().equals(idCabinet))
                .collect(Collectors.toList());
    }

    @Override
    public List<Statistiques> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Statistiques findById(Long id) {
        return data.stream().filter(s -> s.getIdStat().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void create(Statistiques statistiques) {
        data.add(statistiques);
    }

    @Override
    public void update(Statistiques statistiques) {
        deleteById(statistiques.getIdStat());
        data.add(statistiques);
    }

    @Override
    public void delete(Statistiques statistiques) {
        data.removeIf(s -> s.getIdStat().equals(statistiques.getIdStat()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(s -> s.getIdStat().equals(id));
    }
}
