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

public class TypeEpreuveMenu {
    private static TypeEpreuveDAO typeEpreuveDAO;

    public static void afficher(Connection connection) {
        typeEpreuveDAO = new TypeEpreuveDAO(connection);

        JFrame frame = new JFrame("JavaStock - Types d'\u00C9preuve");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(500, 380));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainMenu.WARNING);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\uD83C\uDFC6  Types d'\u00C9preuve");
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

        String[] colonnes = {"ID", "Libell\u00E9"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(MainMenu.TABLE_FONT);
        table.setRowHeight(28);
        table.setSelectionBackground(MainMenu.WARNING);
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
        JLabel lblCount = new JLabel("0 types");
        lblCount.setFont(MainMenu.LABEL_FONT);
        lblCount.setForeground(MainMenu.TEXT_DARK);
        barreActions.add(btnRefresh);
        barreActions.add(btnSupprimer);
        barreActions.add(Box.createHorizontalStrut(20));
        barreActions.add(lblCount);

        panelListe.add(barreActions, BorderLayout.NORTH);
        panelListe.add(scrollTable, BorderLayout.CENTER);

        // --- Onglet Cr\u00E9er ---
        JPanel panelCreer = new JPanel();
        panelCreer.setLayout(new BoxLayout(panelCreer, BoxLayout.Y_AXIS));
        panelCreer.setBackground(Color.WHITE);
        panelCreer.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel titreForm = new JLabel("Nouveau Type d'\u00C9preuve");
        titreForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titreForm.setForeground(MainMenu.TEXT_DARK);
        titreForm.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCreer.add(titreForm);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 25)));

        JPanel pLib = ArticleMenu.createFormField("Libell\u00E9 *");
        JTextField txtLibelle = new JTextField();
        ArticleMenu.styleTextField(txtLibelle);
        pLib.add(txtLibelle, BorderLayout.CENTER);
        panelCreer.add(pLib);
        panelCreer.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pBtns.setOpaque(false);
        pBtns.setMaximumSize(new Dimension(500, 45));
        JButton btnEnvoyer = MainMenu.createStyledButton("  \u2714 Enregistrer  ", MainMenu.SUCCESS, MainMenu.SUCCESS_DARK);
        JButton btnReset = MainMenu.createStyledButton("  \u21BB R\u00E9initialiser  ", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        btnEnvoyer.setPreferredSize(new Dimension(160, 38));
        btnReset.setPreferredSize(new Dimension(160, 38));
        pBtns.add(btnEnvoyer);
        pBtns.add(btnReset);
        panelCreer.add(pBtns);

        tabs.addTab("\uD83D\uDCCB Liste", panelListe);
        tabs.addTab("\u2795 Cr\u00E9er", panelCreer);

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

        // === Charger donn\u00E9es ===
        Runnable chargerDonnees = () -> {
            try {
                List<TypeEpreuve> types = typeEpreuveDAO.lister();
                tableModel.setRowCount(0);
                for (TypeEpreuve t : types) {
                    tableModel.addRow(new Object[]{t.getId(), t.getLibelle()});
                }
                lblCount.setText(types.size() + " type" + (types.size() > 1 ? "s" : ""));
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
                JOptionPane.showMessageDialog(frame, "S\u00E9lectionnez un type.", "Info", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
            String libelle = (String) tableModel.getValueAt(table.convertRowIndexToModel(row), 1);
            int confirm = JOptionPane.showConfirmDialog(frame,
                "Supprimer le type \"" + libelle + "\" ?",
                "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    typeEpreuveDAO.supprimer(id);
                    chargerDonnees.run();
                    JOptionPane.showMessageDialog(frame, "Type supprim\u00E9.", "Succ\u00E8s", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnEnvoyer.addActionListener(e -> {
            String libelle = txtLibelle.getText().trim();
            if (libelle.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Le libell\u00E9 est obligatoire !", "Champ manquant", JOptionPane.WARNING_MESSAGE);
                txtLibelle.requestFocus();
                return;
            }
            try {
                typeEpreuveDAO.creer(new TypeEpreuve(0, libelle));
                JOptionPane.showMessageDialog(frame, "Type ajout\u00E9 !\n\n" + libelle, "Succ\u00E8s", JOptionPane.INFORMATION_MESSAGE);
                txtLibelle.setText("");
                txtLibelle.requestFocus();
                chargerDonnees.run();
                tabs.setSelectedIndex(0);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReset.addActionListener(e -> { txtLibelle.setText(""); txtLibelle.requestFocus(); });
        btnFermer.addActionListener(e -> frame.dispose());
        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
