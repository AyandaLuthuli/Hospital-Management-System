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
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); // Prevent resizing to maintain fixed layout

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Manage Prescriptions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load appointments and doctors from DB
        loadAppointments();
        loadDoctors();

        // Update patient details when an appointment is selected
        appointmentBox.addActionListener(e -> updatePatientDetails());
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Fixed sizes for consistent layout
        Dimension fieldSize = new Dimension(250, 25);
        Dimension labelSize = new Dimension(120, 25);

        // Row 0: Appointment dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel appointmentLabel = new JLabel("Select Appointment:");
        appointmentLabel.setPreferredSize(labelSize);
        panel.add(appointmentLabel, gbc);

        gbc.gridx = 1;
        appointmentBox = new JComboBox<>();
        appointmentBox.setPreferredSize(fieldSize);
        panel.add(appointmentBox, gbc);

        // Row 1: Patient Details Panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JPanel patientPanel = createPatientPanel();
        patientPanel.setPreferredSize(new Dimension(500, 80));
        panel.add(patientPanel, gbc);

        // Row 2: Doctor
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel doctorLabel = new JLabel("Prescribed By:");
        doctorLabel.setPreferredSize(labelSize);
        panel.add(doctorLabel, gbc);

        gbc.gridx = 1;
        doctorBox = new JComboBox<>();
        doctorBox.setPreferredSize(fieldSize);
        panel.add(doctorBox, gbc);

        // Row 3: Prescription Details Panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        JPanel prescriptionPanel = createPrescriptionPanel();
        prescriptionPanel.setPreferredSize(new Dimension(500, 120));
        panel.add(prescriptionPanel, gbc);

        return panel;
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Patient Information"));
        panel.setPreferredSize(new Dimension(480, 80));

        // Fixed size for value labels
        Dimension valueSize = new Dimension(150, 20);

        JLabel nameLabel = new JLabel("Name:");
        panel.add(nameLabel);
        patientNameValue = new JLabel("-");
        patientNameValue.setPreferredSize(valueSize);
        patientNameValue.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        panel.add(patientNameValue);

        JLabel ageLabel = new JLabel("Age:");
        panel.add(ageLabel);
        ageValue = new JLabel("-");
        ageValue.setPreferredSize(valueSize);
        ageValue.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        panel.add(ageValue);

        JLabel genderLabel = new JLabel("Gender:");
        panel.add(genderLabel);
        genderValue = new JLabel("-");
        genderValue.setPreferredSize(valueSize);
        genderValue.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        panel.add(genderValue);

        return panel;
    }

    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Prescription Details"));
        panel.setPreferredSize(new Dimension(480, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(300, 25);

        // Medication
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel medLabel = new JLabel("Medication:");
        medLabel.setPreferredSize(labelSize);
        panel.add(medLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        medicationField = new JTextField();
        medicationField.setPreferredSize(fieldSize);
        panel.add(medicationField, gbc);

        // Dosage
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel dosageLabel = new JLabel("Dosage:");
        dosageLabel.setPreferredSize(labelSize);
        panel.add(dosageLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        dosageField = new JTextField();
        dosageField.setPreferredSize(fieldSize);
        panel.add(dosageField, gbc);

        // Instructions/Notes
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel notesLabel = new JLabel("Instructions/Notes:");
        notesLabel.setPreferredSize(labelSize);
        panel.add(notesLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        notesField = new JTextField();
        notesField.setPreferredSize(fieldSize);
        panel.add(notesField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.setPreferredSize(new Dimension(500, 50));

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> dispose());

        JButton saveButton = new JButton("Save Prescription");
        saveButton.setPreferredSize(new Dimension(140, 30));
        saveButton.addActionListener(e -> savePrescription());

        panel.add(backButton);
        panel.add(saveButton);

        return panel;
    }

    private void loadAppointments() {
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT a.id AS appointment_id, p.full_name, p.dob, p.gender " +
                             "FROM appointments a " +
                             "JOIN patients p ON a.patient_id = p.id")) {

            // Add empty default option
            appointmentBox.addItem("-- Select Appointment --");

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

            // Add empty default option
            doctorBox.addItem("-- Select Doctor --");

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
        if (selected != null && !selected.equals("-- Select Appointment --")) {
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
        } else {
            // Clear patient details if no appointment is selected
            patientNameValue.setText("-");
            ageValue.setText("-");
            genderValue.setText("-");
        }
    }

    private void savePrescription() {
        String selectedAppointment = (String) appointmentBox.getSelectedItem();
        String selectedDoctor = (String) doctorBox.getSelectedItem();
        String medication = medicationField.getText().trim();
        String dosage = dosageField.getText().trim();
        String instructions = notesField.getText().trim();

        // Validation
        if (selectedAppointment == null || selectedAppointment.equals("-- Select Appointment --")) {
            JOptionPane.showMessageDialog(this, "Please select an appointment.");
            return;
        }

        if (selectedDoctor == null || selectedDoctor.equals("-- Select Doctor --")) {
            JOptionPane.showMessageDialog(this, "Please select a doctor.");
            return;
        }

        if (medication.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter medication name.");
            medicationField.requestFocus();
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
                clearForm();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving prescription: " + e.getMessage());
        }
    }

    private void clearForm() {
        appointmentBox.setSelectedIndex(0);
        doctorBox.setSelectedIndex(0);
        medicationField.setText("");
        dosageField.setText("");
        notesField.setText("");
        patientNameValue.setText("-");
        ageValue.setText("-");
        genderValue.setText("-");
    }
}