package JavaStocks;

import java.sql.Timestamp;

/**
 * Modèle représentant un utilisateur de l'application.
 */
public class Utilisateur {
    private int id;
    private String nomUtilisateur;
    private String motDePasse;
    private String nom;
    private String prenom;
    private String role;          // "admin" ou "utilisateur"
    private Timestamp dateCreation;
    private boolean actif;

    public Utilisateur(int id, String nomUtilisateur, String motDePasse,
                       String nom, String prenom, String role,
                       Timestamp dateCreation, boolean actif) {
        this.id = id;
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.dateCreation = dateCreation;
        this.actif = actif;
    }

    /** Constructeur simplifié pour la création */
    public Utilisateur(String nomUtilisateur, String motDePasse, String nom, String prenom) {
        this(0, nomUtilisateur, motDePasse, nom, prenom, "utilisateur", null, true);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public boolean isAdmin() { return "admin".equals(role); }

    public String getNomComplet() { return prenom + " " + nom; }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
}
