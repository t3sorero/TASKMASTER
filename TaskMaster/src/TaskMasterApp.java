import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class TaskMasterApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskMasterFrame().setVisible(true));
    }
}

class TaskMasterFrame extends JFrame {

    public TaskMasterFrame() {
        setTitle("TaskMaster - Juego Relacionar Conceptos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
        add(new JuegoRelacionarConceptosPanel());
    }
}

class JuegoRelacionarConceptosPanel extends JPanel {

    private final Map<String, String> conceptos;
    private final Map<String, JComboBox<String>> respuestas;

    public JuegoRelacionarConceptosPanel() {
        SQLiteConexion db = new SQLiteConexion();
        conceptos = db.obtenerConceptos();

        respuestas = new HashMap<>();

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Relaciona cada definición con su concepto", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        JPanel centroPanel = new JPanel(new GridLayout(conceptos.size(), 2, 20, 10));

        java.util.List<String> definiciones = new ArrayList<>(conceptos.values());
        Collections.shuffle(definiciones);

        for (String definicion : definiciones) {
            JLabel label = new JLabel("<html><body style='width: 250px;'>" + definicion + "</body></html>");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JComboBox<String> combo = new JComboBox<>(conceptos.keySet().toArray(new String[0]));
            combo.setSelectedIndex(-1);
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            respuestas.put(definicion, combo);

            centroPanel.add(label);
            centroPanel.add(combo);
        }

        add(centroPanel, BorderLayout.CENTER);

        JButton finalizarBtn = new JButton("Finalizar");
        finalizarBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        finalizarBtn.setBackground(new Color(66, 133, 244));
        finalizarBtn.setForeground(Color.WHITE);
        finalizarBtn.setFocusPainted(false);
        finalizarBtn.setPreferredSize(new Dimension(150, 40));

        finalizarBtn.addActionListener(e -> mostrarResultados(definiciones));

        JPanel southPanel = new JPanel();
        southPanel.add(finalizarBtn);
        add(southPanel, BorderLayout.SOUTH);
        add(centroPanel, BorderLayout.CENTER);
    }

    private void mostrarResultados(java.util.List<String> definiciones) {
        int aciertos = 0;
        for (String definicion : definiciones) {
            String seleccion = (String) respuestas.get(definicion).getSelectedItem();
            if (seleccion != null && conceptos.get(seleccion).equals(definicion)) {
                aciertos++;
            }
        }

        JOptionPane.showMessageDialog(this,
                "<html><h2 style='font-family: Segoe UI;'>Resultados</h2>"
                + "<p style='font-size: 14px;'>✅ Aciertos: <b>" + aciertos + "</b><br>"
                + "❌ Errores: <b>" + (definiciones.size() - aciertos) + "</b></p></html>",
                "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }
}

class SQLiteConexion {

    private final String url = "jdbc:sqlite:conceptos.db";

    public Map<String, String> obtenerConceptos() {
        Map<String, String> conceptos = new LinkedHashMap<>();

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT concepto, definicion FROM conceptos")) {

            while (rs.next()) {
                conceptos.put(rs.getString("concepto"), rs.getString("definicion"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conceptos;
    }
}
