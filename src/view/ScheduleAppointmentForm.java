package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAppointmentForm extends JFrame {
    private JComboBox<String> patientBox;
    private JLabel nameValue, ageValue, genderValue, contactValue;
    private JComboBox<String> doctorBox;
    private JTextField dateField, reasonField;
    private JComboBox<String> timeBox;

    private Map<String, Integer> patientMap = new HashMap<>(); // key = display name, value = patient_id
    private Map<String, Integer> doctorMap = new HashMap<>();  // key = display name, value = doctor_id

    public ScheduleAppointmentForm() {
        setTitle("Schedule Appointment");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the window non-resizable
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Patient dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Patient:"), gbc);
        gbc.gridx = 1;
        patientBox = new JComboBox<>();
        panel.add(patientBox, gbc);

        // Row 1-4: Patient details
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; nameValue = new JLabel("-"); panel.add(nameValue, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; ageValue = new JLabel("-"); panel.add(ageValue, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; genderValue = new JLabel("-"); panel.add(genderValue, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1; contactValue = new JLabel("-"); panel.add(contactValue, gbc);

        // Row 5: Doctor dropdown
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Select Doctor:"), gbc);
        gbc.gridx = 1;
        doctorBox = new JComboBox<>();
        panel.add(doctorBox, gbc);

        // Row 6-8: Date, Time, Reason
        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; dateField = new JTextField();
        dateField.setToolTipText("Enter date in YYYY-MM-DD format (e.g., 2024-01-15)");
        panel.add(dateField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Time Slot:"), gbc);
        gbc.gridx = 1; timeBox = new JComboBox<>(new String[]{"09:00 AM", "10:00 AM", "11:00 AM", "02:00 PM", "03:00 PM"});
        panel.add(timeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 8; panel.add(new JLabel("Reason for Visit:"), gbc);
        gbc.gridx = 1; reasonField = new JTextField(); panel.add(reasonField, gbc);

        // Row 9: Buttons panel
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton scheduleButton = new JButton("Schedule Appointment");
        JButton backButton = new JButton("Back");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(scheduleButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, gbc);

        add(panel);

        // Load data from DB
        loadPatients();
        loadDoctors();

        // Update patient details
        patientBox.addActionListener(e -> updatePatientDetails());

        // Schedule appointment
        scheduleButton.addActionListener(e -> saveAppointment());

        // Back button action
        backButton.addActionListener(e -> goBackToDashboard());

        // Clear button action
        clearButton.addActionListener(e -> clearForm());
    }

    private void goBackToDashboard() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to go back? Any unsaved changes will be lost.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close current form


        }
    }

    private void clearForm() {
        patientBox.setSelectedIndex(-1);
        doctorBox.setSelectedIndex(-1);
        dateField.setText("");
        timeBox.setSelectedIndex(0);
        reasonField.setText("");
        nameValue.setText("-");
        ageValue.setText("-");
        genderValue.setText("-");
        contactValue.setText("-");
    }

    private void loadPatients() {
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, full_name, dob, gender, phone FROM patients")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String display = rs.getString("full_name");
                patientMap.put(display, id);
                patientBox.addItem(display);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage());
        }
    }

    private void loadDoctors() {
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, full_name FROM doctors WHERE available = true")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String display = rs.getString("full_name");
                doctorMap.put(display, id);
                doctorBox.addItem(display);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors: " + e.getMessage());
        }
    }

    private void updatePatientDetails() {
        String selected = (String) patientBox.getSelectedItem();
        if (selected != null) {
            int id = patientMap.get(selected);
            try (Connection conn = ConnectionBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT full_name, dob, gender, phone FROM patients WHERE id = ?")) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    nameValue.setText(rs.getString("full_name"));

                    // Calculate age from dob
                    String dob = rs.getString("dob");
                    int age = java.time.LocalDate.now().getYear() - java.time.LocalDate.parse(dob).getYear();
                    ageValue.setText(String.valueOf(age));

                    genderValue.setText(rs.getString("gender"));
                    contactValue.setText(rs.getString("phone"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Clear patient details if no patient selected
            nameValue.setText("-");
            ageValue.setText("-");
            genderValue.setText("-");
            contactValue.setText("-");
        }
    }

    private boolean validateInputs() {
        // Check if patient is selected
        if (patientBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            patientBox.requestFocus();
            return false;
        }

        // Check if doctor is selected
        if (doctorBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            doctorBox.requestFocus();
            return false;
        }

        // Validate date
        String date = dateField.getText().trim();
        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter appointment date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in YYYY-MM-DD format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }

        // Check if date is not in the past
        if (isPastDate(date)) {
            JOptionPane.showMessageDialog(this, "Appointment date cannot be in the past.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            dateField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isPastDate(String dateStr) {
        try {
            LocalDate appointmentDate = LocalDate.parse(dateStr);
            return appointmentDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private void saveAppointment() {
        if (!validateInputs()) {
            return;
        }

        String patientName = (String) patientBox.getSelectedItem();
        String doctorName = (String) doctorBox.getSelectedItem();
        String date = dateField.getText().trim();
        String time = (String) timeBox.getSelectedItem();
        String reason = reasonField.getText().trim();

        int patientId = patientMap.get(patientName);
        int doctorId = doctorMap.get(doctorName);

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO appointments (patient_id, doctor_id, appointment_date, time_slot, reason) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setDate(3, Date.valueOf(date));
            stmt.setString(4, time);
            stmt.setString(5, reason);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Appointment scheduled successfully!");
                dispose();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error scheduling appointment: " + e.getMessage());
        }
    }
}