package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class GenerateReportsForm extends JFrame {
    public GenerateReportsForm() {
        setTitle("Generate Reports");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Mock data
        java.util.List<Map<String, String>> appointments = List.of(
                Map.of("Patient", "Ayanda M.", "Doctor", "Dr. Smith", "Date", "2025-09-20"),
                Map.of("Patient", "Thabo N.", "Doctor", "Dr. Patel", "Date", "2025-09-18"),
                Map.of("Patient", "Naledi K.", "Doctor", "Dr. Mokoena", "Date", "2025-09-19")
        );

        java.util.List<Map<String, String>> prescriptions = List.of(
                Map.of("Patient", "Ayanda M.", "Medication", "Ibuprofen", "Date", "2025-09-20"),
                Map.of("Patient", "Thabo N.", "Medication", "Paracetamol", "Date", "2025-09-18"),
                Map.of("Patient", "Naledi K.", "Medication", "Amoxicillin", "Date", "2025-09-19")
        );

        java.util.List<Map<String, String>> patients = List.of(
                Map.of("Name", "Ayanda M.", "Age", "29", "Gender", "Female"),
                Map.of("Name", "Thabo N.", "Age", "34", "Gender", "Male"),
                Map.of("Name", "Naledi K.", "Age", "41", "Gender", "Female")
        );

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
        JTextField dateRangeField = new JTextField("2025-09-18 to 2025-09-20");
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

            java.util.List<Map<String, String>> source = switch (type) {
                case "Appointments" -> appointments;
                case "Prescriptions" -> prescriptions;
                default -> patients;
            };

            for (Map<String, String> record : source) {
                String recordDate = record.get("Date");
                if (type.equals("Patients") || (recordDate != null &&
                        recordDate.compareTo(fromDate) >= 0 && recordDate.compareTo(toDate) <= 0)) {

                    for (Map.Entry<String, String> entry : record.entrySet()) {
                        report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                    report.append("---------------\n");
                }
            }

            resultsArea.setText(report.length() > 0 ? report.toString() : "No records found in the selected range.");
        });

        add(panel);
    }
}