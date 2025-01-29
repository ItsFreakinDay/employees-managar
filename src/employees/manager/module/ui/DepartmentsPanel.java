package employees.manager.module.ui;

import employees.manager.module.dao.DepartmentDao;
import employees.manager.module.models.Department;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class DepartmentsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private DepartmentDao departmentDao;

    public DepartmentsPanel() {
        departmentDao = new DepartmentDao();
        setLayout(new BorderLayout());

        // Панель фильтрации
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Фильтр:"));
        searchField = new JTextField(20);
        filterPanel.add(searchField);
        JButton filterButton = new JButton("Искать");
        filterPanel.add(filterButton);

        // Панель кнопок управления
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Добавить отдел");
        JButton editButton = new JButton("Редактировать");
        JButton deleteButton = new JButton("Удалить");
        JButton refreshButton = new JButton("Обновить таблицу"); // Новая кнопка
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton); // Добавление кнопки "Обновить таблицу"

        // Таблица данных
        tableModel = new DefaultTableModel(new Object[]{"ID", "Название отдела"}, 0);
        table = new JTable(tableModel);
        loadDepartments("");

        // Добавление компонентов на панель
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Действия кнопок
        filterButton.addActionListener(e -> loadDepartments(searchField.getText()));

        addButton.addActionListener(e -> openDepartmentForm(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                openDepartmentForm(new Department(id, name));
            } else {
                JOptionPane.showMessageDialog(this, "Выберите отдел для редактирования");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(this, "Удалить отдел?", "Подтверждение",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (departmentDao.deleteDepartment(id)) {
                        JOptionPane.showMessageDialog(this, "Отдел удален.");
                        loadDepartments("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при удалении отдела.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Выберите отдел для удаления");
            }
        });

        refreshButton.addActionListener(e -> loadDepartments("")); // Обновление таблицы
    }

    private void loadDepartments(String filter) {
        tableModel.setRowCount(0); // Очистка таблицы
        List<Department> departments = departmentDao.getAllDepartments();
        for (Department department : departments) {
            if (filter.isEmpty() || department.getName().toLowerCase().contains(filter.toLowerCase())) {
                tableModel.addRow(new Object[]{department.getId(), department.getName()});
            }
        }
    }

    private void openDepartmentForm(Department department) {
        JTextField nameField = new JTextField(department != null ? department.getName() : "", 20);
        Object[] message = {"Название отдела:", nameField};

        int option = JOptionPane.showConfirmDialog(this, message,
                department == null ? "Добавить отдел" : "Редактировать отдел", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                if (department == null) {
                    if (departmentDao.addDepartment(name)) {
                        JOptionPane.showMessageDialog(this, "Отдел добавлен.");
                        loadDepartments("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при добавлении отдела.");
                    }
                } else {
                    if (departmentDao.updateDepartment(department.getId(), name)) {
                        JOptionPane.showMessageDialog(this, "Отдел обновлен.");
                        loadDepartments("");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при обновлении отдела.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Название отдела не может быть пустым.");
            }
        }
    }
}
