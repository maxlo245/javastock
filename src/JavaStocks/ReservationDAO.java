package JavaStocks;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final String DAO = "ReservationDAO";
    private Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    public void creer(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (date, coureur_id, type_epreuve_id) VALUES (?, ?, ?)";
        DbLogger.sql(DAO, "creer", sql);
        DbLogger.params(reservation.getDate().toString(),
                String.valueOf(reservation.getCoureur().getId()),
                String.valueOf(reservation.getTypeEpreuve().getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                reservation.setId(rs.getInt(1));
            }
            DbLogger.success(DAO, "creer", "Réservation id=" + reservation.getId() + " créée");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creer", e);
            throw e;
        }
        // Insérer les articles réservés
        for (ReservationArticle ra : reservation.getArticles()) {
            String sqlRA = "INSERT INTO ReservationArticle (reservation_id, article_id, quantite) VALUES (?, ?, ?)";
            DbLogger.sql(DAO, "creer[article]", sqlRA);
            DbLogger.params(String.valueOf(reservation.getId()),
                    String.valueOf(ra.getArticle().getId()),
                    String.valueOf(ra.getQuantite()));
            try (PreparedStatement stmtRA = connection.prepareStatement(sqlRA)) {
                stmtRA.setInt(1, reservation.getId());
                stmtRA.setInt(2, ra.getArticle().getId());
                stmtRA.setInt(3, ra.getQuantite());
                stmtRA.executeUpdate();
                DbLogger.success(DAO, "creer[article]", "Article id=" + ra.getArticle().getId() + " lié à réservation id=" + reservation.getId());
            } catch (SQLException e) {
                DbLogger.error(DAO, "creer[article]", e);
                throw e;
            }
        }
    }

    public Reservation consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Reservation WHERE id = ?";
        DbLogger.sql(DAO, "consulter", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Coureur coureur = new CoureurDAO(connection).consulter(rs.getInt("coureur_id"));
                TypeEpreuve typeEpreuve = new TypeEpreuveDAO(connection).consulter(rs.getInt("type_epreuve_id"));
                List<ReservationArticle> articles = listerArticles(id);
                Reservation r = new Reservation(
                    rs.getInt("id"),
                    rs.getDate("date"),
                    coureur,
                    typeEpreuve,
                    articles
                );
                DbLogger.success(DAO, "consulter", "Réservation id=" + id + " trouvée (" + articles.size() + " articles)");
                return r;
            }
            DbLogger.success(DAO, "consulter", "Aucune réservation trouvée pour id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "consulter", e);
            throw e;
        }
        return null;
    }

    public void modifier(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET date = ?, coureur_id = ?, type_epreuve_id = ? WHERE id = ?";
        DbLogger.sql(DAO, "modifier", sql);
        DbLogger.params(reservation.getDate().toString(),
                String.valueOf(reservation.getCoureur().getId()),
                String.valueOf(reservation.getTypeEpreuve().getId()),
                String.valueOf(reservation.getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(reservation.getDate().getTime()));
            stmt.setInt(2, reservation.getCoureur().getId());
            stmt.setInt(3, reservation.getTypeEpreuve().getId());
            stmt.setInt(4, reservation.getId());
            stmt.executeUpdate();
            DbLogger.success(DAO, "modifier", "Réservation id=" + reservation.getId() + " modifiée");
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier", e);
            throw e;
        }
        // Suppression des articles existants
        String sqlDel = "DELETE FROM ReservationArticle WHERE reservation_id = ?";
        DbLogger.sql(DAO, "modifier[supprArticles]", sqlDel);
        try (PreparedStatement stmtDel = connection.prepareStatement(sqlDel)) {
            stmtDel.setInt(1, reservation.getId());
            stmtDel.executeUpdate();
            DbLogger.success(DAO, "modifier[supprArticles]", "Articles supprimés pour réservation id=" + reservation.getId());
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier[supprArticles]", e);
            throw e;
        }
        // Réinsertion des articles
        for (ReservationArticle ra : reservation.getArticles()) {
            String sqlRA = "INSERT INTO ReservationArticle (reservation_id, article_id, quantite) VALUES (?, ?, ?)";
            DbLogger.sql(DAO, "modifier[ajoutArticle]", sqlRA);
            try (PreparedStatement stmtRA = connection.prepareStatement(sqlRA)) {
                stmtRA.setInt(1, reservation.getId());
                stmtRA.setInt(2, ra.getArticle().getId());
                stmtRA.setInt(3, ra.getQuantite());
                stmtRA.executeUpdate();
                DbLogger.success(DAO, "modifier[ajoutArticle]", "Article id=" + ra.getArticle().getId() + " réinséré");
            } catch (SQLException e) {
                DbLogger.error(DAO, "modifier[ajoutArticle]", e);
                throw e;
            }
        }
    }

    public void supprimer(int id) throws SQLException {
        String sqlDel = "DELETE FROM ReservationArticle WHERE reservation_id = ?";
        DbLogger.sql(DAO, "supprimer[articles]", sqlDel);
        try (PreparedStatement stmtDel = connection.prepareStatement(sqlDel)) {
            stmtDel.setInt(1, id);
            stmtDel.executeUpdate();
            DbLogger.success(DAO, "supprimer[articles]", "Articles supprimés pour réservation id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer[articles]", e);
            throw e;
        }
        String sql = "DELETE FROM Reservation WHERE id = ?";
        DbLogger.sql(DAO, "supprimer", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            DbLogger.success(DAO, "supprimer", "Réservation id=" + id + " supprimée");
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer", e);
            throw e;
        }
    }

    public List<Reservation> lister() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservation";
        DbLogger.sql(DAO, "lister", sql);
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
            DbLogger.result(DAO, "lister", reservations.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "lister", e);
            throw e;
        }
        return reservations;
    }

    private List<ReservationArticle> listerArticles(int reservationId) throws SQLException {
        List<ReservationArticle> articles = new ArrayList<>();
        String sql = "SELECT * FROM ReservationArticle WHERE reservation_id = ?";
        DbLogger.sql(DAO, "listerArticles", sql);
        DbLogger.params(String.valueOf(reservationId));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Article article = new ArticleDAO(connection).consulter(rs.getInt("article_id"));
                articles.add(new ReservationArticle(article, rs.getInt("quantite")));
            }
            DbLogger.result(DAO, "listerArticles", articles.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "listerArticles", e);
            throw e;
        }
        return articles;
    }
}
// ...déplacé dans JavaStocks...
