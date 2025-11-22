// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.modules.actes.baseImplementation;

import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.service.modules.actes.api.ActeService;

import java.util.List;

public class ActeServiceImpl implements ActeService {

    private final ActeRepository acteRepository; //pour communiquer avec la base de donnes

    // Constructeur : on initialise le repository ActeRepository ici
    public ActeServiceImpl() {
        this.acteRepository = new ActeRepositoryImpl();
    }

    @Override
    public List<Acte> getAllActes() {
        return acteRepository.findAll();
    }

    @Override
    public Acte getActeById(Long id) {
        if (id == null) {
            System.err.println("Erreur : L'ID ne peut pas être null.");
            return null;
        }
        return acteRepository.findById(id);
    }

    @Override
    public void addActe(Acte acte) {
        // Validation métier avant d'appeler le repository
        if (validerActe(acte)) {
            acteRepository.create(acte);
            System.out.println("Succès : Acte ajouté avec succès.");
        }
    }

    @Override
    public void updateActe(Acte acte) {
        // Validation avant modification
        if (acte.getIdActe() == null) {
            System.err.println("Erreur : Impossible de modifier un acte sans ID.");
            return;
        }

        if (validerActe(acte)) {
            acteRepository.update(acte);
            System.out.println("Succès : Acte modifié avec succès.");
        }
    }

    @Override
    public void deleteActeById(Long id) {
        if (id == null) {
            System.err.println("Erreur : ID invalide pour la suppression.");
            return;
        }
        // On pourrait vérifier ici si l'acte est déjà utilisé dans une facture avant de le supprimer
        acteRepository.deleteById(id);
        System.out.println("Succès : Acte supprimé.");
    }

    @Override
    public List<Acte> getActesByCategorie(CategorieActe categorie) {
        if (categorie == null) {
            return getAllActes(); // Si pas de catégorie, on retourne tout
        }
        return acteRepository.findByCategorie(categorie);
    }

    @Override
    public List<Acte> searchActesByLibelle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActes();
        }
        return acteRepository.searchByLibelle(keyword);
    }

    // Méthode privée pour valider les données d'un acte (Logique métier)
    private boolean validerActe(Acte acte) {
        if (acte == null) {
            System.err.println("Erreur : L'acte est null.");
            return false;
        }
        if (acte.getLibelle() == null || acte.getLibelle().trim().isEmpty()) {
            System.err.println("Erreur : Le libellé de l'acte est obligatoire.");
            return false;
        }
        if (acte.getPrixDeBase() == null || acte.getPrixDeBase() < 0) {
            System.err.println("Erreur : Le prix de base doit être positif.");
            return false;
        }
        if (acte.getCategorie() == null) {
            System.err.println("Erreur : La catégorie est obligatoire.");
            return false;
        }
        return true;
    }
}