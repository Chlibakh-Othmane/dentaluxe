package ma.dentaluxe.repository.modules.patient.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;
import ma.dentaluxe.repository.modules.dossierMedical.api.AntecedentsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntecedentRepositoryImpl implements AntecedentRepository, AntecedentsRepository {

    // ==================== MAPPING UTILITAIRE ====================
    private Antecedent map(ResultSet rs) throws SQLException {
        return Antecedent.builder()
                .idAntecedent(rs.getLong("idAntecedent"))
                .nom(rs.getString("nom"))
                .categorie(CategorieAntecedent.valueOf(rs.getString("categorie")))
                .niveauRisque(NiveauRisque.valueOf(rs.getString("niveauDeRisque")))
                .build();
    }

    // ==================== MÉTHODES CRUD DE BASE ====================

    @Override
    public List<Antecedent> findAll() {
        List<Antecedent> list = new ArrayList<>();
        String sql = "SELECT * FROM antecedents ORDER BY nom";
        try (Connection c = Db.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public Antecedent findById(Long id) {
        String sql = "SELECT * FROM antecedents WHERE idAntecedent = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void create(Antecedent a) {
        String sql = "INSERT INTO antecedents (nom, categorie, niveauDeRisque) VALUES (?, ?, ?)";
        try (Connection c = Db.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getNom());
            ps.setString(2, a.getCategorie().name());
            ps.setString(3, a.getNiveauRisque().name());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        long generatedId = rs.getLong(1);
                        // ICI : Maintenant ça va vraiment enregistrer l'ID dans l'objet
                        a.setIdAntecedent(generatedId);
                        System.out.println("DEBUG DATABASE : ID Réel généré = " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("ERREUR SQL CREATE : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Antecedent a) {
        String sql = "UPDATE antecedents SET nom=?, categorie=?, niveauDeRisque=? WHERE idAntecedent=?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNom());
            ps.setString(2, a.getCategorie().name());
            ps.setString(3, a.getNiveauRisque().name());
            ps.setLong(4, a.getIdAntecedent());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(Antecedent a) {
        if (a != null) deleteById(a.getIdAntecedent());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM antecedents WHERE idAntecedent = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ==================== MÉTHODES DE RECHERCHE & FILTRES ====================

    @Override
    public List<Antecedent> findByNom(String nom) {
        List<Antecedent> list = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE nom LIKE ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + nom + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Antecedent> findByCategorie(CategorieAntecedent cat) {
        List<Antecedent> list = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE categorie = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cat.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niv) {
        List<Antecedent> list = new ArrayList<>();
        String sql = "SELECT * FROM antecedents WHERE niveauDeRisque = ?";
        try (Connection c = Db.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, niv.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ==================== MÉTHODES DE LIAISON (Contrat Patient/Dossier) ====================

    @Override
    public List<Antecedent> findByPatientId(Long patientId) {
        // Implémentation vide pour l'instant pour permettre la compilation
        return new ArrayList<>();
    }

    @Override
    public List<Antecedent> findByDossierMedicalId(Long idDM) {
        // Implémentation vide pour l'instant
        return new ArrayList<>();
    }

    @Override public void addAntecedentToPatient(Long pId, Long aId, String notes) {}
    @Override public void removeAntecedentFromPatient(Long pId, Long aId) {}
    @Override public String getNotesForPatient(Long pId, Long aId) { return ""; }
    @Override public void updateNotesForPatient(Long pId, Long aId, String notes) {}
}