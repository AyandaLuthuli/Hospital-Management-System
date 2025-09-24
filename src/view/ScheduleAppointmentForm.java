package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAppointmentForm extends JFrame {
    public ScheduleAppointmentForm() {
        setTitle("Schedule Appointment");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Dummy patient data
        Map<String, String[]> patientData = new HashMap<>();
        patientData.put("Patient001", new String[]{"Ayanda M.", "29", "Female", "082-123-4567"});
        patientData.put("Patient002", new String[]{"Thabo N.", "34", "Male", "083-456-7890"});
        patientData.put("Patient003", new String[]{"Naledi K.", "41", "Female", "084-321-6543"});

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

        // Row 4: Contact
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        JLabel contactValue = new JLabel("-");
        panel.add(contactValue, gbc);

        // Row 5: Doctor
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Select Doctor:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> doctorBox = new JComboBox<>(new String[]{"Dr. Smith", "Dr. Patel", "Dr. Mokoena"});
        panel.add(doctorBox, gbc);

        // Row 6: Date
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        JTextField dateField = new JTextField();
        panel.add(dateField, gbc);

        // Row 7: Time
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Time Slot:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> timeBox = new JComboBox<>(new String[]{
                "09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM"
        });
        panel.add(timeBox, gbc);

        // Row 8: Reason
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Reason for Visit:"), gbc);
        gbc.gridx = 1;
        JTextField reasonField = new JTextField();
        panel.add(reasonField, gbc);

        // Row 9: Button
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        JButton scheduleButton = new JButton("Schedule Appointment");
        panel.add(scheduleButton, gbc);

        // Update patient details on selection
        patientBox.addActionListener(e -> {
            String selectedId = (String) patientBox.getSelectedItem();
            String[] details = patientData.get(selectedId);
            if (details != null) {
                nameValue.setText(details[0]);
                ageValue.setText(details[1]);
                genderValue.setText(details[2]);
                contactValue.setText(details[3]);
            }
        });

        // Placeholder scheduling logic
        scheduleButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Appointment scheduled for " + nameValue.getText() +
                            " with " + doctorBox.getSelectedItem() +
                            " on " + dateField.getText() + " at " + timeBox.getSelectedItem()
            );
        });

        add(panel);
    }
}