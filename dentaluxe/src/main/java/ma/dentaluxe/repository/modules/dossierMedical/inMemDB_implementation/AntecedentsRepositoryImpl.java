package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.repository.modules.dossierMedical.api.AntecedentsRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AntecedentsRepositoryImpl implements AntecedentsRepository {
    private final List<Antecedents> data = new ArrayList<>();

    @Override
    public List<Antecedents> findAll() { return List.copyOf(data); }

    @Override
    public Antecedents findById(Long id) {
        return data.stream().filter(a -> a.getIdAntecedent().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Antecedents> findByDossierMedicalId(Long idDM) {
        return data.stream().filter(a -> a.getIdDM().equals(idDM)).collect(Collectors.toList());
    }

    @Override
    public void create(Antecedents entity) { data.add(entity); }

    @Override
    public void update(Antecedents entity) {
        deleteById(entity.getIdAntecedent());
        data.add(entity);
    }

    @Override
    public void delete(Antecedents entity) { data.removeIf(a -> a.getIdAntecedent().equals(entity.getIdAntecedent())); }

    @Override
    public void deleteById(Long id) { data.removeIf(a -> a.getIdAntecedent().equals(id)); }
}
