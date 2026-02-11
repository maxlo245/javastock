import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FormulaireAleatoire extends JFrame {
    private JTextField nomField, prenomField, libelleEpreuveField, dateField;
    private JButton genererBtn, validerBtn;
    private Random random = new Random();
    private static final String[] NOMS = {"Martin", "Durand", "Dupont", "Bernard", "Petit"};
    private static final String[] PRENOMS = {"Alice", "Jean", "Luc", "Marie", "Sophie"};
    private static final String[] EPREUVES = {"Marathon", "Trail", "10km", "Semi-marathon"};

    public FormulaireAleatoire() {
        setTitle("Formulaire Aléatoire de Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel("Nom :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        add(prenomField);

        add(new JLabel("Type d'épreuve :"));
        libelleEpreuveField = new JTextField();
        add(libelleEpreuveField);

        add(new JLabel("Date (YYYY-MM-DD) :"));
        dateField = new JTextField();
        add(dateField);

        genererBtn = new JButton("Générer aléatoirement");
        validerBtn = new JButton("Valider");
        add(genererBtn);
        add(validerBtn);

        genererBtn.addActionListener(e -> genererAleatoire());
        validerBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Nom : " + nomField.getText() +
                "\nPrénom : " + prenomField.getText() +
                "\nType d'épreuve : " + libelleEpreuveField.getText() +
                "\nDate : " + dateField.getText(), "Données du formulaire", JOptionPane.INFORMATION_MESSAGE));
    }

    private void genererAleatoire() {
        nomField.setText(NOMS[random.nextInt(NOMS.length)]);
        prenomField.setText(PRENOMS[random.nextInt(PRENOMS.length)]);
        libelleEpreuveField.setText(EPREUVES[random.nextInt(EPREUVES.length)]);
        int year = 2025 + random.nextInt(3);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        dateField.setText(String.format("%04d-%02d-%02d", year, month, day));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormulaireAleatoire().setVisible(true));
    }
}
