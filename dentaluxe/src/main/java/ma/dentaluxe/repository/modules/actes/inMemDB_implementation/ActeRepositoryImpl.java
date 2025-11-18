//AUTEUR : AYA LEZREGUE
package ma.dentaluxe.repository.modules.actes.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class ActeRepositoryImpl implements ActeRepository {

    @Override
    public List<Acte> findByCategorie(CategorieActe categorie) {

        List<Acte> actes = new ArrayList<>();
         String sql = "SELECT * FROM acte WHERE categorie = ?";
          try (Connection conn = Db.getConnection();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, categorie.name());
               ResultSet rs = pstmt.executeQuery();

                  while (rs.next()) {
                       actes.add(mapResultSetToActe(rs));
                  }
                } catch (SQLException e) {
                  e.printStackTrace();
          }
          return actes;
    }

    @Override
    public List<Acte> searchByLibelle(String keyword) {
            List<Acte> actes = new ArrayList<>();
            String sql = "SELECT * FROM acte WHERE libelle LIKE ?";

             try (Connection conn = Db.getConnection();
                  PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, "%" + keyword + "%");
                 ResultSet rs = pstmt.executeQuery();


                  while (rs.next()) {
                       actes.add(mapResultSetToActe(rs));
                  }
                   } catch (SQLException e) {
                 e.printStackTrace();
             }
              return actes;

    }

    @Override
    public List<Acte> findAll() {
        List<Acte> actes = new ArrayList<>();//On crée une liste vide pour stocker les objets Acte qu’on va extraire de la base.
        String sql = "SELECT * FROM acte";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                actes.add(mapResultSetToActe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actes;
    }
      //transformer une ligne de résultat SQL (ResultSet) en objet Java Acte
    private Acte mapResultSetToActe(ResultSet rs) throws SQLException {
         return Acte.builder()     //pour créer un objet Acte
            .idActe(rs.getLong("idActe"))
            .libelle(rs.getString("libelle"))
            .description(rs.getString("description"))
            .prixDeBase(rs.getDouble("prixDeBase"))
             .categorie(CategorieActe.valueOf(rs.getString("categorie")))  
              .build(); //    Termine la construction de l’objet Acte
    }

    @Override
    public Acte findById(Long id) {
        String sql = "SELECT * FROM acte WHERE idActe = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);//donne 1 a idActe pour ne pas donner une erreure a cause de (?)
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToActe(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Acte acte) {
        String sql = "INSERT INTO acte (libelle, description, prixDeBase, categorie, idInterventionMedecin) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, acte.getLibelle());//Remplace le premier ? de la requête SQL par la valeur du libellé de l’acte.
            pstmt.setString(2, acte.getDescription());
            pstmt.setDouble(3, acte.getPrixDeBase());
            pstmt.setString(4, acte.getCategorie().name());

            if (acte.getIdInterventionMedecin() != null) {
                pstmt.setLong(5, acte.getIdInterventionMedecin());
            } else {
                pstmt.setNull(5, java.sql.Types.BIGINT);
            }

            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                acte.setIdActe(rs.getLong(1));//RECUPERER LE PREIER ID GENERER ET met à jour l’objet Acte en mémoire avec son nouvel identifiant.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Acte acte) {

        String sql = "UPDATE acte SET libelle = ?, description = ?, prixDeBase = ?, categorie = ?, idInterventionMedecin = ? WHERE idActe = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, acte.getLibelle());
            pstmt.setString(2, acte.getDescription());
            pstmt.setDouble(3, acte.getPrixDeBase());
            pstmt.setString(4, acte.getCategorie().name());


            if (acte.getIdInterventionMedecin() != null) {
                pstmt.setLong(5, acte.getIdInterventionMedecin());
            } else {
                pstmt.setNull(5, java.sql.Types.BIGINT);
            }
            pstmt.setLong(6, acte.getIdActe());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Acte acte) {
        deleteById(acte.getIdActe());
    }

      @Override
         public void deleteById(Long id) {
          String sql = "DELETE FROM acte WHERE idActe = ?";

          try (Connection conn = Db.getConnection();
               PreparedStatement pstmt = conn.prepareStatement(sql)) {

              pstmt.setLong(1, id);
              pstmt.executeUpdate();

               } catch (SQLException e) {

                  e.printStackTrace();    }

      }   }