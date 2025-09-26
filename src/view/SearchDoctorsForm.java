package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchDoctorsForm extends JFrame {
    private JTextField specialtyField;
    private JTable resultsTable;

    public SearchDoctorsForm() {
        setTitle("Search Doctors");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 🔹 Top search panel
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Specialization:"));
        specialtyField = new JTextField(20);
        searchPanel.add(specialtyField);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // 🔹 Results table
        resultsTable = new JTable(new DefaultTableModel(
                new Object[]{"Name", "Specialty", "Phone", "Email", "License No", "Available", "Working Hours"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);

        // 🔹 Search button action
        searchButton.addActionListener(e -> searchDoctors());
    }

    private void searchDoctors() {
        String specialty = specialtyField.getText().trim();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT full_name, specialty, phone, email, license_no, available, working_hours " +
                             "FROM doctors WHERE specialty LIKE ?")) {

            stmt.setString(1, "%" + specialty + "%");
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
            model.setRowCount(0); // Clear previous results

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("full_name"),
                        rs.getString("specialty"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("license_no"),
                        rs.getBoolean("available") ? "Yes" : "No",
                        rs.getString("working_hours")
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No doctors found for this specialization.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage());
        }
    }
}
