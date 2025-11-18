//AUTEUR : AYA LEZREGUE
package ma.dentaluxe.repository.modules.caisse.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.finance.Facture;
import ma.dentaluxe.entities.finance.SituationFinanciere;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.repository.modules.caisse.api.SituationFinanciereRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SituationFinanciereRepositoryImpl implements  SituationFinanciereRepository {

    @Override
    public SituationFinanciere findByDossierMedicalId(Long idDM) {
        String sql = "SELECT * FROM situationfinanciere WHERE idDM = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSituationFinanciere(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateAfterPayment(Long idSF, double montantPaye) {
        String sql = "UPDATE situationfinanciere SET totalPaye = totalPaye + ?, " +
                "resteDu = totalDesActes - (totalPaye + ?), " +
                "statut = CASE WHEN (totalDesActes - (totalPaye + ?)) = 0 THEN 'SOLDE' " +
                "WHEN (totalDesActes - (totalPaye + ?)) < 0 THEN 'CREDIT' " +
                "ELSE 'DEBIT' END WHERE idSF = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, montantPaye);
            pstmt.setDouble(2, montantPaye);
            pstmt.setDouble(3, montantPaye);
            pstmt.setDouble(4, montantPaye);
            pstmt.setLong(5, idSF);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<SituationFinanciere> findAll() {
        List<SituationFinanciere> situations = new ArrayList<>();
        String sql = "SELECT * FROM situationfinanciere";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                situations.add(mapResultSetToSituationFinanciere(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return situations;
    }

    @Override
    public SituationFinanciere findById(Long id) {
        String sql = "SELECT * FROM situationfinanciere WHERE idSF = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSituationFinanciere(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void create(SituationFinanciere sf) {
        String sql = "INSERT INTO situationfinanciere (idDM, totalDesActes, totalPaye, resteDu, " +
                "creance, statut, enPromo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, sf.getIdDM());
            pstmt.setDouble(2, sf.getTotalDesActes());
            pstmt.setDouble(3, sf.getTotalPaye());
            pstmt.setDouble(4, sf.getResteDu());
            pstmt.setDouble(5, sf.getCreance());
            pstmt.setString(6, sf.getStatut().name());
            pstmt.setBoolean(7, sf.getEnPromo());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                sf.setIdSF(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(SituationFinanciere sf) {
        String sql = "UPDATE situationfinanciere SET idDM = ?, totalDesActes= ?, totalPaye= ?, resteDu=? , creance= ?, statut= ?, enPromo= ? WHERE idSF = ? ";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, sf.getIdDM());
            pstmt.setDouble(2, sf.getTotalDesActes());
            pstmt.setDouble(3, sf.getTotalPaye());
            pstmt.setDouble(4, sf.getResteDu());
            pstmt.setDouble(5, sf.getCreance());
            pstmt.setString(6, sf.getStatut().name());
            pstmt.setBoolean(7, sf.getEnPromo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void delete(SituationFinanciere sf) {
        deleteById(sf.getIdSF());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM situationfinanciere WHERE idSF = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private SituationFinanciere mapResultSetToSituationFinanciere(ResultSet rs) throws SQLException {
        return SituationFinanciere.builder()
                .idSF(rs.getLong("idSF"))
                .idDM(rs.getLong("idDM"))
                .totalDesActes(rs.getDouble("totalDesActes"))
                .totalPaye(rs.getDouble("totalPaye"))
                .resteDu(rs.getDouble("resteDu"))
                .creance(rs.getDouble("creance"))
                .statut(StatutSituationFinanciere.valueOf(rs.getString("statut")))
                .enPromo(rs.getBoolean("enPromo"))
                .build();
    }


    }

