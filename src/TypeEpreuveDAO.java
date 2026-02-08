package JavaStocks;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeEpreuveDAO {
    private Connection connection;

    public TypeEpreuveDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(TypeEpreuve typeEpreuve) throws SQLException {
        String sql = "INSERT INTO Type_epreuve (libelle) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            stmt.executeUpdate();
        }
    }

    public TypeEpreuve consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Type_epreuve WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                );
            }
        }
        return null;
    }

    public void modifier(TypeEpreuve typeEpreuve) throws SQLException {
        String sql = "UPDATE Type_epreuve SET libelle = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, typeEpreuve.getLibelle());
            stmt.setInt(2, typeEpreuve.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM Type_epreuve WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<TypeEpreuve> lister() throws SQLException {
        List<TypeEpreuve> types = new ArrayList<>();
        String sql = "SELECT * FROM Type_epreuve";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                types.add(new TypeEpreuve(
                    rs.getInt("id"),
                    rs.getString("libelle")
                ));
            }
        }
        return types;
    }
}
