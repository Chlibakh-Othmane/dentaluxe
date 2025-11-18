package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.dossierMedical.api.AntecedentsRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntecedentsRepositoryImpl implements AntecedentsRepository {

    @Override
    public List<Antecedents> findAll() {
        List<Antecedents> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM Antecedents";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedents(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public Antecedents findById(Long id) {
        String sql = "SELECT * FROM Antecedents WHERE idAntecedent = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAntecedents(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Antecedents antecedent) {
        String sql = "INSERT INTO Antecedents (idDM, nom, categorie, niveauDeRisque) VALUES (?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, antecedent.getIdDM());
            pstmt.setString(2, antecedent.getNom());
            pstmt.setString(3, antecedent.getCategorie().name());
            pstmt.setString(4, antecedent.getNiveauDeRisque().name());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                antecedent.setIdAntecedent(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Antecedents antecedent) {
        String sql = "UPDATE Antecedents SET idDM = ?, nom = ?, categorie = ?, niveauDeRisque = ? WHERE idAntecedent = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, antecedent.getIdDM());
            pstmt.setString(2, antecedent.getNom());
            pstmt.setString(3, antecedent.getCategorie().name());
            pstmt.setString(4, antecedent.getNiveauDeRisque().name());
            pstmt.setLong(5, antecedent.getIdAntecedent());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Antecedents antecedent) {
        deleteById(antecedent.getIdAntecedent());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Antecedents WHERE idAntecedent = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Antecedents> findByDossierMedicalId(Long idDM) {
        List<Antecedents> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM Antecedents WHERE idDM = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedents(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public List<Antecedents> findByCategorie(CategorieAntecedent categorie) {
        List<Antecedents> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM Antecedents WHERE categorie = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categorie.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedents(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public List<Antecedents> findByNiveauRisque(NiveauRisque niveauRisque) {
        List<Antecedents> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM Antecedents WHERE niveauDeRisque = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, niveauRisque.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedents(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    private Antecedents mapResultSetToAntecedents(ResultSet rs) throws SQLException {
        return Antecedents.builder()
                .idAntecedent(rs.getLong("idAntecedent"))
                .idDM(rs.getLong("idDM"))
                .nom(rs.getString("nom"))
                .categorie(CategorieAntecedent.valueOf(rs.getString("categorie")))
                .niveauDeRisque(NiveauRisque.valueOf(rs.getString("niveauDeRisque")))
                .build();
    }
}