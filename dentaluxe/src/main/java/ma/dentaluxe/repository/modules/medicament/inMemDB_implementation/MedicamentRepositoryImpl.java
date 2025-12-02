package ma.dentaluxe.repository.modules.medicament.inMemDB_implementation;

// ===== IMPORTS ENTITÉS =====
import ma.dentaluxe.entities.ordonnance.Medicament;

// ===== IMPORTS REPOSITORY =====
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;

// ===== IMPORTS BASE DE DONNÉES =====
import ma.dentaluxe.conf.Db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

// ===== AUTRES IMPORTS =====
import java.util.ArrayList;
import java.util.List;

// ==============================================================
// AUTEURS : CHLIBAKH OTHMANE
// ==============================================================

/**
 * Implémentation du repository Medicament pour base de données MySQL
 */
public class MedicamentRepositoryImpl implements MedicamentRepository {

    // ==============================================================
    // MÉTHODES DE RECHERCHE
    // ==============================================================

    @Override
    public List<Medicament> findByNom(String nom) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE nom LIKE ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByType(String type) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE type = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByRemboursable(Boolean remboursable) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE remboursable = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, remboursable);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }

    @Override
    public List<Medicament> findByForme(String forme) {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament WHERE forme = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, forme);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }

    // ==============================================================
    // MÉTHODES CRUD DE BASE
    // ==============================================================

    @Override
    public List<Medicament> findAll() {
        List<Medicament> medicaments = new ArrayList<>();
        String sql = "SELECT * FROM medicament ORDER BY nom";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                medicaments.add(mapResultSetToMedicament(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicaments;
    }

    @Override
    public Medicament findById(Long id) {
        String sql = "SELECT * FROM medicament WHERE idMedicament = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMedicament(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Medicament medicament) {
        String sql = "INSERT INTO medicament (nom, type, forme, remboursable, prixUnitaire, description) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, medicament.getNom());
            pstmt.setString(2, medicament.getType());
            pstmt.setString(3, medicament.getForme());
            pstmt.setBoolean(4, medicament.getRemboursable());
            pstmt.setDouble(5, medicament.getPrixUnitaire());

            if (medicament.getDescription() != null) {
                pstmt.setString(6, medicament.getDescription());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }

            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                medicament.setIdMedicament(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Medicament medicament) {
        String sql = "UPDATE medicament SET nom = ?, type = ?, forme = ?, remboursable = ?, prixUnitaire = ?, description = ? WHERE idMedicament = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medicament.getNom());
            pstmt.setString(2, medicament.getType());
            pstmt.setString(3, medicament.getForme());
            pstmt.setBoolean(4, medicament.getRemboursable());
            pstmt.setDouble(5, medicament.getPrixUnitaire());

            if (medicament.getDescription() != null) {
                pstmt.setString(6, medicament.getDescription());
            } else {
                pstmt.setNull(6, Types.VARCHAR);
            }

            pstmt.setLong(7, medicament.getIdMedicament());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Medicament medicament) {
        deleteById(medicament.getIdMedicament());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM medicament WHERE idMedicament = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==============================================================
    // MÉTHODE UTILITAIRE PRIVÉE
    // ==============================================================

    /**
     * Convertit un ResultSet en objet Medicament
     * @param rs ResultSet SQL
     * @return Objet Medicament
     * @throws SQLException en cas d'erreur SQL
     */
    private Medicament mapResultSetToMedicament(ResultSet rs) throws SQLException {
        return Medicament.builder()
                .idMedicament(rs.getLong("idMedicament"))
                .nom(rs.getString("nom"))
                .type(rs.getString("type"))
                .forme(rs.getString("forme"))
                .remboursable(rs.getBoolean("remboursable"))
                .prixUnitaire(rs.getDouble("prixUnitaire"))
                .description(rs.getString("description"))
                .build();
    }
}