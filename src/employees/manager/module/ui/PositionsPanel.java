package employees.manager.module.ui;

import employees.manager.module.dao.PositionDao;
import employees.manager.module.models.Position;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PositionsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> departmentFilter;
    private PositionDao positionDao;
    private Map<String, Integer> departmentMap; // Карта для связи названия отдела с его ID

    public PositionsPanel() {
        positionDao = new PositionDao();
        departmentMap = positionDao.getDepartmentMap(); // Загрузка карты отделов
        setLayout(new BorderLayout());

        // Панель фильтров
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Фильтр по названию:"));
        searchField = new JTextField(20);
        filterPanel.add(searchField);
        JButton searchButton = new JButton("Искать");
        filterPanel.add(searchButton);


        filterPanel.add(new JLabel("Фильтр по отделу:"));
        departmentFilter = new JComboBox<>();
        departmentFilter.addItem("Все отделы");
        departmentMap.keySet().forEach(departmentFilter::addItem);
        filterPanel.add(departmentFilter);

        // Панель управления
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Добавить должность");
        JButton editButton = new JButton("Редактировать");
        JButton deleteButton = new JButton("Удалить");
        JButton refreshButton = new JButton("Обновить таблицу");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Таблица
        tableModel = new DefaultTableModel(new Object[]{"ID", "Должность", "Отдел"}, 0);
        table = new JTable(tableModel);
        loadPositions("", "Все отделы");

        // Добавление компонентов на панель
        add(filterPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Действия кнопок
        searchButton.addActionListener(e -> loadPositions(searchField.getText(), (String) departmentFilter.getSelectedItem()));

        addButton.addActionListener(e -> openPositionForm(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                String department = (String) tableModel.getValueAt(selectedRow, 2);
                openPositionForm(new Position(id, name, department));
            } else {
                JOptionPane.showMessageDialog(this, "Выберите должность для редактирования.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(this, "Удалить должность?", "Подтверждение",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (positionDao.deletePosition(id)) {
                        JOptionPane.showMessageDialog(this, "Должность удалена.");
                        loadPositions("", "Все отделы");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при удалении должности.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Выберите должность для удаления.");
            }
        });

        refreshButton.addActionListener(e -> loadPositions("", "Все отделы"));
    }

    private void loadPositions(String filter, String department) {
        departmentMap = positionDao.getDepartmentMap();

        departmentFilter.removeAllItems();
        departmentFilter.addItem("Все отделы");

        for (String deptName : departmentMap.keySet()) {
            departmentFilter.addItem(deptName);
            System.out.println("Добавлен отдел: " + deptName); // Проверяем, добавляется ли в выпадающий список
        }

        tableModel.setRowCount(0);

        List<Position> positions = positionDao.getAllPositions();
        for (Position position : positions) {
            boolean matchesFilter = filter.isEmpty() || position.getName().toLowerCase().contains(filter.toLowerCase());
            boolean matchesDepartment = department.equals("Все отделы") || position.getDepartment().equals(department);
            if (matchesFilter && matchesDepartment) {
                tableModel.addRow(new Object[]{position.getId(), position.getName(), position.getDepartment()});
            }
        }
    }



    private void openPositionForm(Position position) {
        JTextField nameField = new JTextField(position != null ? position.getName() : "", 20);
        JComboBox<String> departmentBox = new JComboBox<>();
        departmentMap.keySet().forEach(departmentBox::addItem);

        if (position != null) {
            departmentBox.setSelectedItem(position.getDepartment());
        }

        Object[] message = {
                "Название должности:", nameField,
                "Отдел:", departmentBox
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                position == null ? "Добавить должность" : "Редактировать должность", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String department = (String) departmentBox.getSelectedItem();
            int departmentId = departmentMap.getOrDefault(department, -1);

            if (!name.isEmpty() && departmentId != -1) {
                if (position == null) {
                    if (positionDao.isPositionExists(name)) {
                        JOptionPane.showMessageDialog(this, "Должность с таким названием уже существует.");
                    } else if (positionDao.addPosition(name, departmentId)) {
                        JOptionPane.showMessageDialog(this, "Должность добавлена.");
                        loadPositions("", "Все отделы");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при добавлении должности.");
                    }
                } else {
                    if (positionDao.isPositionExists(name) && !name.equals(position.getName())) {
                        JOptionPane.showMessageDialog(this, "Должность с таким названием уже существует.");
                    } else if (positionDao.updatePosition(position.getId(), name, departmentId)) {
                        JOptionPane.showMessageDialog(this, "Должность обновлена.");
                        loadPositions("", "Все отделы");
                    } else {
                        JOptionPane.showMessageDialog(this, "Ошибка при обновлении должности.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Все поля должны быть заполнены.");
            }
        }
    }

}
