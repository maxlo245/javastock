package JavaStocks;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class TypeEpreuveMenu {
    private static TypeEpreuveDAO typeEpreuveDAO;

    public static void afficher(Connection connection) {
        typeEpreuveDAO = new TypeEpreuveDAO(connection);

        JFrame frame = new JFrame("Formulaire Type d'Épreuve");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Titre
        JLabel titre = new JLabel("Nouveau Type d'Épreuve");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titre);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Champ Libellé
        JPanel panelLibelle = new JPanel(new BorderLayout(10, 5));
        panelLibelle.setMaximumSize(new Dimension(400, 60));
        JLabel lblLibelle = new JLabel("Libellé:");
        lblLibelle.setPreferredSize(new Dimension(100, 25));
        JTextField txtLibelle = new JTextField();
        txtLibelle.setPreferredSize(new Dimension(250, 25));
        panelLibelle.add(lblLibelle, BorderLayout.WEST);
        panelLibelle.add(txtLibelle, BorderLayout.CENTER);
        mainPanel.add(panelLibelle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Boutons
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        JButton btnEnvoyer = new JButton("Envoyer");
        btnEnvoyer.setPreferredSize(new Dimension(120, 35));
        btnEnvoyer.setBackground(new Color(34, 139, 34));
        btnEnvoyer.setForeground(Color.WHITE);
        btnEnvoyer.setFocusPainted(false);

        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.setPreferredSize(new Dimension(120, 35));

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setPreferredSize(new Dimension(120, 35));

        panelBoutons.add(btnEnvoyer);
        panelBoutons.add(btnReinitialiser);
        panelBoutons.add(btnFermer);
        mainPanel.add(panelBoutons);

        // Action Envoyer
        btnEnvoyer.addActionListener(e -> {
            String libelle = txtLibelle.getText().trim();

            if (libelle.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le libellé est obligatoire!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                TypeEpreuve typeEpreuve = new TypeEpreuve(0, libelle);
                typeEpreuveDAO.creer(typeEpreuve);

                JOptionPane.showMessageDialog(frame,
                    "Type d'épreuve ajouté avec succès!\n\nLibellé: " + libelle,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser le formulaire
                txtLibelle.setText("");
                txtLibelle.requestFocus();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Réinitialiser
        btnReinitialiser.addActionListener(e -> {
            txtLibelle.setText("");
            txtLibelle.requestFocus();
        });

        // Action Fermer
        btnFermer.addActionListener(e -> frame.dispose());

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
