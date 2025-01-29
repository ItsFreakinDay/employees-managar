package employees.manager.module.ui;

import javax.swing.*;
import java.awt.*;

public class HelpPanel extends JPanel {

    public HelpPanel() {
        setLayout(new BorderLayout());

        // Заголовок
        JLabel titleLabel = new JLabel("Справка", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Информация о программе
        JTextArea aboutText = new JTextArea("Система управления персоналом\nВерсия 0.0.1\nГод 2025");
        aboutText.setEditable(false);
        aboutText.setFont(new Font("Arial", Font.PLAIN, 16));
        aboutText.setBackground(getBackground()); // Сохраняем цвет фона панели
        add(aboutText, BorderLayout.CENTER);
    }
}
