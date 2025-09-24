import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import view.LoginFrame;

import java.sql.*;

public class Main {
    public static void main(String[] args) {



//
//        try {  Connection connection= DriverManager.getConnection(
//                "jdbc:mysql://127.0.0.1:3306/hospital_db",
//                "root",
//                "0787474599Altie*"
//
//        );
//            Statement statement=connection.createStatement();
//            ResultSet resultSet=statement.executeQuery("SELECT * FROM USERS");
//
//            while (resultSet.next()){
//
//                System.out.println(resultSet.getString("username"));
//
//                System.out.println(resultSet.getString("password_hash"));
//
//                System.out.println(resultSet.getString("role"));
//            }
//
//        } catch (SQLException e){
//            e.printStackTrace();
//        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Failed to apply system look and feel" );
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}