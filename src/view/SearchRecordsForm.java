package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class SearchRecordsForm extends JFrame {
    public SearchRecordsForm() {
        setTitle("Search Records");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Mock data
        java.util.List<Map<String, String>> records = new ArrayList<>();
        records.add(Map.of("Patient ID", "Patient001", "Name", "Ayanda M.", "Doctor", "Dr. Smith", "Date", "2025-09-20", "Medication", "Ibuprofen"));
        records.add(Map.of("Patient ID", "Patient002", "Name", "Thabo N.", "Doctor", "Dr. Patel", "Date", "2025-09-18", "Medication", "Paracetamol"));
        records.add(Map.of("Patient ID", "Patient003", "Name", "Naledi K.", "Doctor", "Dr. Mokoena", "Date", "2025-09-19", "Medication", "Amoxicillin"));

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
        scrollPane.setPreferredSize(new Dimension(500, 150));
        panel.add(scrollPane, gbc);

        // Search logic
        searchButton.addActionListener(e -> {
            String type = (String) searchTypeBox.getSelectedItem();
            String keyword = keywordField.getText().trim().toLowerCase();

            StringBuilder results = new StringBuilder();
            for (Map<String, String> record : records) {
                String value = record.get(type);
                if (value != null && value.toLowerCase().contains(keyword)) {
                    results.append("Patient ID: ").append(record.get("Patient ID")).append("\n")
                            .append("Name: ").append(record.get("Name")).append("\n")
                            .append("Doctor: ").append(record.get("Doctor")).append("\n")
                            .append("Date: ").append(record.get("Date")).append("\n")
                            .append("Medication: ").append(record.get("Medication")).append("\n")
                            .append("---------------\n");
                }
            }

            resultsArea.setText(results.length() == 0 ? "No matching records found." : results.toString());
        });

        add(panel);
    }
}