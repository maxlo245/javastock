package JavaStocks;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class ArticleMenu {
    private static ArticleDAO articleDAO;

    public static void afficher(Connection connection) {
        articleDAO = new ArticleDAO(connection);

        JFrame frame = new JFrame("JavaStock - Articles");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 520);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(600, 450));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainMenu.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\uD83D\uDCE6  Gestion des Articles");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MainMenu.TEXT_LIGHT);
        header.add(lblTitle, BorderLayout.WEST);

        // === Onglets ===
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(MainMenu.BTN_FONT);

        // --- Onglet Liste ---
        JPanel panelListe = new JPanel(new BorderLayout(0, 10));
        panelListe.setBackground(MainMenu.LIGHT_BG);
        panelListe.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] colonnes = {"ID", "Libellé", "Catégorie", "Quantité", "Statut"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(MainMenu.TABLE_FONT);
        table.setRowHeight(28);
        table.setSelectionBackground(MainMenu.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(MainMenu.TABLE_HEADER);
        table.getTableHeader().setBackground(MainMenu.DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        // Cacher la colonne ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Barre d'actions liste
        JPanel barreActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barreActions.setBackground(MainMenu.LIGHT_BG);
        JButton btnRefresh = MainMenu.createStyledButton("\u21BB Actualiser", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnSupprimer = MainMenu.createStyledButton("\uD83D\uDDD1 Supprimer", MainMenu.DANGER, new Color(192, 57, 43));
        JLabel lblCount = new JLabel("0 articles");
        lblCount.setFont(MainMenu.LABEL_FONT);
        lblCount.setForeground(MainMenu.TEXT_DARK);
        barreActions.add(btnRefresh);
        barreActions.add(btnSupprimer);
        barreActions.add(Box.createHorizontalStrut(20));
        barreActions.add(lblCount);

        panelListe.add(barreActions, BorderLayout.NORTH);
        panelListe.add(scrollTable, BorderLayout.CENTER);

        // --- Onglet Créer ---
        JPanel panelCreer = new JPanel();
        panelCreer.setLayout(new BoxLayout(panelCreer, BoxLayout.Y_AXIS));
        panelCreer.setBackground(Color.WHITE);
        panelCreer.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titreForm = new JLabel("Nouvel Article");
        titreForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titreForm.setForeground(MainMenu.TEXT_DARK);
        titreForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreer.add(titreForm);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 25)));

        // Champ Libellé
        JPanel pLib = createFormField("Libellé *");
        JTextField txtLibelle = new JTextField();
        styleTextField(txtLibelle);
        pLib.add(txtLibelle, BorderLayout.CENTER);
        panelCreer.add(pLib);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 12)));

        // Champ Catégorie
        JPanel pCat = createFormField("Catégorie *");
        String[] categories = {"Textile", "Boisson", "DenreeSeche"};
        JComboBox<String> cboCategorie = new JComboBox<>(categories);
        cboCategorie.setFont(MainMenu.LABEL_FONT);
        pCat.add(cboCategorie, BorderLayout.CENTER);
        panelCreer.add(pCat);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 12)));

        // Champ Quantité
        JPanel pQte = createFormField("Quantité *");
        JSpinner spinQuantite = new JSpinner(new SpinnerNumberModel(0, 0, 99999, 1));
        spinQuantite.setFont(MainMenu.LABEL_FONT);
        pQte.add(spinQuantite, BorderLayout.CENTER);
        panelCreer.add(pQte);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 30)));

        // Boutons formulaire
        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pBtns.setOpaque(false);
        pBtns.setMaximumSize(new Dimension(500, 45));
        JButton btnEnvoyer = MainMenu.createStyledButton("  \u2714 Enregistrer  ", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        JButton btnReset = MainMenu.createStyledButton("  \u21BB Réinitialiser  ", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        btnEnvoyer.setPreferredSize(new Dimension(160, 38));
        btnReset.setPreferredSize(new Dimension(160, 38));
        pBtns.add(btnEnvoyer);
        pBtns.add(btnReset);
        panelCreer.add(pBtns);

        tabs.addTab("\uD83D\uDCCB Liste", panelListe);
        tabs.addTab("\u2795 Créer", panelCreer);

        // === Footer ===
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(MainMenu.LIGHT_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        JButton btnFermer = MainMenu.createStyledButton("  Fermer  ", MainMenu.DARK_LIGHT, MainMenu.DARK);
        btnFermer.setPreferredSize(new Dimension(100, 35));
        footer.add(btnFermer);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // === Charger les données ===
        Runnable chargerDonnees = () -> {
            try {
                List<Article> articles = articleDAO.lister();
                tableModel.setRowCount(0);
                for (Article a : articles) {
                    String statut = a.getQuantite() == 0 ? "\u26A0 Rupture" : (a.getQuantite() < 5 ? "\u26A0 Stock bas" : "\u2705 OK");
                    tableModel.addRow(new Object[]{a.getId(), a.getLibelle(), a.getCategorie(), a.getQuantite(), statut});
                }
                lblCount.setText(articles.size() + " article" + (articles.size() > 1 ? "s" : ""));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        };
        chargerDonnees.run();

        // === Actions ===
        btnRefresh.addActionListener(e -> chargerDonnees.run());

        btnSupprimer.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Sélectionnez un article dans la liste.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
            String nom = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Voulez-vous vraiment supprimer l'article \"" + nom + "\" ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    articleDAO.supprimer(id);
                    chargerDonnees.run();
                    JOptionPane.showMessageDialog(frame, "Article supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEnvoyer.addActionListener(e -> {
            String libelle = txtLibelle.getText().trim();
            if (libelle.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le libellé est obligatoire !", "Champ manquant", JOptionPane.WARNING_MESSAGE);
                txtLibelle.requestFocus();
                return;
            }
            try {
                int quantite = (Integer) spinQuantite.getValue();
                String categorie = (String) cboCategorie.getSelectedItem();
                Article article = new Article(0, libelle, categorie, quantite, false);
                articleDAO.creer(article);
                JOptionPane.showMessageDialog(frame,
                    "Article ajouté avec succès !\n\n" + libelle + " (" + categorie + ") × " + quantite,
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                txtLibelle.setText("");
                spinQuantite.setValue(0);
                cboCategorie.setSelectedIndex(0);
                txtLibelle.requestFocus();
                chargerDonnees.run();
                tabs.setSelectedIndex(0); // Revenir à la liste
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> {
            txtLibelle.setText("");
            spinQuantite.setValue(0);
            cboCategorie.setSelectedIndex(0);
            txtLibelle.requestFocus();
        });

        btnFermer.addActionListener(e -> frame.dispose());

        // Raccourci Escape pour fermer
        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /** Crée un champ de formulaire avec label */
    static JPanel createFormField(String label) {
        JPanel p = new JPanel(new BorderLayout(10, 5));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(450, 45));
        JLabel lbl = new JLabel(label);
        lbl.setFont(MainMenu.LABEL_FONT);
        lbl.setForeground(MainMenu.TEXT_DARK);
        lbl.setPreferredSize(new Dimension(110, 25));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    /** Applique un style moderne à un JTextField */
    static void styleTextField(JTextField tf) {
        tf.setFont(MainMenu.LABEL_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }
}
