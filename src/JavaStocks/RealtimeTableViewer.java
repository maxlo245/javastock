import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class RealtimeTableViewer extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private Connection connection;
    private String tableName;

    public RealtimeTableViewer(String url, String user, String password, String tableName) {
        super("Visualisation temps réel : " + tableName);
        this.tableName = tableName;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion : " + e.getMessage());
            System.exit(1);
        }
        Timer timer = new Timer(5000, e -> refreshTable());
        timer.start();
        refreshTable();
    }

    private void refreshTable() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            Vector<String> columns = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columns.add(meta.getColumnName(i));
            }
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            tableModel.setDataVector(data, columns);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/javastocks";
        String user = "admin";
        String password = "root";
        String tableName = args.length > 0 ? args[0] : "article";
        SwingUtilities.invokeLater(() -> new RealtimeTableViewer(url, user, password, tableName).setVisible(true));
    }
}
