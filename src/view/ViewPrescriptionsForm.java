package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewPrescriptionsForm extends JFrame {
    private long patientId;

    public ViewPrescriptionsForm(long patientId) {
        this.patientId = patientId;

        setTitle("Your Prescriptions");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea prescriptionsArea = new JTextArea();
        prescriptionsArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(prescriptionsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Load prescriptions for this patient
        loadPrescriptions(prescriptionsArea);
    }

    private void loadPrescriptions(JTextArea prescriptionsArea) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT p.medicine, p.dosage, p.instructions, d.full_name AS doctor, a.appointment_date " +
                             "FROM prescriptions p " +
                             "JOIN appointments a ON p.appointment_id = a.id " +
                             "JOIN doctors d ON p.prescribed_by = d.id " +
                             "WHERE a.patient_id = ?")) {

            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Appointment Date: ").append(rs.getDate("appointment_date")).append("\n")
                        .append("Medicine: ").append(rs.getString("medicine")).append("\n")
                        .append("Dosage: ").append(rs.getString("dosage")).append("\n")
                        .append("Instructions: ").append(rs.getString("instructions")).append("\n")
                        .append("Prescribed By: Dr. ").append(rs.getString("doctor")).append("\n")
                        .append("-------------------------------------------------\n");
            }

            if (sb.length() == 0) {
                prescriptionsArea.setText("No prescriptions found.");
            } else {
                prescriptionsArea.setText(sb.toString());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            prescriptionsArea.setText("Error loading prescriptions: " + ex.getMessage());
        }
    }
}
