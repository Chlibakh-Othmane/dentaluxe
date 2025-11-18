package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.userManager.api.UtilisateurRepository;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private final List<Utilisateur> data = new ArrayList<>();


    @Override
    public Utilisateur findByLogin(String login) {
        return data.stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null);
    }


    @Override
    public Utilisateur authenticate(String login, String password) {
        return data.stream()
                .filter(u -> u.getLogin().equals(login) && u.getPasswordHash().equals(password))
                .findFirst().orElse(null);
    }

    @Override
    public List<Utilisateur> findAll() {
        return List.copyOf(data);
    }

    @Override
    public Utilisateur findById(Long id) {
        return data.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void create(Utilisateur utilisateur) {
        data.add(utilisateur);
    }

    @Override
    public void update(Utilisateur utilisateur) {
        deleteById(utilisateur.getId());
        data.add(utilisateur);
    }

    @Override
    public void delete(Utilisateur utilisateur) {
        data.removeIf(u -> u.getId().equals(utilisateur.getId()));
    }

    @Override
    public void deleteById(Long id) {
        data.removeIf(u -> u.getId().equals(id));
    }
}
