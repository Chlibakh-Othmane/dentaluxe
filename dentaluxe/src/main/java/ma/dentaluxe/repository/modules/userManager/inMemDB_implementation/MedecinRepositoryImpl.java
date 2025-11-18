package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.entities.utilisateur.Medecin;
import ma.dentaluxe.repository.modules.userManager.api.MedecinRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedecinRepositoryImpl implements MedecinRepository {
    private final List<Medecin> data = new ArrayList<>();

    @Override
    public List<Medecin> findAll() { return List.copyOf(data); }

    @Override
    public Medecin findById(Long id) {
        return data.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        return data.stream()
                .filter(m -> m.getSpecialite().equalsIgnoreCase(specialite))
                .collect(Collectors.toList());
    }

    @Override
    public void create(Medecin entity) { data.add(entity); }

    @Override
    public void update(Medecin entity) {
        deleteById(entity.getId());
        data.add(entity);
    }

    @Override
    public void delete(Medecin entity) { data.removeIf(m -> m.getId().equals(entity.getId())); }

    @Override
    public void deleteById(Long id) { data.removeIf(m -> m.getId().equals(id)); }
}
