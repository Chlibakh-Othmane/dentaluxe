package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.entities.utilisateur.Secretaire;
import ma.dentaluxe.repository.modules.userManager.api.SecretaireRepository;
import java.util.ArrayList;
import java.util.List;

public class SecretaireRepositoryImpl implements SecretaireRepository {
    private final List<Secretaire> data = new ArrayList<>();

    @Override
    public List<Secretaire> findAll() { return List.copyOf(data); }

    @Override
    public Secretaire findById(Long id) {
        return data.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Secretaire findByCNSS(String numCnss) {
        return data.stream().filter(s -> s.getNumCnss().equals(numCnss)).findFirst().orElse(null);
    }

    @Override
    public void create(Secretaire entity) { data.add(entity); }

    @Override
    public void update(Secretaire entity) {
        deleteById(entity.getId());
        data.add(entity);
    }

    @Override
    public void delete(Secretaire entity) { data.removeIf(s -> s.getId().equals(entity.getId())); }

    @Override
    public void deleteById(Long id) { data.removeIf(s -> s.getId().equals(id)); }

}
