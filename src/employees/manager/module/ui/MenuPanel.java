package employees.manager.module.ui;

import employees.manager.module.dao.EmployeeDao;
import employees.manager.module.models.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.net.URI;
import java.util.List;

public class MenuPanel extends JPanel {

    public MenuPanel() {
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icons/images.png"));
        ImageIcon scaledIcon = new ImageIcon(logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
        logoLabel.setIcon(scaledIcon);

        JLabel wishesLabel = new JLabel("Добро пожаловать! Успехов в работе.");
        wishesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        wishesLabel.setForeground(Color.GRAY);

        headerPanel.add(logoLabel);
        headerPanel.add(wishesLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel versionLabel = new JLabel("Версия: v. 0.0.1");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(Color.DARK_GRAY);

        footerPanel.add(versionLabel, BorderLayout.WEST);
        add(footerPanel, BorderLayout.SOUTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton settingsButton = new JButton("Настройки");
        JButton reportButton = new JButton("Печать отчета по сотрудникам");
        JButton projectLinkButton = new JButton("Ссылка на проект");
        JButton refreshButton = new JButton("Обновить данные");
        JButton exportButton = new JButton("Экспорт в Excel");
        JButton helpButton = new JButton("Помощь");
        JButton exitButton = new JButton("Выход");

        settingsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        projectLinkButton.setFont(new Font("Arial", Font.PLAIN, 14));
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exportButton.setFont(new Font("Arial", Font.PLAIN, 14));
        helpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));

        settingsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Функция еще не реализована.", "Настройки", JOptionPane.INFORMATION_MESSAGE));

//        reportButton.addActionListener(e -> {
//            JTable table = new JTable(new Object[][]{
//                    {"1", "Иван Иванов", "HR"},
//                    {"2", "Петр Петров", "IT"},
//                    {"3", "Сергей Сергеев", "Finance"}},
//                    new String[]{"ID", "Имя", "Отдел"});
//            try {
//                table.print();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        });
        reportButton.addActionListener(e -> {
            // Получаем список всех сотрудников
            EmployeeDao dao = new EmployeeDao();
            List<Employee> employees = dao.getAllEmployees();

            // Создаем модель таблицы на основе полученных данных
            Object[][] data = new Object[employees.size()][8]; // 8 колонок для всех полей
            for (int i = 0; i < employees.size(); i++) {
                Employee emp = employees.get(i);
                data[i][0] = emp.getId();
                data[i][1] = emp.getPersonnelNumber();
                data[i][2] = emp.getFullName();
                data[i][3] = emp.getBirthDate();
                data[i][4] = emp.getEmail();
                data[i][5] = emp.getPhone();
                data[i][6] = emp.getEducation();
                data[i][7] = emp.getCurrentPosition();
            }

            // Создаем таблицу и отправляем её на печать
            JTable table = new JTable(data, new String[] {
                    "ID",
                    "Табельный номер",
                    "ФИО",
                    "Дата рождения",
                    "Электронная почта",
                    "Телефон",
                    "Образование",
                    "Должность"
            });
            try {
                table.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        projectLinkButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        refreshButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Данные обновлены.", "Обновление", JOptionPane.INFORMATION_MESSAGE));

        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Экспорт завершен.", "Экспорт", JOptionPane.INFORMATION_MESSAGE));

        helpButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Справка по работе с программой.", "Помощь", JOptionPane.INFORMATION_MESSAGE));

        exitButton.addActionListener(e -> System.exit(0));

        centerPanel.add(settingsButton);
        centerPanel.add(reportButton);
        centerPanel.add(projectLinkButton);
        centerPanel.add(refreshButton);
        centerPanel.add(exportButton);
        centerPanel.add(helpButton);
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);
    }
}
