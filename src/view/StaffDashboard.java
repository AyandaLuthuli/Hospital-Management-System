package view;

import javax.swing.*;
import java.awt.*;

public class StaffDashboard extends JFrame {
    public StaffDashboard() {
        setTitle("Staff Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton addPatientBtn = new JButton("Add New Patient");
        addPatientBtn.addActionListener(e -> new AddPatientForm().setVisible(true));
        panel.add(addPatientBtn);

        JButton scheduleBtn = new JButton("Schedule Appointment");
        scheduleBtn.addActionListener(e -> new ScheduleAppointmentForm().setVisible(true));
        panel.add(scheduleBtn);

        JButton prescriptionBtn = new JButton("Manage Prescriptions");
        prescriptionBtn.addActionListener(e -> new ManagePrescriptionsForm().setVisible(true));
        panel.add(prescriptionBtn);

        JButton searchBtn = new JButton("Search Records");
        searchBtn.addActionListener(e -> new SearchRecordsForm().setVisible(true));
        panel.add(searchBtn);

        JButton reportBtn = new JButton("Generate Reports");
        reportBtn.addActionListener(e -> new GenerateReportsForm().setVisible(true));
        panel.add(reportBtn);


        add(panel);
    }
}