package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ManagePrescriptionsForm extends JFrame {
    public ManagePrescriptionsForm() {
        setTitle("Manage Prescriptions");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Dummy patient data
        Map<String, String[]> patientData = new HashMap<>();
        patientData.put("Patient001", new String[]{"Ayanda M.", "29", "Female"});
        patientData.put("Patient002", new String[]{"Thabo N.", "34", "Male"});
        patientData.put("Patient003", new String[]{"Naledi K.", "41", "Female"});

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Patient dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Patient:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> patientBox = new JComboBox<>(patientData.keySet().toArray(new String[0]));
        panel.add(patientBox, gbc);

        // Row 1: Name
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JLabel nameValue = new JLabel("-");
        panel.add(nameValue, gbc);

        // Row 2: Age
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        JLabel ageValue = new JLabel("-");
        panel.add(ageValue, gbc);

        // Row 3: Gender
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        JLabel genderValue = new JLabel("-");
        panel.add(genderValue, gbc);

        // Row 4: Medication
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Medication:"), gbc);
        gbc.gridx = 1;
        JTextField medicationField = new JTextField();
        panel.add(medicationField, gbc);

        // Row 5: Dosage
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1;
        JTextField dosageField = new JTextField();
        panel.add(dosageField, gbc);

        // Row 6: Notes
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Instructions/Notes:"), gbc);
        gbc.gridx = 1;
        JTextField notesField = new JTextField();
        panel.add(notesField, gbc);

        // Row 7: Save button
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        JButton saveButton = new JButton("Save Prescription");
        panel.add(saveButton, gbc);

        // Update patient details on selection
        patientBox.addActionListener(e -> {
            String selectedId = (String) patientBox.getSelectedItem();
            String[] details = patientData.get(selectedId);
            if (details != null) {
                nameValue.setText(details[0]);
                ageValue.setText(details[1]);
                genderValue.setText(details[2]);
            }
        });

        // Save logic (placeholder)
        saveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Prescription saved for " + nameValue.getText() +
                            "\nMedication: " + medicationField.getText() +
                            "\nDosage: " + dosageField.getText()
            );
        });

        add(panel);
    }
}