package JavaStocks;

public class TypeEpreuve {
    private int id;
    private String libelle;

        // ...déplacé dans JavaStocks...
        this.id = id;
        this.libelle = libelle;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    @Override
    public String toString() {
        return "TypeEpreuve{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
