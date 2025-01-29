package employees.manager.module;

import employees.manager.module.ui.*;

import javax.swing.*;

public class MainApp extends JFrame {
    public MainApp() {
        setTitle("Управление персоналом");
        setSize(800, 600);
        setLocationRelativeTo(null); // Центрирует окно на экране
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Сотрудники", new EmployeePanel());
        tabbedPane.addTab("Должности", new PositionsPanel());
        tabbedPane.addTab("Отделы", new DepartmentsPanel());
        tabbedPane.addTab("Меню", new MenuPanel());
        tabbedPane.addTab("Справка", new HelpPanel());

        //tabbedPane.addTab("Статусы сотрудников", new EmployeeStatusesPanel());

        add(tabbedPane);

        setVisible(true);

    }

    public static void main(String[] args) {
        new MainApp();
    }
}
