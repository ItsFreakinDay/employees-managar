package employees.manager.module.ui;

import employees.manager.module.dao.EmployeeDao;
import employees.manager.module.models.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeePanel extends JPanel {
    private final EmployeeDao employeeDao;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public EmployeePanel() {
        this.employeeDao = new EmployeeDao();

        setLayout(new BorderLayout());

        // Модель таблицы
        tableModel = new DefaultTableModel(new String[]{
                "Табельный номер", "ФИО", "Дата рождения", "Электронная почта", "Телефон",
                "Образование", "Должность", "Статус"
        }, 0);
        table = new JTable(tableModel);

        // Добавление таблицы на панель
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Панель с кнопками
        JPanel buttonsPanel = new JPanel();
        JButton addButton = new JButton("Добавить");
        JButton editButton = new JButton("Редактировать");
        JButton deleteButton = new JButton("Удалить");
        JButton refreshButton = new JButton("Обновить");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Обработчики кнопок
        addButton.addActionListener(e -> addEmployee());
        editButton.addActionListener(e -> editEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        refreshButton.addActionListener(e -> loadEmployees());

        // Загрузка данных при запуске
        loadEmployees();
    }

    private void loadEmployees() {
        tableModel.setRowCount(0); // Очистка таблицы
        List<Employee> employees = employeeDao.getAllEmployees();
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{
                    emp.getPersonnelNumber(), emp.getFullName(), emp.getBirthDate(), emp.getEmail(),
                    emp.getPhone(), emp.getEducation(), emp.getCurrentPosition(), emp.getCurrentStatus()
            });
        }
    }

private void addEmployee() {
    JTextField personnelNumberField = new JTextField();
    JTextField fullNameField = new JTextField();
    JTextField birthDateField = new JTextField();
    JTextField emailField = new JTextField();
    JTextField phoneField = new JTextField();
    JTextField educationField = new JTextField();

    JComboBox<String> positionComboBox = new JComboBox<>(employeeDao.getAllPositions().toArray(new String[0]));
    JComboBox<String> statusComboBox = new JComboBox<>(employeeDao.getAllStatuses().toArray(new String[0]));

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Табельный номер:"));
    panel.add(personnelNumberField);
    panel.add(new JLabel("ФИО:"));
    panel.add(fullNameField);
    panel.add(new JLabel("Дата рождения (YYYY-MM-DD):"));
    panel.add(birthDateField);
    panel.add(new JLabel("Электронная почта:"));
    panel.add(emailField);
    panel.add(new JLabel("Телефон:"));
    panel.add(phoneField);
    panel.add(new JLabel("Образование:"));
    panel.add(educationField);
    panel.add(new JLabel("Должность:"));
    panel.add(positionComboBox);
    panel.add(new JLabel("Статус:"));
    panel.add(statusComboBox);

    int result = JOptionPane.showConfirmDialog(this, panel, "Добавить сотрудника",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        Employee newEmployee = new Employee();
        newEmployee.setPersonnelNumber(personnelNumberField.getText());
        newEmployee.setFullName(fullNameField.getText());
        newEmployee.setBirthDate(birthDateField.getText());
        newEmployee.setEmail(emailField.getText());
        newEmployee.setPhone(phoneField.getText());
        newEmployee.setEducation(educationField.getText());
        newEmployee.setCurrentPosition((String) positionComboBox.getSelectedItem());
        newEmployee.setCurrentStatus((String) statusComboBox.getSelectedItem());

        if (employeeDao.addEmployee(newEmployee)) {
            JOptionPane.showMessageDialog(this, "Сотрудник успешно добавлен.");
            loadEmployees();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при добавлении сотрудника.");
        }
    }
}

//private void editEmployee() {
//    int selectedRow = table.getSelectedRow();
//    if (selectedRow == -1) {
//        JOptionPane.showMessageDialog(this, "Выберите сотрудника для редактирования.");
//        return;
//    }
//
//    String personnelNumber = tableModel.getValueAt(selectedRow, 0).toString();
//    Employee employee = employeeDao.getAllEmployees().stream()
//            .filter(emp -> emp.getPersonnelNumber().equals(personnelNumber))
//            .findFirst().orElse(null);
//
//    if (employee == null) {
//        JOptionPane.showMessageDialog(this, "Сотрудник не найден.");
//        return;
//    }
//
//    JTextField fullNameField = new JTextField(employee.getFullName());
//    JTextField birthDateField = new JTextField(employee.getBirthDate());
//    JTextField emailField = new JTextField(employee.getEmail());
//    JTextField phoneField = new JTextField(employee.getPhone());
//    JTextField educationField = new JTextField(employee.getEducation());
//
//    JComboBox<String> positionComboBox = new JComboBox<>(employeeDao.getAllPositions().toArray(new String[0]));
//    positionComboBox.setSelectedItem(employee.getCurrentPosition());
//
//    JComboBox<String> statusComboBox = new JComboBox<>(employeeDao.getAllStatuses().toArray(new String[0]));
//    statusComboBox.setSelectedItem(employee.getCurrentStatus());
//
//    JPanel panel = new JPanel(new GridLayout(0, 1));
//    panel.add(new JLabel("ФИО:"));
//    panel.add(fullNameField);
//    panel.add(new JLabel("Дата рождения (YYYY-MM-DD):"));
//    panel.add(birthDateField);
//    panel.add(new JLabel("Электронная почта:"));
//    panel.add(emailField);
//    panel.add(new JLabel("Телефон:"));
//    panel.add(phoneField);
//    panel.add(new JLabel("Образование:"));
//    panel.add(educationField);
//    panel.add(new JLabel("Должность:"));
//    panel.add(positionComboBox);
//    panel.add(new JLabel("Статус:"));
//    panel.add(statusComboBox);
//
//    int result = JOptionPane.showConfirmDialog(this, panel, "Редактировать сотрудника",
//            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//
//    if (result == JOptionPane.OK_OPTION) {
//        employee.setFullName(fullNameField.getText());
//        employee.setBirthDate(birthDateField.getText());
//        employee.setEmail(emailField.getText());
//        employee.setPhone(phoneField.getText());
//        employee.setEducation(educationField.getText());
//        employee.setCurrentPosition((String) positionComboBox.getSelectedItem());
//        employee.setCurrentStatus((String) statusComboBox.getSelectedItem());
//
//        if (employeeDao.updateEmployee(employee)) {
//            JOptionPane.showMessageDialog(this, "Сотрудник успешно обновлён.");
//            loadEmployees();
//        } else {
//            JOptionPane.showMessageDialog(this, "Ошибка при обновлении сотрудника.");
//        }
//    }
//}
private void editEmployee() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Выберите сотрудника для редактирования.");
        return;
    }

    String personnelNumber = tableModel.getValueAt(selectedRow, 0).toString();
    Employee employee = employeeDao.getAllEmployees().stream()
            .filter(emp -> emp.getPersonnelNumber().equals(personnelNumber))
            .findFirst().orElse(null);

    if (employee == null) {
        JOptionPane.showMessageDialog(this, "Сотрудник не найден.");
        return;
    }

    // Поля для редактирования данных
    JTextField fullNameField = new JTextField(employee.getFullName());
    JTextField birthDateField = new JTextField(employee.getBirthDate());
    JTextField emailField = new JTextField(employee.getEmail());
    JTextField phoneField = new JTextField(employee.getPhone());
    JTextField educationField = new JTextField(employee.getEducation());

    // Выпадающие списки для статуса и должности
    JComboBox<String> positionComboBox = new JComboBox<>(employeeDao.getAllPositions().toArray(new String[0]));
    positionComboBox.setSelectedItem(employee.getCurrentPosition());

    JComboBox<String> statusComboBox = new JComboBox<>(employeeDao.getAllStatuses().toArray(new String[0]));
    statusComboBox.setSelectedItem(employee.getCurrentStatus());

    // Панель для отображения полей
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("ФИО:"));
    panel.add(fullNameField);
    panel.add(new JLabel("Дата рождения (YYYY-MM-DD):"));
    panel.add(birthDateField);
    panel.add(new JLabel("Электронная почта:"));
    panel.add(emailField);
    panel.add(new JLabel("Телефон:"));
    panel.add(phoneField);
    panel.add(new JLabel("Образование:"));
    panel.add(educationField);
    panel.add(new JLabel("Должность:"));
    panel.add(positionComboBox);
    panel.add(new JLabel("Статус:"));
    panel.add(statusComboBox);

    int result = JOptionPane.showConfirmDialog(this, panel, "Редактировать сотрудника",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Обновляем данные объекта
        employee.setFullName(fullNameField.getText());
        employee.setBirthDate(birthDateField.getText());
        employee.setEmail(emailField.getText());
        employee.setPhone(phoneField.getText());
        employee.setEducation(educationField.getText());
        employee.setCurrentPosition((String) positionComboBox.getSelectedItem());
        employee.setCurrentStatus((String) statusComboBox.getSelectedItem());

        // Обновляем запись в БД
        if (employeeDao.updateEmployee(employee)) {
            JOptionPane.showMessageDialog(this, "Сотрудник успешно обновлён.");
            loadEmployees();
        } else {
            JOptionPane.showMessageDialog(this, "Ошибка при обновлении сотрудника.");
        }
    }
}


    private void deleteEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите сотрудника для удаления.");
            return;
        }

        String personnelNumber = tableModel.getValueAt(selectedRow, 0).toString();
        Employee employee = employeeDao.getAllEmployees().stream()
                .filter(emp -> emp.getPersonnelNumber().equals(personnelNumber))
                .findFirst().orElse(null);

        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Сотрудник не найден.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить сотрудника: " + employee.getFullName() + "?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            if (employeeDao.deleteEmployee(employee.getId())) {
                JOptionPane.showMessageDialog(this, "Сотрудник успешно удалён.");
                loadEmployees();
            } else {
                JOptionPane.showMessageDialog(this, "Ошибка при удалении сотрудника.");
            }
        }
    }
}
