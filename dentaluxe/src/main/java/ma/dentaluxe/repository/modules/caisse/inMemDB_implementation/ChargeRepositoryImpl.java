package ma.dentaluxe.repository.modules.caisse.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.caisse.Charge;
import ma.dentaluxe.repository.modules.caisse.api.ChargeRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChargeRepositoryImpl implements ChargeRepository {
    @Override
    public void create(Charge c) {
        String sql = "INSERT INTO Charge (titre, description, montant, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTitre());
            ps.setString(2, c.getDescription());
            ps.setDouble(3, c.getMontant());
            ps.setTimestamp(4, Timestamp.valueOf(c.getDate()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) c.setIdCharge(rs.getLong(1));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public List<Charge> findAll() {
        List<Charge> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Charge ORDER BY date DESC")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Méthodes update/delete/findById standard...
    @Override public Charge findById(Long id) { return null; } // À implémenter si besoin
    @Override public void update(Charge charge) {} // À implémenter si besoin
    @Override public void delete(Charge charge) {} // À implémenter si besoin
    @Override public void deleteById(Long id) {} // À implémenter si besoin

    private Charge map(ResultSet rs) throws SQLException {
        return Charge.builder()
                .idCharge(rs.getLong("idCharge"))
                .titre(rs.getString("titre"))
                .description(rs.getString("description"))
                .montant(rs.getDouble("montant"))
                .date(rs.getTimestamp("date").toLocalDateTime())
                .build();
    }
}