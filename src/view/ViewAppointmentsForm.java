package view;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewAppointmentsForm extends JFrame {
    private JTextArea appointmentsArea;

    public ViewAppointmentsForm(long patientId) {
        setTitle("Your Appointments");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        appointmentsArea = new JTextArea();
        appointmentsArea.setEditable(false);
        panel.add(new JScrollPane(appointmentsArea), BorderLayout.CENTER);

        add(panel);

        loadAppointments(patientId);
    }

    private void loadAppointments(long patientId) {
        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT a.id, a.appointment_date, a.time_slot, a.status, a.reason, d.full_name AS doctor_name " +
                             "FROM appointments a " +
                             "JOIN doctors d ON a.doctor_id = d.id " +
                             "WHERE a.patient_id = ? ORDER BY a.appointment_date DESC")) {

            stmt.setLong(1, patientId);
            ResultSet rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("Appointment ID: ").append(rs.getLong("id")).append("\n");
                sb.append("Date: ").append(rs.getDate("appointment_date")).append("\n");
                sb.append("Time: ").append(rs.getString("time_slot")).append("\n");
                sb.append("Doctor: ").append(rs.getString("doctor_name")).append("\n");
                sb.append("Status: ").append(rs.getString("status")).append("\n");
                sb.append("Reason: ").append(rs.getString("reason")).append("\n");
                sb.append("-------------------------------------------------\n");
            }

            if (sb.length() == 0) {
                sb.append("No appointments found.");
            }

            appointmentsArea.setText(sb.toString());

        } catch (SQLException ex) {
            ex.printStackTrace();
            appointmentsArea.setText("Error loading appointments: " + ex.getMessage());
        }
    }
}
