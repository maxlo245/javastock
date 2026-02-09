package JavaStocks;

public class Textile extends Article {
    private String taille;
    private String couleur;

    public Textile(int id, String libelle, int quantite, boolean suppressionLogique, String taille, String couleur) {
        super(id, libelle, "T", quantite, suppressionLogique);
        this.taille = taille;
        this.couleur = couleur;
    }

    public String getTaille() { return taille; }
    public void setTaille(String taille) { this.taille = taille; }
    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    @Override
    public String toString() {
        return super.toString() + ", Textile{taille='" + taille + '\'' + ", couleur='" + couleur + '\'' + '}';
    }
}
