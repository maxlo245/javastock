package JavaStocks;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationMenu {
    private static ReservationDAO reservationDAO;
    private static CoureurDAO coureurDAO;
    private static TypeEpreuveDAO typeEpreuveDAO;
    private static ArticleDAO articleDAO;

    public static void afficher(Connection connection) {
        reservationDAO = new ReservationDAO(connection);
        coureurDAO = new CoureurDAO(connection);
        typeEpreuveDAO = new TypeEpreuveDAO(connection);
        articleDAO = new ArticleDAO(connection);

        JFrame frame = new JFrame("Formulaire Réservation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 550);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Titre
        JLabel titre = new JLabel("Nouvelle Réservation");
        titre.setFont(new Font("Arial", Font.BOLD, 18));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titre);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Date (automatique)
        JPanel panelDate = new JPanel(new BorderLayout(10, 5));
        panelDate.setMaximumSize(new Dimension(500, 40));
        JLabel lblDate = new JLabel("Date:");
        lblDate.setPreferredSize(new Dimension(120, 25));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        JLabel lblDateValue = new JLabel(sdf.format(new Date()));
        lblDateValue.setFont(new Font("Arial", Font.BOLD, 14));
        panelDate.add(lblDate, BorderLayout.WEST);
        panelDate.add(lblDateValue, BorderLayout.CENTER);
        mainPanel.add(panelDate);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sélection Coureur
        JPanel panelCoureur = new JPanel(new BorderLayout(10, 5));
        panelCoureur.setMaximumSize(new Dimension(500, 60));
        JLabel lblCoureur = new JLabel("Coureur:");
        lblCoureur.setPreferredSize(new Dimension(120, 25));
        JComboBox<Coureur> cboCoureur = new JComboBox<>();
        cboCoureur.setPreferredSize(new Dimension(320, 25));
        panelCoureur.add(lblCoureur, BorderLayout.WEST);
        panelCoureur.add(cboCoureur, BorderLayout.CENTER);
        mainPanel.add(panelCoureur);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sélection Type d'épreuve
        JPanel panelType = new JPanel(new BorderLayout(10, 5));
        panelType.setMaximumSize(new Dimension(500, 60));
        JLabel lblType = new JLabel("Type d'épreuve:");
        lblType.setPreferredSize(new Dimension(120, 25));
        JComboBox<TypeEpreuve> cboType = new JComboBox<>();
        cboType.setPreferredSize(new Dimension(320, 25));
        panelType.add(lblType, BorderLayout.WEST);
        panelType.add(cboType, BorderLayout.CENTER);
        mainPanel.add(panelType);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Sélection Article
        JPanel panelArticle = new JPanel(new BorderLayout(10, 5));
        panelArticle.setMaximumSize(new Dimension(500, 60));
        JLabel lblArticle = new JLabel("Article:");
        lblArticle.setPreferredSize(new Dimension(120, 25));
        JComboBox<Article> cboArticle = new JComboBox<>();
        cboArticle.setPreferredSize(new Dimension(320, 25));
        panelArticle.add(lblArticle, BorderLayout.WEST);
        panelArticle.add(cboArticle, BorderLayout.CENTER);
        mainPanel.add(panelArticle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Quantité
        JPanel panelQuantite = new JPanel(new BorderLayout(10, 5));
        panelQuantite.setMaximumSize(new Dimension(500, 60));
        JLabel lblQuantite = new JLabel("Quantité:");
        lblQuantite.setPreferredSize(new Dimension(120, 25));
        JSpinner spinQuantite = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinQuantite.setPreferredSize(new Dimension(80, 25));
        JPanel quantiteWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        quantiteWrapper.add(spinQuantite);
        panelQuantite.add(lblQuantite, BorderLayout.WEST);
        panelQuantite.add(quantiteWrapper, BorderLayout.CENTER);
        mainPanel.add(panelQuantite);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Liste des articles ajoutés
        JPanel panelListe = new JPanel(new BorderLayout());
        panelListe.setMaximumSize(new Dimension(500, 100));
        JLabel lblListe = new JLabel("Articles sélectionnés:");
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> listeArticles = new JList<>(listModel);
        JScrollPane scrollListe = new JScrollPane(listeArticles);
        scrollListe.setPreferredSize(new Dimension(480, 80));
        panelListe.add(lblListe, BorderLayout.NORTH);
        panelListe.add(scrollListe, BorderLayout.CENTER);
        mainPanel.add(panelListe);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Bouton Ajouter article
        JButton btnAjouterArticle = new JButton("+ Ajouter cet article");
        btnAjouterArticle.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAjouterArticle.setPreferredSize(new Dimension(180, 30));
        mainPanel.add(btnAjouterArticle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Liste pour stocker les articles sélectionnés
        List<ReservationArticle> articlesSelectionnes = new ArrayList<>();

        // Boutons principaux
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

        // Charger les données
        try {
            List<Coureur> coureurs = coureurDAO.lister();
            for (Coureur c : coureurs) {
                cboCoureur.addItem(c);
            }

            List<TypeEpreuve> types = typeEpreuveDAO.lister();
            for (TypeEpreuve t : types) {
                cboType.addItem(t);
            }

            List<Article> articles = articleDAO.lister();
            for (Article a : articles) {
                cboArticle.addItem(a);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Erreur de chargement: " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Personnaliser l'affichage des ComboBox
        cboCoureur.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Coureur) {
                    Coureur c = (Coureur) value;
                    setText(c.getNom() + " " + c.getPrenom());
                }
                return this;
            }
        });

        cboType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TypeEpreuve) {
                    TypeEpreuve t = (TypeEpreuve) value;
                    setText(t.getLibelle());
                }
                return this;
            }
        });

        cboArticle.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Article) {
                    Article a = (Article) value;
                    setText(a.getLibelle() + " (" + a.getCategorie() + ") - Dispo: " + a.getQuantite());
                }
                return this;
            }
        });

        // Action Ajouter article à la liste
        btnAjouterArticle.addActionListener(e -> {
            Article selectedArticle = (Article) cboArticle.getSelectedItem();
            int quantite = (Integer) spinQuantite.getValue();

            if (selectedArticle != null) {
                articlesSelectionnes.add(new ReservationArticle(selectedArticle, quantite));
                listModel.addElement(selectedArticle.getLibelle() + " x " + quantite);
                JOptionPane.showMessageDialog(frame, "Article ajouté à la sélection!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Action Envoyer
        btnEnvoyer.addActionListener(e -> {
            Coureur coureur = (Coureur) cboCoureur.getSelectedItem();
            TypeEpreuve typeEpreuve = (TypeEpreuve) cboType.getSelectedItem();

            if (coureur == null) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un coureur!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (typeEpreuve == null) {
                JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un type d'épreuve!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (articlesSelectionnes.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Veuillez ajouter au moins un article!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Reservation reservation = new Reservation(0, new Date(), coureur, typeEpreuve, articlesSelectionnes);
                reservationDAO.creer(reservation);

                StringBuilder message = new StringBuilder("Réservation créée avec succès!\n\n");
                message.append("Coureur: ").append(coureur.getNom()).append(" ").append(coureur.getPrenom()).append("\n");
                message.append("Type: ").append(typeEpreuve.getLibelle()).append("\n");
                message.append("Articles: ").append(articlesSelectionnes.size()).append("\n");

                JOptionPane.showMessageDialog(frame, message.toString(),
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

                // Réinitialiser
                articlesSelectionnes.clear();
                listModel.clear();
                cboCoureur.setSelectedIndex(0);
                cboType.setSelectedIndex(0);
                cboArticle.setSelectedIndex(0);
                spinQuantite.setValue(1);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement: " + ex.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Réinitialiser
        btnReinitialiser.addActionListener(e -> {
            articlesSelectionnes.clear();
            listModel.clear();
            if (cboCoureur.getItemCount() > 0) cboCoureur.setSelectedIndex(0);
            if (cboType.getItemCount() > 0) cboType.setSelectedIndex(0);
            if (cboArticle.getItemCount() > 0) cboArticle.setSelectedIndex(0);
            spinQuantite.setValue(1);
        });

        // Action Fermer
        btnFermer.addActionListener(e -> frame.dispose());

        JScrollPane scrollMain = new JScrollPane(mainPanel);
        scrollMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollMain.getVerticalScrollBar().setUnitIncrement(16);

        frame.setContentPane(scrollMain);
        frame.setVisible(true);
    }
}
