package view;

import javax.swing.*;
import java.awt.*;

public class SearchDoctorsForm extends JFrame {
    public SearchDoctorsForm() {
        setTitle("Search Doctors");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Specialization:"));
        panel.add(new JTextField());

        panel.add(new JLabel("Location:"));
        panel.add(new JTextField());

        JButton searchButton = new JButton("Search");
        panel.add(searchButton);

        add(panel);
    }
}