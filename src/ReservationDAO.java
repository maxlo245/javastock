package JavaStocks;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDAO {
    private Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (date, coureur_id, type_epreuve_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reservation.setId(rs.getInt(1));
            }
        }
        // Insérer les articles réservés
        for (ReservationArticle ra : reservation.getArticles()) {
            String sqlRA = "INSERT INTO Reservation_Article (reservation_id, article_id, quantite) VALUES (?, ?, ?)";
            try (PreparedStatement stmtRA = connection.prepareStatement(sqlRA)) {
                stmtRA.setInt(1, reservation.getId());
                stmtRA.setInt(2, ra.getArticle().getId());
                stmtRA.setInt(3, ra.getQuantite());
                stmtRA.executeUpdate();
            }
        }
    }

    public Reservation consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coureur coureur = new CoureurDAO(connection).consulter(rs.getInt("coureur_id"));
                TypeEpreuve typeEpreuve = new TypeEpreuveDAO(connection).consulter(rs.getInt("type_epreuve_id"));
                List<ReservationArticle> articles = listerArticles(id);
                return new Reservation(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    coureur,
                    typeEpreuve,
                    articles
                );
            }
        }
        return null;
    }

    public void modifier(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET date = ?, coureur_id = ?, type_epreuve_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            stmt.setInt(4, reservation.getId());
            stmt.executeUpdate();
        }
        // Suppression des articles existants
        String sqlDel = "DELETE FROM Reservation_Article WHERE reservation_id = ?";
        try (PreparedStatement stmtDel = connection.prepareStatement(sqlDel)) {
            stmtDel.setInt(1, reservation.getId());
            stmtDel.executeUpdate();
        }
        // Réinsertion des articles
        for (ReservationArticle ra : reservation.getArticles()) {
            String sqlRA = "INSERT INTO Reservation_Article (reservation_id, article_id, quantite) VALUES (?, ?, ?)";
            try (PreparedStatement stmtRA = connection.prepareStatement(sqlRA)) {
                stmtRA.setInt(1, reservation.getId());
                stmtRA.setInt(2, ra.getArticle().getId());
                stmtRA.setInt(3, ra.getQuantite());
                stmtRA.executeUpdate();
            }
        }
    }

    public void supprimer(int id) throws SQLException {
        String sqlDel = "DELETE FROM Reservation_Article WHERE reservation_id = ?";
        try (PreparedStatement stmtDel = connection.prepareStatement(sqlDel)) {
            stmtDel.setInt(1, id);
            stmtDel.executeUpdate();
        }
        String sql = "DELETE FROM Reservation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Reservation> lister() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Coureur coureur = new CoureurDAO(connection).consulter(rs.getInt("coureur_id"));
                TypeEpreuve typeEpreuve = new TypeEpreuveDAO(connection).consulter(rs.getInt("type_epreuve_id"));
                List<ReservationArticle> articles = listerArticles(rs.getInt("id"));
                reservations.add(new Reservation(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    coureur,
                    typeEpreuve,
                    articles
                ));
            }
        }
        return reservations;
    }

    private List<ReservationArticle> listerArticles(int reservationId) throws SQLException {
        List<ReservationArticle> articles = new ArrayList<>();
        String sql = "SELECT * FROM Reservation_Article WHERE reservation_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new ArticleDAO(connection).consulter(rs.getInt("article_id"));
                articles.add(new ReservationArticle(article, rs.getInt("quantite")));
            }
        }
        return articles;
    }
}
