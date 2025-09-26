package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class GenerateReportsForm extends JFrame {
    public GenerateReportsForm() {
        setTitle("Generate Reports");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Report type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Report Type:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> reportTypeBox = new JComboBox<>(new String[]{"Appointments", "Prescriptions", "Patients"});
        panel.add(reportTypeBox, gbc);

        // Row 1: Date range
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Date Range:"), gbc);
        gbc.gridx = 1;
        JTextField dateRangeField = new JTextField("2025-09-01 to 2025-09-30");
        panel.add(dateRangeField, gbc);

        // Row 2: Generate button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton generateButton = new JButton("Generate");
        panel.add(generateButton, gbc);

        // Row 3: Results area
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JTextArea resultsArea = new JTextArea("Report output will appear here...");
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(550, 150));
        panel.add(scrollPane, gbc);

        // Logic for generating reports
        generateButton.addActionListener(e -> {
            String type = (String) reportTypeBox.getSelectedItem();
            String range = dateRangeField.getText().trim();
            String[] dates = range.split("to");
            String fromDate = dates.length > 0 ? dates[0].trim() : "";
            String toDate = dates.length > 1 ? dates[1].trim() : "";

            StringBuilder report = new StringBuilder("📄 " + type + " Report\n\n");

            try (Connection conn = ConnectionBD.getConnection()) {
                PreparedStatement stmt = null;

                if ("Appointments".equals(type)) {
                    stmt = conn.prepareStatement(
                            "SELECT a.id, p.full_name AS patient, d.full_name AS doctor, a.appointment_date, a.time_slot, a.status, a.reason " +
                                    "FROM appointments a " +
                                    "JOIN patients p ON a.patient_id = p.id " +
                                    "JOIN doctors d ON a.doctor_id = d.id " +
                                    "WHERE a.appointment_date BETWEEN ? AND ?");
                    stmt.setString(1, fromDate);
                    stmt.setString(2, toDate);
                } else if ("Prescriptions".equals(type)) {
                    stmt = conn.prepareStatement(
                            "SELECT pr.id, p.full_name AS patient, d.full_name AS doctor, pr.medicine, pr.dosage, pr.instructions, pr.created_at " +
                                    "FROM prescriptions pr " +
                                    "JOIN appointments a ON pr.appointment_id = a.id " +
                                    "JOIN patients p ON a.patient_id = p.id " +
                                    "JOIN doctors d ON pr.prescribed_by = d.id " +
                                    "WHERE pr.created_at BETWEEN ? AND ?");
                    stmt.setString(1, fromDate + " 00:00:00");
                    stmt.setString(2, toDate + " 23:59:59");
                } else { // Patients
                    stmt = conn.prepareStatement(
                            "SELECT id, full_name, dob, gender, phone, email FROM patients");
                }

                ResultSet rs = stmt.executeQuery();

                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();

                while (rs.next()) {
                    for (int i = 1; i <= colCount; i++) {
                        report.append(meta.getColumnLabel(i))
                                .append(": ")
                                .append(rs.getString(i))
                                .append("\n");
                    }
                    report.append("---------------\n");
                }

                resultsArea.setText(report.length() > 0 ? report.toString() : "No records found in the selected range.");

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
            }
        });

        add(panel);
    }
}
