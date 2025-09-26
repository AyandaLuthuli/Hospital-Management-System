package view;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JFrame {
    private long patientId;

    public PatientDashboard(long patientId) {
        this.patientId = patientId;

        setTitle("Patient Dashboard");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 rows now
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton viewAppointmentsBtn = new JButton("View Appointments");
        viewAppointmentsBtn.addActionListener(e -> new ViewAppointmentsForm(patientId).setVisible(true));
        panel.add(viewAppointmentsBtn);

        JButton viewPrescriptionsBtn = new JButton("View Prescriptions");
        viewPrescriptionsBtn.addActionListener(e -> new ViewPrescriptionsForm(patientId).setVisible(true));
        panel.add(viewPrescriptionsBtn);

        JButton searchDoctorsBtn = new JButton("Search Doctors");
        searchDoctorsBtn.addActionListener(e -> new SearchDoctorsForm().setVisible(true));
        panel.add(searchDoctorsBtn);

        // 🔹 Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true); // back to login
            this.dispose(); // close dashboard
        });
        panel.add(logoutBtn);

        add(panel);
    }
}
