package JavaStocks;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TypeEpreuveDAO {
    private static final String DAO = "TypeEpreuveDAO";
    private Connection connection;

    public TypeEpreuveDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(TypeEpreuve typeEpreuve) throws SQLException {
        String sql = "INSERT INTO TypeEpreuve (libelle) VALUES (?)";
        DbLogger.sql(DAO, "creer", sql);
        DbLogger.params(typeEpreuve.getLibelle());
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            stmt.executeUpdate();
            DbLogger.success(DAO, "creer", "TypeEpreuve '" + typeEpreuve.getLibelle() + "' créé");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creer", e);
            throw e;
        }
    }

    public TypeEpreuve consulter(int id) throws SQLException {
        String sql = "SELECT * FROM TypeEpreuve WHERE id = ?";
        DbLogger.sql(DAO, "consulter", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                TypeEpreuve t = new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                );
                DbLogger.success(DAO, "consulter", "Trouvé: " + t.getLibelle());
                return t;
            }
            DbLogger.success(DAO, "consulter", "Aucun type trouvé pour id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "consulter", e);
            throw e;
        }
        return null;
    }

    public void modifier(TypeEpreuve typeEpreuve) throws SQLException {
        String sql = "UPDATE TypeEpreuve SET libelle = ? WHERE id = ?";
        DbLogger.sql(DAO, "modifier", sql);
        DbLogger.params(typeEpreuve.getLibelle(), String.valueOf(typeEpreuve.getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            stmt.setInt(2, typeEpreuve.getId());
            stmt.executeUpdate();
            DbLogger.success(DAO, "modifier", "TypeEpreuve id=" + typeEpreuve.getId() + " modifié");
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier", e);
            throw e;
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM TypeEpreuve WHERE id = ?";
        DbLogger.sql(DAO, "supprimer", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DbLogger.success(DAO, "supprimer", "TypeEpreuve id=" + id + " supprimé");
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer", e);
            throw e;
        }
    }

    public List<TypeEpreuve> lister() throws SQLException {
        List<TypeEpreuve> types = new ArrayList<>();
        String sql = "SELECT * FROM TypeEpreuve";
        DbLogger.sql(DAO, "lister", sql);
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                types.add(new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                ));
            }
            DbLogger.result(DAO, "lister", types.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "lister", e);
            throw e;
        }
        return types;
    }
}
