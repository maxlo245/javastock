package JavaStocks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Menu Historique : affiche les logs BDD en temps réel dans l'interface.
 * Permet de filtrer par niveau (SQL, OK, ERROR, CONN) et par texte.
 */
public class HistoriqueMenu {

    public static void afficher() {

        JFrame frame = new JFrame("JavaStock - Historique des opérations");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 560);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(750, 450));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(MainMenu.LIGHT_BG);

        // === Header ===
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MainMenu.DARK_LIGHT);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JLabel lblTitle = new JLabel("\uD83D\uDCC8  Historique des opérations");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(MainMenu.TEXT_LIGHT);
        header.add(lblTitle, BorderLayout.WEST);

        JLabel lblCount = new JLabel();
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblCount.setForeground(new Color(149, 165, 166));
        header.add(lblCount, BorderLayout.EAST);

        // === Barre de filtres ===
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterBar.setBackground(MainMenu.LIGHT_BG);
        filterBar.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 15));

        JLabel lblFiltre = new JLabel("Filtrer :");
        lblFiltre.setFont(MainMenu.LABEL_FONT);
        filterBar.add(lblFiltre);

        String[] niveaux = {"Tous", "SQL", "OK", "ERROR", "CONN", "INFO"};
        JComboBox<String> cboNiveau = new JComboBox<>(niveaux);
        cboNiveau.setFont(MainMenu.LABEL_FONT);
        filterBar.add(cboNiveau);

        JTextField txtRecherche = new JTextField(20);
        txtRecherche.setFont(MainMenu.LABEL_FONT);
        txtRecherche.setToolTipText("Rechercher dans les logs...");
        txtRecherche.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        filterBar.add(txtRecherche);

        JButton btnRefresh = MainMenu.createStyledButton("\u21BB Actualiser", MainMenu.PRIMARY, MainMenu.PRIMARY_DARK);
        JButton btnClear = MainMenu.createStyledButton("\uD83D\uDDD1 Vider", MainMenu.DANGER, new Color(192, 57, 43));
        filterBar.add(btnRefresh);
        filterBar.add(btnClear);

        // === Tableau ===
        String[] colonnes = {"Heure", "Niveau", "Source", "Message", "Paramètres"};
        DefaultTableModel tableModel = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Consolas", Font.PLAIN, 12));
        table.setRowHeight(26);
        table.setSelectionBackground(MainMenu.PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(MainMenu.TABLE_HEADER);
        table.getTableHeader().setBackground(MainMenu.DARK);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setAutoCreateRowSorter(true);

        // Largeurs de colonnes
        table.getColumnModel().getColumn(0).setPreferredWidth(130);  // Heure
        table.getColumnModel().getColumn(1).setPreferredWidth(60);   // Niveau
        table.getColumnModel().getColumn(2).setPreferredWidth(160);  // Source
        table.getColumnModel().getColumn(3).setPreferredWidth(320);  // Message
        table.getColumnModel().getColumn(4).setPreferredWidth(200);  // Params

        // Renderer coloré pour le niveau
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    String level = value != null ? value.toString() : "";
                    switch (level) {
                        case "SQL":
                            c.setForeground(MainMenu.PRIMARY);
                            c.setBackground(new Color(232, 245, 253));
                            break;
                        case "OK":
                            c.setForeground(MainMenu.SUCCESS);
                            c.setBackground(new Color(234, 250, 241));
                            break;
                        case "ERROR":
                            c.setForeground(Color.WHITE);
                            c.setBackground(MainMenu.DANGER);
                            break;
                        case "CONN":
                        case "DISC":
                            c.setForeground(new Color(142, 68, 173));
                            c.setBackground(new Color(245, 238, 248));
                            break;
                        case "INFO":
                            c.setForeground(MainMenu.WARNING);
                            c.setBackground(new Color(254, 249, 231));
                            break;
                        default:
                            c.setForeground(MainMenu.TEXT_DARK);
                            c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });

        // Renderer pour les lignes ERROR (texte rouge)
        DefaultTableCellRenderer errorAwareRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    int modelRow = t.convertRowIndexToModel(row);
                    String level = (String) tableModel.getValueAt(modelRow, 1);
                    if ("ERROR".equals(level)) {
                        c.setForeground(MainMenu.DANGER);
                        c.setBackground(new Color(253, 237, 236));
                    } else {
                        c.setForeground(MainMenu.TEXT_DARK);
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(errorAwareRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(errorAwareRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(errorAwareRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(errorAwareRenderer);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBackground(MainMenu.LIGHT_BG);
        panelTable.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        panelTable.add(scrollTable, BorderLayout.CENTER);

        // === Footer ===
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(MainMenu.LIGHT_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        JLabel lblInfo = new JLabel("Les logs sont stockés en mémoire (max 500 entrées) — se vident à la fermeture de l'application");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblInfo.setForeground(new Color(149, 165, 166));
        footer.add(lblInfo, BorderLayout.WEST);

        JButton btnFermer = MainMenu.createStyledButton("  Fermer  ", MainMenu.DARK_LIGHT, MainMenu.DARK);
        btnFermer.setPreferredSize(new Dimension(100, 35));
        footer.add(btnFermer, BorderLayout.EAST);

        // === Assemblage ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(filterBar, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(panelTable, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        // === Chargement des données ===
        Runnable chargerDonnees = () -> {
            List<DbLogger.LogEntry> entries = DbLogger.getHistory();
            tableModel.setRowCount(0);
            for (DbLogger.LogEntry entry : entries) {
                tableModel.addRow(new Object[]{
                    entry.getTimestampStr(),
                    entry.level,
                    entry.source,
                    entry.message,
                    entry.detail != null ? entry.detail : ""
                });
            }
            lblCount.setText(entries.size() + " entrée(s)");

            // Scroller en bas pour voir les logs les plus récents
            if (table.getRowCount() > 0) {
                table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
            }
        };
        chargerDonnees.run();

        // === Filtrage ===
        Runnable appliquerFiltre = () -> {
            String niveau = (String) cboNiveau.getSelectedItem();
            String texte = txtRecherche.getText().trim().toLowerCase();

            sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    // Filtre par niveau
                    if (!"Tous".equals(niveau)) {
                        String lvl = entry.getStringValue(1);
                        if (!niveau.equals(lvl)) return false;
                    }
                    // Filtre par texte
                    if (!texte.isEmpty()) {
                        for (int i = 0; i < entry.getValueCount(); i++) {
                            String val = entry.getStringValue(i).toLowerCase();
                            if (val.contains(texte)) return true;
                        }
                        return false;
                    }
                    return true;
                }
            });
        };

        cboNiveau.addActionListener(e -> appliquerFiltre.run());
        txtRecherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { appliquerFiltre.run(); }
            public void removeUpdate(DocumentEvent e) { appliquerFiltre.run(); }
            public void changedUpdate(DocumentEvent e) { appliquerFiltre.run(); }
        });

        // === Actions ===
        btnRefresh.addActionListener(e -> {
            chargerDonnees.run();
            appliquerFiltre.run();
        });

        btnClear.addActionListener(e -> {
            DbLogger.clearHistory();
            chargerDonnees.run();
        });

        btnFermer.addActionListener(e -> frame.dispose());

        frame.getRootPane().registerKeyboardAction(e -> frame.dispose(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Auto-refresh toutes les 3 secondes
        javax.swing.Timer autoRefresh = new javax.swing.Timer(3000, e -> {
            if (frame.isVisible()) {
                int oldCount = tableModel.getRowCount();
                chargerDonnees.run();
                appliquerFiltre.run();
                // Si de nouvelles entrées, scroller en bas
                if (tableModel.getRowCount() > oldCount && table.getRowCount() > 0) {
                    table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
                }
            }
        });
        autoRefresh.start();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                autoRefresh.stop();
            }
        });

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
