package employees.manager.module.login;

import employees.manager.module.Conn;
import employees.manager.module.MainApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class Login extends JFrame implements ActionListener {

    private final JTextField tusername;
    private final JPasswordField tpassword;
    private final JButton login, back;

    public Login() {
        super("Employee Management System - Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPanel = new JPanel(new BorderLayout());
        setContentPane(contentPanel);

        // Create the form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        tusername = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(tusername, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        tpassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(tpassword, gbc);

        login = new JButton("LOGIN");
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(login, gbc);

        back = new JButton("BACK");
        back.setBackground(Color.BLACK);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(back, gbc);

        contentPanel.add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            try {
                String username = tusername.getText();
                String password = String.valueOf(tpassword.getPassword());

                // Получаем экземпляр класса Conn
                Conn dbConn = Conn.getInstance();

                // Получаем соединение с базой данных
                Connection connection = dbConn.getConnection();

                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM users WHERE username = ? AND password = ?");
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    dispose();
                    new MainApp();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                statement.close();
                connection.close(); // Закрываем соединение после использования

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == back) {
            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
