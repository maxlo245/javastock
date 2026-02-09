package JavaStocks;

public class ReservationArticle {
    private Article article;
    private int quantite;

    public ReservationArticle(Article article, int quantite) {
        this.article = article;
        this.quantite = quantite;
    }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    @Override
    public String toString() {
        return "ReservationArticle{" +
                "article=" + article +
                ", quantite=" + quantite +
                '}';
    }
}
