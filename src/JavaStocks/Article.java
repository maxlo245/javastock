package JavaStocks;

public class Article {
    private int id;
    private String libelle;
    private String categorie;
    private int quantite;
    private boolean suppressionLogique;

    public Article(int id, String libelle, String categorie, int quantite, boolean suppressionLogique) {
        this.id = id;
        this.libelle = libelle;
        this.categorie = categorie;
        this.quantite = quantite;
        this.suppressionLogique = suppressionLogique;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public boolean isSuppressionLogique() { return suppressionLogique; }
    public void setSuppressionLogique(boolean suppressionLogique) { this.suppressionLogique = suppressionLogique; }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", categorie='" + categorie + '\'' +
                ", quantite=" + quantite +
                ", suppressionLogique=" + suppressionLogique +
                '}';
    }
}
