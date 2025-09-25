package view;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JFrame {
    private long patientId;

    public PatientDashboard(long patientId) {
        this.patientId = patientId;

        setTitle("Patient Dashboard");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
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

        add(panel);
    }
}
