package JavaStocks;

public class Boisson extends Article {
    private float volume;

    public Boisson(int id, String libelle, int quantite, boolean suppressionLogique, float volume) {
        super(id, libelle, "B", quantite, suppressionLogique);
        this.volume = volume;
    }

    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }

    @Override
    public String toString() {
        return super.toString() + ", Boisson{volume=" + volume + '}';
    }
}
