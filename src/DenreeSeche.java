package JavaStocks;

public class DenreeSeche extends Article {
    private float poids;

    public DenreeSeche(int id, String libelle, int quantite, boolean suppressionLogique, float poids) {
        super(id, libelle, "DS", quantite, suppressionLogique);
        this.poids = poids;
    }

    public float getPoids() { return poids; }
    public void setPoids(float poids) { this.poids = poids; }

    @Override
    public String toString() {
        return super.toString() + ", DenreeSeche{poids=" + poids + '}';
    }
}
