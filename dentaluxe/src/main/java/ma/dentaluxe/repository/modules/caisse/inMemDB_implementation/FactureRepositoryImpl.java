package ma.dentaluxe.repository.modules.caisse.inMemDB_implementation;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.caisse.Facture;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.repository.modules.caisse.api.FactureRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureRepositoryImpl  implements FactureRepository{

    @Override
    public List<Facture> findAll() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture ORDER BY dateCreation DESC";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                factures.add(mapResultSetToFacture(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public Facture findById(Long id) {
        String sql = "SELECT * FROM facture WHERE idFacture = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFacture(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Facture facture) {
        String sql = "INSERT INTO facture (idSF, idConsultation, totalFacture, totalDesActes, " +
                "montantPaye, reste, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, facture.getIdSF());
            pstmt.setObject(2, facture.getIdConsultation(), Types.BIGINT);
            pstmt.setDouble(3, facture.getTotalFacture());
            pstmt.setDouble(4, facture.getTotalDesActes());
            pstmt.setDouble(5, facture.getMontantPaye());
            pstmt.setDouble(6, facture.getReste());
            pstmt.setString(7, facture.getStatut().name());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                facture.setIdFacture(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Facture facture) {
        String sql = "UPDATE facture SET idSF = ?, idConsultation = ?, totalFacture = ?, " +
                "totalDesActes = ?, montantPaye = ?, reste = ?, statut = ? WHERE idFacture = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, facture.getIdSF());
            pstmt.setObject(2, facture.getIdConsultation(), Types.BIGINT);
            pstmt.setDouble(3, facture.getTotalFacture());
            pstmt.setDouble(4, facture.getTotalDesActes());
            pstmt.setDouble(5, facture.getMontantPaye());
            pstmt.setDouble(6, facture.getReste());
            pstmt.setString(7, facture.getStatut().name());
            pstmt.setLong(8, facture.getIdFacture());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Facture facture) {
        deleteById(facture.getIdFacture());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM facture WHERE idFacture = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Facture> findByStatut(StatutFacture statut) {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture WHERE statut = ? ORDER BY dateCreation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                factures.add(mapResultSetToFacture(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public Facture findByConsultationId(Long idConsultation) {
        String sql = "SELECT * FROM facture WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idConsultation);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToFacture(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Facture> findBySituationFinanciereId(Long idSF) {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM Facture WHERE idSF = ? ORDER BY dateCreation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idSF);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                factures.add(mapResultSetToFacture(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return factures;
    }

    @Override
    public double calculateTotalFacturesImpayees() {
        String sql = "SELECT SUM(reste) as totalImpaye FROM Facture " +
                "WHERE statut IN ('EN_ATTENTE', 'PARTIELLEMENT_PAYEE')";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("totalImpaye");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private Facture mapResultSetToFacture(ResultSet rs) throws SQLException {
        return Facture.builder()
                .idFacture(rs.getLong("idFacture"))
                .idSF(rs.getLong("idSF"))
                .idConsultation((Long) rs.getObject("idConsultation"))
                .totalFacture(rs.getDouble("totalFacture"))
                .totalDesActes(rs.getDouble("totalDesActes"))
                .montantPaye(rs.getDouble("montantPaye"))
                .reste(rs.getDouble("reste"))
                .statut(StatutFacture.valueOf(rs.getString("statut")))
                .dateCreation(rs.getTimestamp("dateCreation").toLocalDateTime())
                .build();
    }
}


