package JavaStocks;
import java.util.Date;
import java.util.List;

public class Reservation {
    private int id;
    private Date date;
    private Coureur coureur;
    private TypeEpreuve typeEpreuve;
    private List<ReservationArticle> articles;

    public Reservation(int id, Date date, Coureur coureur, TypeEpreuve typeEpreuve, List<ReservationArticle> articles) {
        this.id = id;
        this.date = date;
        this.coureur = coureur;
        this.typeEpreuve = typeEpreuve;
        this.articles = articles;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Coureur getCoureur() { return coureur; }
    public void setCoureur(Coureur coureur) { this.coureur = coureur; }
    public TypeEpreuve getTypeEpreuve() { return typeEpreuve; }
    public void setTypeEpreuve(TypeEpreuve typeEpreuve) { this.typeEpreuve = typeEpreuve; }
    public List<ReservationArticle> getArticles() { return articles; }
    public void setArticles(List<ReservationArticle> articles) { this.articles = articles; }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", coureur=" + coureur +
                ", typeEpreuve=" + typeEpreuve +
                ", articles=" + articles +
                '}';
    }
}
