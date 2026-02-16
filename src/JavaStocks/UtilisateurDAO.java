package JavaStocks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour les opérations sur les utilisateurs.
 */
public class UtilisateurDAO {
    private static final String DAO = "UtilisateurDAO";
    private Connection connection;

    public UtilisateurDAO(Connection connection) {
        this.connection = connection;
    }

    /** Masque partiellement un nom d'utilisateur pour les logs */
    private static String mask(String username) {
        if (username == null || username.length() <= 2) return "***";
        return username.charAt(0) + "*".repeat(username.length() - 2) + username.charAt(username.length() - 1);
    }

    /**
     * Crée la table Utilisateur si elle n'existe pas.
     * Appelé au démarrage pour assurer la compatibilité.
     */
    public void creerTableSiAbsente() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Utilisateur (" +
                "id SERIAL PRIMARY KEY, " +
                "nom_utilisateur VARCHAR(50) UNIQUE NOT NULL, " +
                "mot_de_passe VARCHAR(255) NOT NULL, " +
                "nom VARCHAR(50) NOT NULL, " +
                "prenom VARCHAR(50) NOT NULL, " +
                "role VARCHAR(20) NOT NULL DEFAULT 'utilisateur' CHECK (role IN ('admin', 'utilisateur')), " +
                "date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "actif BOOLEAN DEFAULT TRUE)";
        DbLogger.sql(DAO, "creerTableSiAbsente", sql);
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            DbLogger.success(DAO, "creerTableSiAbsente", "Table Utilisateur vérifiée/créée");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creerTableSiAbsente", e);
            throw e;
        }

        // Créer un admin par défaut si aucun utilisateur n'existe
        String countSql = "SELECT COUNT(*) FROM Utilisateur";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                creer(new Utilisateur("admin", "admin", "Administrateur", "Système"));
                DbLogger.info("Utilisateur admin par défaut créé (login: admin / mdp: admin)");
            }
        }
    }

    /** Inscription d'un nouvel utilisateur */
    public void creer(Utilisateur user) throws SQLException {
        String sql = "INSERT INTO Utilisateur (nom_utilisateur, mot_de_passe, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";
        DbLogger.sql(DAO, "creer", "INSERT INTO Utilisateur (...) VALUES (?, ?, ?, ?, ?)");
        DbLogger.params(mask(user.getNomUtilisateur()), "[MASQU\u00c9]", "***", "***", user.getRole());
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getNomUtilisateur());
            stmt.setString(2, user.getMotDePasse());
            stmt.setString(3, user.getNom());
            stmt.setString(4, user.getPrenom());
            stmt.setString(5, user.getRole());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            DbLogger.success(DAO, "creer", "Utilisateur '" + user.getNomUtilisateur() + "' créé (id=" + user.getId() + ")");
        } catch (SQLException e) {
            DbLogger.error(DAO, "creer", e);
            throw e;
        }
    }

    /** Authentification : retourne l'utilisateur ou null si échec */
    public Utilisateur authentifier(String nomUtilisateur, String motDePasse) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = ? AND actif = TRUE";
        DbLogger.sql(DAO, "authentifier", "SELECT * FROM Utilisateur WHERE nom_utilisateur = ? AND mot_de_passe = ? AND actif = TRUE");
        DbLogger.params(mask(nomUtilisateur), "[MASQU\u00c9]");
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = mapResultSet(rs);
                DbLogger.success(DAO, "authentifier", "Connexion r\u00e9ussie pour '" + mask(nomUtilisateur) + "' (role=" + u.getRole() + ")");
                return u;
            }
            DbLogger.success(DAO, "authentifier", "\u00c9chec d'authentification pour '" + mask(nomUtilisateur) + "'");
        } catch (SQLException e) {
            DbLogger.error(DAO, "authentifier", e);
            throw e;
        }
        return null;
    }

    /** Vérifie si un nom d'utilisateur existe déjà */
    public boolean existeNomUtilisateur(String nomUtilisateur) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Utilisateur WHERE nom_utilisateur = ?";
        DbLogger.sql(DAO, "existeNomUtilisateur", sql);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                DbLogger.success(DAO, "existeNomUtilisateur", mask(nomUtilisateur) + " \u2192 " + (existe ? "existe" : "disponible"));
                return existe;
            }
        } catch (SQLException e) {
            DbLogger.error(DAO, "existeNomUtilisateur", e);
            throw e;
        }
        return false;
    }

    /** Consulter un utilisateur par ID */
    public Utilisateur consulter(int id) throws SQLException {
        String sql = "SELECT * FROM Utilisateur WHERE id = ?";
        DbLogger.sql(DAO, "consulter", sql);
        DbLogger.params(String.valueOf(id));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur u = mapResultSet(rs);
                DbLogger.success(DAO, "consulter", "Trouvé: " + u.getNomUtilisateur());
                return u;
            }
            DbLogger.success(DAO, "consulter", "Aucun utilisateur trouvé pour id=" + id);
        } catch (SQLException e) {
            DbLogger.error(DAO, "consulter", e);
            throw e;
        }
        return null;
    }

    /** Modifier un utilisateur */
    public void modifier(Utilisateur user) throws SQLException {
        String sql = "UPDATE Utilisateur SET nom = ?, prenom = ?, role = ?, actif = ? WHERE id = ?";
        DbLogger.sql(DAO, "modifier", sql);
        DbLogger.params("***", "***", user.getRole(),
                String.valueOf(user.isActif()), String.valueOf(user.getId()));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getRole());
            stmt.setBoolean(4, user.isActif());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
            DbLogger.success(DAO, "modifier", "Utilisateur id=" + user.getId() + " modifié");
        } catch (SQLException e) {
            DbLogger.error(DAO, "modifier", e);
            throw e;
        }
    }

    /** Changer le mot de passe */
    public void changerMotDePasse(int userId, String nouveauMotDePasse) throws SQLException {
        String sql = "UPDATE Utilisateur SET mot_de_passe = ? WHERE id = ?";
        DbLogger.sql(DAO, "changerMotDePasse", "UPDATE Utilisateur SET mot_de_passe = ? WHERE id = ?");
        DbLogger.params("[MASQU\u00c9]", String.valueOf(userId));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nouveauMotDePasse);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            DbLogger.success(DAO, "changerMotDePasse", "Mot de passe changé pour id=" + userId);
        } catch (SQLException e) {
            DbLogger.error(DAO, "changerMotDePasse", e);
            throw e;
        }
    }

    /** Supprimer définitivement un utilisateur */
    public void supprimer(int userId) throws SQLException {
        String sql = "DELETE FROM Utilisateur WHERE id = ?";
        DbLogger.sql(DAO, "supprimer", sql);
        DbLogger.params(String.valueOf(userId));
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            DbLogger.success(DAO, "supprimer", rows + " utilisateur(s) supprimé(s) pour id=" + userId);
        } catch (SQLException e) {
            DbLogger.error(DAO, "supprimer", e);
            throw e;
        }
    }

    /** Lister tous les utilisateurs */
    public List<Utilisateur> lister() throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur ORDER BY date_creation DESC";
        DbLogger.sql(DAO, "lister", sql);
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSet(rs));
            }
            DbLogger.result(DAO, "lister", users.size());
        } catch (SQLException e) {
            DbLogger.error(DAO, "lister", e);
            throw e;
        }
        return users;
    }

    /** Mapper un ResultSet vers un Utilisateur */
    private Utilisateur mapResultSet(ResultSet rs) throws SQLException {
        return new Utilisateur(
            rs.getInt("id"),
            rs.getString("nom_utilisateur"),
            rs.getString("mot_de_passe"),
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("role"),
            rs.getTimestamp("date_creation"),
            rs.getBoolean("actif")
        );
    }
}
