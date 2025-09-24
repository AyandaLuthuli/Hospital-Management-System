package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ManagePrescriptionsForm extends JFrame {
    private JComboBox<String> appointmentBox;
    private JLabel patientNameValue, ageValue, genderValue;
    private JTextField medicationField, dosageField, notesField;
    private JComboBox<String> doctorBox;

    // Maps to track IDs
    private Map<String, Integer> appointmentMap = new HashMap<>();
    private Map<String, Integer> doctorMap = new HashMap<>();

    public ManagePrescriptionsForm() {
        setTitle("Manage Prescriptions");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Appointment dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Select Appointment:"), gbc);
        gbc.gridx = 1;
        appointmentBox = new JComboBox<>();
        panel.add(appointmentBox, gbc);

        // Row 1-3: Patient details
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; patientNameValue = new JLabel("-"); panel.add(patientNameValue, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1; ageValue = new JLabel("-"); panel.add(ageValue, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1; genderValue = new JLabel("-"); panel.add(genderValue, gbc);

        // Row 4: Doctor
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Prescribed By:"), gbc);
        gbc.gridx = 1;
        doctorBox = new JComboBox<>();
        panel.add(doctorBox, gbc);

        // Row 5-7: Prescription fields
        gbc.gridx = 0; gbc.gridy = 5; panel.add(new JLabel("Medication:"), gbc);
        gbc.gridx = 1; medicationField = new JTextField(); panel.add(medicationField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; panel.add(new JLabel("Dosage:"), gbc);
        gbc.gridx = 1; dosageField = new JTextField(); panel.add(dosageField, gbc);

        gbc.gridx = 0; gbc.gridy = 7; panel.add(new JLabel("Instructions/Notes:"), gbc);
        gbc.gridx = 1; notesField = new JTextField(); panel.add(notesField, gbc);

        // Row 8: Save button
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JButton saveButton = new JButton("Save Prescription");
        panel.add(saveButton, gbc);

        add(panel);

        // Load appointments and doctors from DB
        loadAppointments();
        loadDoctors();

        // Update patient details when an appointment is selected
        appointmentBox.addActionListener(e -> updatePatientDetails());

        // Save prescription
        saveButton.addActionListener(e -> savePrescription());
    }

    private void loadAppointments() {
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT a.id AS appointment_id, p.full_name, p.dob, p.gender " +
                             "FROM appointments a " +
                             "JOIN patients p ON a.patient_id = p.id")) {

            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                String display = rs.getString("full_name") + " (ID:" + appointmentId + ")";
                appointmentMap.put(display, appointmentId);
                appointmentBox.addItem(display);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointments: " + e.getMessage());
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
        String selected = (String) appointmentBox.getSelectedItem();
        if (selected != null) {
            int appointmentId = appointmentMap.get(selected);
            try (Connection conn = ConnectionBD.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT p.full_name, p.dob, p.gender " +
                                 "FROM appointments a JOIN patients p ON a.patient_id = p.id " +
                                 "WHERE a.id = ?")) {

                stmt.setInt(1, appointmentId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("full_name");
                    patientNameValue.setText(fullName);

                    String dob = rs.getString("dob");
                    int age = java.time.LocalDate.now().getYear() - java.time.LocalDate.parse(dob).getYear();
                    ageValue.setText(String.valueOf(age));

                    genderValue.setText(rs.getString("gender"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePrescription() {
        String selectedAppointment = (String) appointmentBox.getSelectedItem();
        String selectedDoctor = (String) doctorBox.getSelectedItem();
        String medication = medicationField.getText().trim();
        String dosage = dosageField.getText().trim();
        String instructions = notesField.getText().trim();

        if (selectedAppointment == null || selectedDoctor == null || medication.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.");
            return;
        }

        int appointmentId = appointmentMap.get(selectedAppointment);
        int doctorId = doctorMap.get(selectedDoctor);

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO prescriptions (appointment_id, prescribed_by, medicine, dosage, instructions) " +
                             "VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, appointmentId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, medication);
            stmt.setString(4, dosage);
            stmt.setString(5, instructions);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Prescription saved successfully!");
                dispose();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription: " + e.getMessage());
        }
    }
}
