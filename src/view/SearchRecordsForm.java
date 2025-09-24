package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class SearchRecordsForm extends JFrame {

    public SearchRecordsForm() {
        setTitle("Search Records");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search type
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Search by:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> searchTypeBox = new JComboBox<>(new String[]{"Patient ID", "Name", "Doctor", "Date", "Medication"});
        panel.add(searchTypeBox, gbc);

        // Keyword
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Keyword:"), gbc);
        gbc.gridx = 1;
        JTextField keywordField = new JTextField();
        panel.add(keywordField, gbc);

        // Search button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton searchButton = new JButton("Search");
        panel.add(searchButton, gbc);

        // Results area
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JTextArea resultsArea = new JTextArea("Search results will appear here...");
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(550, 200));
        panel.add(scrollPane, gbc);

        // Search logic
        searchButton.addActionListener(e -> {
            String type = (String) searchTypeBox.getSelectedItem();
            String keyword = keywordField.getText().trim().toLowerCase();
            StringBuilder results = new StringBuilder();

            try (Connection conn = ConnectionBD.getConnection()) {
                // Query joining patients, doctors, appointments, prescriptions
                String sql = """
                    SELECT 
                        p.id AS patient_id,
                        p.full_name AS patient_name,
                        d.full_name AS doctor_name,
                        a.appointment_date,
                        pr.medicine
                    FROM prescriptions pr
                    JOIN appointments a ON pr.appointment_id = a.id
                    JOIN patients p ON a.patient_id = p.id
                    JOIN doctors d ON a.doctor_id = d.id
                """;

                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String patientId = String.valueOf(rs.getInt("patient_id"));
                    String name = rs.getString("patient_name");
                    String doctor = rs.getString("doctor_name");
                    String date = rs.getDate("appointment_date").toString();
                    String medication = rs.getString("medicine");

                    boolean match = switch (type) {
                        case "Patient ID" -> patientId.toLowerCase().contains(keyword);
                        case "Name" -> name.toLowerCase().contains(keyword);
                        case "Doctor" -> doctor.toLowerCase().contains(keyword);
                        case "Date" -> date.toLowerCase().contains(keyword);
                        case "Medication" -> medication.toLowerCase().contains(keyword);
                        default -> false;
                    };

                    if (match) {
                        results.append("Patient ID: ").append(patientId).append("\n")
                                .append("Name: ").append(name).append("\n")
                                .append("Doctor: ").append(doctor).append("\n")
                                .append("Date: ").append(date).append("\n")
                                .append("Medication: ").append(medication).append("\n")
                                .append("----------------------\n");
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                results.append("Error fetching records: ").append(ex.getMessage());
            }

            resultsArea.setText(results.length() == 0 ? "No matching records found." : results.toString());
        });

        add(panel);
    }
}
