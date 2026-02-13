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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

public class CoureurMenu {
    private static CoureurDAO coureurDAO;

    public static void afficher(Connection connection) {
        coureurDAO = new CoureurDAO(connection);

        JFrame frame = new JFrame("JavaStock - Coureurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(650, 480);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(550, 420));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(142, 68, 173));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\uD83C\uDFC3  Gestion des Coureurs");
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

        String[] colonnes = {"ID", "Nom", "Prénom"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(MainMenu.TABLE_FONT);
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(142, 68, 173));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.getTableHeader().setFont(MainMenu.TABLE_HEADER);
        table.getTableHeader().setBackground(MainMenu.DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel barreActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        barreActions.setBackground(MainMenu.LIGHT_BG);
        JButton btnRefresh = MainMenu.createStyledButton("\u21BB Actualiser", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnSupprimer = MainMenu.createStyledButton("\uD83D\uDDD1 Supprimer", MainMenu.DANGER, new Color(192, 57, 43));
        JLabel lblCount = new JLabel("0 coureurs");
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

        JLabel titreForm = new JLabel("Nouveau Coureur");
        titreForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titreForm.setForeground(MainMenu.TEXT_DARK);
        titreForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreer.add(titreForm);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel pNom = ArticleMenu.createFormField("Nom *");
        JTextField txtNom = new JTextField();
        ArticleMenu.styleTextField(txtNom);
        pNom.add(txtNom, BorderLayout.CENTER);
        panelCreer.add(pNom);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel pPrenom = ArticleMenu.createFormField("Prénom *");
        JTextField txtPrenom = new JTextField();
        ArticleMenu.styleTextField(txtPrenom);
        pPrenom.add(txtPrenom, BorderLayout.CENTER);
        panelCreer.add(pPrenom);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 30)));

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

        // === Charger données ===
        Runnable chargerDonnees = () -> {
            try {
                List<Coureur> coureurs = coureurDAO.lister();
                tableModel.setRowCount(0);
                for (Coureur c : coureurs) {
                    tableModel.addRow(new Object[]{c.getId(), c.getNom(), c.getPrenom()});
                }
                lblCount.setText(coureurs.size() + " coureur" + (coureurs.size() > 1 ? "s" : ""));
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
                JOptionPane.showMessageDialog(frame, "Sélectionnez un coureur.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
            String nom = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
            String prenom = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 2);
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Supprimer le coureur \"" + nom + " " + prenom + "\" ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    coureurDAO.supprimer(id);
                    chargerDonnees.run();
                    JOptionPane.showMessageDialog(frame, "Coureur supprimé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEnvoyer.addActionListener(e -> {
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            if (nom.isEmpty() || prenom.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le nom et le prénom sont obligatoires !", "Champ manquant", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                coureurDAO.creer(new Coureur(0, nom, prenom));
                JOptionPane.showMessageDialog(frame, "Coureur ajouté !\n\n" + nom + " " + prenom, "Succès", JOptionPane.INFORMATION_MESSAGE);
                txtNom.setText("");
                txtPrenom.setText("");
                txtNom.requestFocus();
                chargerDonnees.run();
                tabs.setSelectedIndex(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> { txtNom.setText(""); txtPrenom.setText(""); txtNom.requestFocus(); });
        btnFermer.addActionListener(e -> frame.dispose());
        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
